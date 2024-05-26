package com.shrishdeshpande.qe.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shrishdeshpande.qe.api.transaction.Transaction;
import com.shrishdeshpande.qe.util.ChronoUtils;

import java.util.ArrayList;
import java.util.List;

public class Block {
    private final BlockHeader header;

    private final List<Transaction> transactions;

    @JsonCreator
    public Block(@JsonProperty("header") BlockHeader header, @JsonProperty("transactions") List<Transaction> transactions) {
        this.header = header;
        this.transactions = transactions;
    }

    public BlockHeader getHeader() {
        return header;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String readable() {
        String local = ChronoUtils.convertUnixMillisToLocalDateTime(timestamp());
        return String.format("%s : %s -> %s: %d", local, header.previousHash(), header.hash(), header.numTransactions());
    }

    public long timestamp() {
        return header.timestamp();
    }

    public static class Builder {
        private BlockHeader header;
        private List<Transaction> transactions = new ArrayList<>();

        public Builder header(BlockHeader header) {
            this.header = header;
            return this;
        }

        public Builder transactions(List<Transaction> transactions) {
            this.transactions = transactions;
            return this;
        }

        public Builder transaction(Transaction transaction) {
            transactions.add(transaction);
            return this;
        }

        public Block build() {
            if (transactions.size() != header.numTransactions()) {
                throw new IllegalStateException("Number of transactions does not match the number of transactions in the header");
            }

            return new Block(header, transactions);
        }
    }
}
