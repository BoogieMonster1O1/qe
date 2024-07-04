package com.shrishdeshpande.qe.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shrishdeshpande.qe.api.Block;
import com.shrishdeshpande.qe.api.transaction.ContractTransaction;
import com.shrishdeshpande.qe.api.transaction.MintTransaction;
import com.shrishdeshpande.qe.api.transaction.NftTransaction;
import com.shrishdeshpande.qe.api.transaction.Transaction;
import com.shrishdeshpande.qe.client.BlockchainClient;
import com.shrishdeshpande.qe.server.BlockchainServer;
import com.shrishdeshpande.qe.util.SocketMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.IGNORE_UNKNOWN;

public class Blockchain {


    private static final Logger LOGGER = LogManager.getLogger(Blockchain.class);
    private static Blockchain instance;

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    private final Lock lock;

    private final File file;

    private final List<Block> blocks;

    private Blockchain(File path) {
        this.file = path;
        this.blocks = new LinkedList<>();
        this.lock = new ReentrantLock();
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void replaceChain(List<Block> blocks) {
        this.lock.lock();

        List<Block> newBlocks = new LinkedList<>();
        for (Block block : blocks) {
            if (!this.blocks.contains(block)) {
                newBlocks.add(block);
            }
        }

        this.blocks.clear();

        this.blocks.addAll(blocks);

        List<Transaction> transactions = newBlocks.stream()
                .flatMap(block -> block.getTransactions().stream())
                .toList();

        boolean dirty = false;

        for (Transaction e : transactions) {
            if (e instanceof ContractTransaction && Objects.equals(e.getRecipient(), BlockchainClient.getInstance().name)) {
                LOGGER.info("Executing contract transaction: {}", e);
                if (((ContractTransaction) e).getAmount() < 100) {
                    LOGGER.error("Contract transaction amount too low: {}", e);
                    break;
                }
                List<String> currentNfts = getCurrentNfts();
                if (currentNfts.isEmpty()) {
                    LOGGER.error("Not enough NFTs to execute contract transaction: {}", e);
                    break;
                }
                NftTransaction newTrans = new NftTransaction(BlockchainClient.getInstance().name, e.getSender(), System.currentTimeMillis(), currentNfts.get(0), 0);
                BlockchainServer.getInstance().addTransaction(newTrans);
                dirty = true;
            }
        }

        this.lock.unlock();

        if (dirty) {
            Block last = Blockchain.getInstance().getBlocks().getLast();
            Block block = BlockchainServer.getInstance().mine(last);
            Blockchain.getInstance().addBlock(block);
            SocketMessages.newBlock(Blockchain.getInstance().getBlocks());
            BlockchainServer.getInstance().clearMemPool();
        }
    }

    public @NotNull List<String> getCurrentNfts() {
        List<String> currentNfts = new LinkedList<>();
        for (Block block : this.blocks) {
            for (Transaction transaction : block.getTransactions()) {
                if (transaction instanceof MintTransaction mt && Objects.equals(mt.getRecipient(), BlockchainClient.getInstance().name)) {
                    currentNfts.add(mt.getIndivisibleId());
                } else if (transaction instanceof NftTransaction nt && Objects.equals(nt.getSender(), BlockchainClient.getInstance().name)) {
                    currentNfts.remove(nt.getIndivisibleId());
                }
            }
        }
        return currentNfts;
    }

    public void addBlock(Block block) {
        this.lock.lock();
        blocks.add(block);
        blocks.sort(Comparator.comparing(Block::timestamp));
        this.lock.unlock();

        this.lock.lock();
        writeToFile();
        this.lock.unlock();
    }

    private void writeToFile() {
        try {
            OBJECT_MAPPER.writeValue(file, blocks);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write blocks to file", e);
        }
    }

    public static void init(String path) {
        List<Block> blocks;
        Path bcpath = Path.of(path, "c.json");
        instance = new Blockchain(bcpath.toFile());

        if (!Files.exists(bcpath)) {
            try {
                Files.createFile(bcpath);
                Files.write(bcpath, "[]".getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                blocks = OBJECT_MAPPER.readValue(bcpath.toFile(), new TypeReference<>() {});
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            instance.blocks.addAll(blocks);
            blocks.sort(Comparator.comparing(Block::timestamp));
        }
    }

    public static Blockchain getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Blockchain has not been initialized");
        }
        return instance;
    }
}
