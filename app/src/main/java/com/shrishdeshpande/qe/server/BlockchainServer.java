package com.shrishdeshpande.qe.server;

import com.shrishdeshpande.qe.api.Block;
import com.shrishdeshpande.qe.api.transaction.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class BlockchainServer {
    public static final Logger LOGGER = LogManager.getLogger(BlockchainServer.class);

    private static BlockchainServer instance;

    public final List<Transaction> mempool = new ArrayList<>();

    private final String name;

    private BlockchainServer(String name) {
        this.name = name;
    }

    public static void initWithName(String name) {
        instance = new BlockchainServer(name);
    }

    public static BlockchainServer getInstance() {
        if (instance == null) {
            throw new IllegalStateException("BlockchainServer not initialized");
        }
        return instance;
    }

    public Block genesis() {
        BlockMiner miner = new BlockMiner(new ArrayList<>());
        return miner.mine(name, null);
    }

    public Block mine(Block previous) {
        BlockMiner miner = new BlockMiner(mempool);
        return miner.mine(name, previous);
    }

    public void addTransaction(Transaction transaction) {
        // Check if transaction exists
        if (mempool.contains(transaction)) {
            LOGGER.warn("Transaction already exists in mempool: {}", transaction);
            return;
        }
        mempool.add(transaction);
        LOGGER.info("Added transaction to mempool: {}", transaction);
    }

    public void removeTransactions(List<Block> blockchain) {
        for (Block block : blockchain) {
            for (Transaction transaction : block.getTransactions()) {
                mempool.remove(transaction);
            }
        }
    }

    public void clearMemPool() {
        mempool.clear();
    }
}
