package com.shrishdeshpande.qe.blockchain;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class BlockHeader {
    /**
     * The hash of the block
     */
    private final String hash;

    /**
     * The hash of the previous block
     */
    private final String previousHash;

    /**
     * The timestamp of the block, represented as milliseconds since the Unix epoch
     */
    private final long timestamp;

    /**
     * The nonce of the block
     */
    private final long nonce;

    /**
     * The difficulty of the block
     */
    private final int difficulty;

    /**
     * The merkle root of the block
     */
    private final String merkleRoot;

    /**
     * The number of transactions in the block
     */
    private final int numTransactions;


    public BlockHeader(String hash, String previousHash, long timestamp, long nonce, int difficulty, String merkleRoot, int numTransactions) {
        this.hash = hash;
        this.previousHash = previousHash;
        this.timestamp = timestamp;
        this.nonce = nonce;
        this.difficulty = difficulty;
        this.merkleRoot = merkleRoot;
        this.numTransactions = numTransactions;
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getNonce() {
        return nonce;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public int getNumTransactions() {
        return numTransactions;
    }

    public void writeToStream(DataOutputStream stream) {
        try {
            stream.writeUTF(hash);
            stream.writeUTF(previousHash);
            stream.writeLong(timestamp);
            stream.writeLong(nonce);
            stream.writeInt(difficulty);
            stream.writeUTF(merkleRoot);
            stream.writeInt(numTransactions);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static BlockHeader fromDataInputStream(DataInputStream stream) {
        try {
            String hash = stream.readUTF();
            String previousHash = stream.readUTF();
            long timestamp = stream.readLong();
            long nonce = stream.readLong();
            int difficulty = stream.readInt();
            String merkleRoot = stream.readUTF();
            int numTransactions = stream.readInt();

            return new BlockHeader.Builder()
                .setHash(hash)
                .setPreviousHash(previousHash)
                .setTimestamp(timestamp)
                .setNonce(nonce)
                .setDifficulty(difficulty)
                .setMerkleRoot(merkleRoot)
                .setNumTransactions(numTransactions)
                .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class Builder {
        private String hash;
        private String previousHash;
        private long timestamp;
        private long nonce;
        private int difficulty;
        private String merkleRoot;
        private int numTransactions;

        Builder setHash(String hash) {
            this.hash = hash;
            return this;
        }

        public Builder setPreviousHash(String previousHash) {
            this.previousHash = previousHash;
            return this;
        }

        public Builder setTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder setNonce(long nonce) {
            this.nonce = nonce;
            return this;
        }

        public Builder setDifficulty(int difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        public Builder setMerkleRoot(String merkleRoot) {
            this.merkleRoot = merkleRoot;
            return this;
        }

        public Builder setNumTransactions(int numTransactions) {
            this.numTransactions = numTransactions;
            return this;
        }

        public BlockHeader build() {
            return new BlockHeader(hash, previousHash, timestamp, nonce, difficulty, merkleRoot, numTransactions);
        }
    }
}
