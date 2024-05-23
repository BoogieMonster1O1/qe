package com.shrishdeshpande.qe.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlockchainClient {
    public static final Logger LOGGER = LogManager.getLogger(BlockchainClient.class);

    private static BlockchainClient instance;

    private final String path;

    private BlockchainClient(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public static void init(String path) {
        LOGGER.info("Initializing blockchain client");
        instance = new BlockchainClient(path);
    }

    public static BlockchainClient getInstance() {
        if (instance == null) {
            LOGGER.error("Tried to get instance of client before initializing");
            throw new IllegalStateException("Client has not been initialized");
        }
        return instance;
    }
}
