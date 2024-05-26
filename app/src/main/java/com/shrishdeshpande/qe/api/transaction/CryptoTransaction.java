package com.shrishdeshpande.qe.api.transaction;

import org.apache.commons.codec.digest.DigestUtils;

public class CryptoTransaction extends Transaction {
    private final double amount;

    private String signature;

    public CryptoTransaction(Type type, String sender, String recipient, long timestamp, double amount) {
        super(type, sender, recipient, timestamp);
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public String hash() {
        return DigestUtils.sha256Hex(String.format("%s%s%s%s%d", signature, sender, recipient, amount, timestamp));
    }
}
