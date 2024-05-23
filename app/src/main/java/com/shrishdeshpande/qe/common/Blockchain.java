package com.shrishdeshpande.qe.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shrishdeshpande.qe.api.Block;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Blockchain {
    private static Blockchain instance;

    private final ObjectMapper objectMapper;

    private final Lock lock;

    private final File file;

    private final List<Block> blocks;

    private Blockchain(String path) {
        this.file = new File(path);
        this.blocks = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.objectMapper = new ObjectMapper();
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void addBlock(Block block) {
        this.lock.lock();
        blocks.add(block);
        this.lock.unlock();

        this.lock.lock();
        writeToFile();
        this.lock.unlock();
    }

    private void writeToFile() {
        // Write blocks to file

        try {
            objectMapper.writeValue(file, blocks);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write blocks to file", e);
        }
    }

    public static void init(String path) {
        instance = new Blockchain(path);
    }

    public static Blockchain getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Blockchain has not been initialized");
        }
        return instance;
    }
}
