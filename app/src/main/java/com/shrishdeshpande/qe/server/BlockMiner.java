package com.shrishdeshpande.qe.server;

import com.shrishdeshpande.qe.api.Block;
import com.shrishdeshpande.qe.api.BlockHeader;
import com.shrishdeshpande.qe.api.transaction.Transaction;
import com.shrishdeshpande.qe.api.transaction.TransactionTree;
import com.shrishdeshpande.qe.util.TaskQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class BlockMiner {
    private static final Logger LOGGER = LogManager.getLogger(BlockMiner.class);

    public static final Thread MINING_THREAD;
    public static final TaskQueue TASK_QUEUE;

    private final List<Transaction> transactions;

    public BlockMiner(List<Transaction> transactions) {
        this.transactions = transactions;
        LOGGER.info("Initialized block miner with {} blocks", transactions.size());
    }

    public Block mine(String whoami, Block last) {
        LOGGER.info("Mining block with {} blocks...", transactions.size());
        TransactionTree tree = new TransactionTree(transactions);
        String merkleRoot = tree.rootHash();
        String previousHash = last == null ? "" : last.getHeader().hash();
        int numTransactions = transactions.size();
        String miner = whoami;
        long nonce = 0;
        while (true) {
            byte[] hash = BlockHeader.hash(previousHash, nonce, merkleRoot);
            if (nonce % 25 == 0) {
//                LOGGER.info("Mining block with nonce {}", nonce);
            }
            // Difficulty: 8 leading zeros
            if (hash[0] == 0 && hash[1] == 0) {
                LOGGER.info("Mined block with nonce {}", nonce);
                return new Block.Builder()
                        .header(new BlockHeader.Builder()
                                .setHash(BlockHeader.hashHex(previousHash, nonce, merkleRoot))
                                .setPreviousHash(previousHash)
                                .setTimestamp(System.currentTimeMillis())
                                .setNonce(nonce)
                                .setMerkleRoot(merkleRoot)
                                .setNumTransactions(numTransactions)
                                .setMiner(miner)
                                .build())
                        .transactions(transactions)
                        .build();
            }
            nonce++;
        }
    }

    public void mine(Cancellation cancellation) {
        LOGGER.info("Mining block with {} blocks...", transactions.size());

        while (!cancellation.isCancelled()) {
            // TODO: parallelize mining
        }

        LOGGER.info("Mining cancelled");
    }

    public static class Cancellation {
        private boolean cancelled = false;

        public void cancel() {
            cancelled = true;
        }

        public boolean isCancelled() {
            return cancelled;
        }
    }

    private static void miningThread() {
        while (true) {
            try {
                TASK_QUEUE.execute();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LOGGER.error("Mining thread interrupted", e);
            }
        }
    }

    static {
        MINING_THREAD = new Thread(BlockMiner::miningThread);
        TASK_QUEUE = new TaskQueue(MINING_THREAD);

        MINING_THREAD.setName("Mining Thread");
        MINING_THREAD.start();
    }
}
