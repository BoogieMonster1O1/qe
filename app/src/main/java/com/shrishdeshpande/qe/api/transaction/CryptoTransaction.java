package com.shrishdeshpande.qe.api.transaction;

import org.apache.commons.codec.digest.DigestUtils;

public class CryptoTransaction extends Transaction {
    private final double amount;

    private String signature;

    public CryptoTransaction(Type type, String sender, String recipient, double senderBalance, double recipientBalance, double amount) {
        super(type, sender, recipient, senderBalance, recipientBalance);
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public String hash() {
        return DigestUtils.sha256Hex(String.format("%s%s%s%s%s", sender, recipient, senderBalance, recipientBalance, amount));
    }
}
