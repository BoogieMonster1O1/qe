package com.shrishdeshpande.qe.common;

import com.shrishdeshpande.qe.api.Block;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Blockchain {
    private static Blockchain instance;

    private final Lock lock;

    private final String path;

    private final List<Block> blocks;

    private Blockchain(String path) {
        this.path = path;
        this.blocks = new LinkedList<>();
        this.lock = new ReentrantLock();
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void addBlock(Block block) {
        this.lock.lock();
        blocks.add(block);
        this.lock.unlock();
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
