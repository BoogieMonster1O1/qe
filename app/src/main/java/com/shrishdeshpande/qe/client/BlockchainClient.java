package com.shrishdeshpande.qe.client;

import com.wultra.security.pqc.sike.crypto.KeyGenerator;
import com.wultra.security.pqc.sike.model.ImplementationType;
import com.wultra.security.pqc.sike.model.Party;
import com.wultra.security.pqc.sike.param.SikeParam;
import com.wultra.security.pqc.sike.param.SikeParamP434;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyPair;

public class BlockchainClient {
    public static final Logger LOGGER = LogManager.getLogger(BlockchainClient.class);

    private static BlockchainClient instance;

    public static final SikeParam SIKE_PARAM = new SikeParamP434(ImplementationType.OPTIMIZED);
    public static final KeyGenerator KEY_GENERATOR = new KeyGenerator(SIKE_PARAM);

    private final String path;

    public final KeyPair keyPair;

    private BlockchainClient(String path) {
        this.path = path;
        Path keyPairPath = Path.of(path, "keypair");
        if (Files.exists(keyPairPath)) {
            LOGGER.info("Loading keypair from {}", keyPairPath);
            ObjectInputStream ois;
            try {
                ois = new ObjectInputStream(Files.newInputStream(keyPairPath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                keyPair = (KeyPair) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            LOGGER.info("Generating keypair and saving to {}", keyPairPath);
            KeyPair keyPair;
            try {
                keyPair = KEY_GENERATOR.generateKeyPair(Party.ALICE);
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }
            this.keyPair = keyPair;
            try {
                Files.createDirectories(keyPairPath.getParent());
                Files.createFile(keyPairPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ObjectOutputStream oos;
            try {
                oos = new ObjectOutputStream(Files.newOutputStream(keyPairPath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                oos.writeObject(keyPair);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
