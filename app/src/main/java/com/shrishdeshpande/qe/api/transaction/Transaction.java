package com.shrishdeshpande.qe.api.transaction;

public abstract class Transaction {

    protected final Type type;

    protected final String sender;

    protected final String recipient;

    protected final double senderBalance;

    protected final double recipientBalance;

    public Transaction(Type type, String sender, String recipient, double senderBalance, double recipientBalance) {
        this.type = type;
        this.sender = sender;
        this.recipient = recipient;
        this.senderBalance = senderBalance;
        this.recipientBalance = recipientBalance;
    }

    public Type getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public double getSenderBalance() {
        return senderBalance;
    }

    public double getRecipientBalance() {
        return recipientBalance;
    }

    public enum Type {
        CRYPTO,
        NFT
    }
}
