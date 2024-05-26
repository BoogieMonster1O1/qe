package com.shrishdeshpande.qe.server;

import com.shrishdeshpande.qe.api.transaction.Transaction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class BlockchainServer {
    public static final Logger LOGGER = LogManager.getLogger(BlockchainServer.class);

    private static BlockchainServer instance;

    private final List<Transaction> mempool = new ArrayList<>();

    public static BlockchainServer getInstance() {
        if (instance == null) {
            instance = new BlockchainServer();
        }
        return instance;
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
}
