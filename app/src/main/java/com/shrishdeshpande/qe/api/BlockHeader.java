package com.shrishdeshpande.qe.api;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * @param hash            The hash of the block
 * @param previousHash    The hash of the previous block
 * @param timestamp       The timestamp of the block, represented as milliseconds since the Unix epoch
 * @param nonce           The nonce of the block
 * @param merkleRoot      The merkle root of the block
 * @param numTransactions The number of blocks in the block
 */
public record BlockHeader(String hash, String previousHash, long timestamp, long nonce,
                          String merkleRoot, int numTransactions, String miner) {

    public void writeToStream(DataOutputStream stream) {
        try {
            stream.writeUTF(hash);
            stream.writeUTF(previousHash);
            stream.writeLong(timestamp);
            stream.writeLong(nonce);
            stream.writeUTF(merkleRoot);
            stream.writeInt(numTransactions);
            stream.writeUTF(miner);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] hash(String previousHash, long nonce, String merkleRoot) {
        return DigestUtils.sha256(String.format("%s%d%s", previousHash, nonce, merkleRoot));
    }

    public static String hashHex(String previousHash, long nonce, String merkleRoot) {
        return DigestUtils.sha256Hex(String.format("%s%d%s", previousHash, nonce, merkleRoot));
    }

    public static BlockHeader fromDataInputStream(DataInputStream stream) {
        try {
            String hash = stream.readUTF();
            String previousHash = stream.readUTF();
            long timestamp = stream.readLong();
            long nonce = stream.readLong();
            String merkleRoot = stream.readUTF();
            int numTransactions = stream.readInt();
            String miner = stream.readUTF();

            return new BlockHeader.Builder()
                    .setHash(hash)
                    .setPreviousHash(previousHash)
                    .setTimestamp(timestamp)
                    .setNonce(nonce)
                    .setMerkleRoot(merkleRoot)
                    .setNumTransactions(numTransactions)
                    .setMiner(miner)
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
        private String merkleRoot;
        private int numTransactions;
        private String miner;

        public Builder setHash(String hash) {
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

        public Builder setMerkleRoot(String merkleRoot) {
            this.merkleRoot = merkleRoot;
            return this;
        }

        public Builder setNumTransactions(int numTransactions) {
            this.numTransactions = numTransactions;
            return this;
        }

        public Builder setMiner(String miner) {
            this.miner = miner;
            return this;
        }

        public BlockHeader build() {
            return new BlockHeader(hash, previousHash, timestamp, nonce, merkleRoot, numTransactions, miner);
        }
    }
}
