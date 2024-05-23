package com.shrishdeshpande.qe.api;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * @param hash            The hash of the block
 * @param previousHash    The hash of the previous block
 * @param timestamp       The timestamp of the block, represented as milliseconds since the Unix epoch
 * @param nonce           The nonce of the block
 * @param difficulty      The difficulty of the block
 * @param merkleRoot      The merkle root of the block
 * @param numTransactions The number of transactions in the block
 */
public record BlockHeader(String hash, String previousHash, long timestamp, long nonce, int difficulty,
                          String merkleRoot, int numTransactions) {

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
