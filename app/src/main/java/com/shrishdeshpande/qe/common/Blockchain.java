package com.shrishdeshpande.qe.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.shrishdeshpande.qe.api.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
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
