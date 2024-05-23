package com.shrishdeshpande.qe.blockchain.transaction;

public class CryptoTransaction extends Transaction {
    private final double amount;

    public CryptoTransaction(Type type, String sender, String recipient, double senderBalance, double recipientBalance, double amount) {
        super(type, sender, recipient, senderBalance, recipientBalance);
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}
