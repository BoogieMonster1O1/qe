package com.shrishdeshpande.qe.api.transaction;

import org.apache.commons.codec.digest.DigestUtils;

public class CryptoTransaction extends Transaction {
    private final double amount;

    public CryptoTransaction(String sender, String recipient, long timestamp, double amount) {
        super(Type.CRYPTO, sender, recipient, timestamp);
        this.amount = amount;
    }

    public CryptoTransaction(String sender, String recipient, double amount) {
        this(sender, recipient, System.currentTimeMillis(), amount);
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public int hashCode() {
        return sender.hashCode() + recipient.hashCode() + (int) timestamp + (int) (amount * 100);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Transaction rhs = (Transaction) obj;
        return sender.equals(rhs.sender) && recipient.equals(rhs.recipient) && timestamp == rhs.timestamp;
    }

    public String hash() {
        return DigestUtils.sha256Hex(String.format("%s%s%s%d", sender, recipient, amount, timestamp));
    }
}
