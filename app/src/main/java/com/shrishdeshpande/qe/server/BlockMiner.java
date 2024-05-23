package com.shrishdeshpande.qe.server;

import com.shrishdeshpande.qe.api.transaction.Transaction;
import com.shrishdeshpande.qe.util.TaskQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Queue;

public class BlockMiner {
    private static final Logger LOGGER = LogManager.getLogger(BlockMiner.class);

    public static final Thread MINING_THREAD;
    public static final TaskQueue TASK_QUEUE;

    private final List<Transaction> transactions;

    public BlockMiner(List<Transaction> transactions) {
        this.transactions = transactions;
        LOGGER.info("Initialized block miner with {} transactions", transactions.size());
    }

    public void mine(Cancellation cancellation) {
        LOGGER.info("Mining block with {} transactions...", transactions.size());

        while (!cancellation.isCancelled()) {
            // TODO: Implement mining logic
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
