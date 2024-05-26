package com.shrishdeshpande.qe.api.transaction;

public abstract class Transaction {

    protected final Type type;

    protected final String sender;

    protected final String recipient;

    public Transaction(Type type, String sender, String recipient) {
        this.type = type;
        this.sender = sender;
        this.recipient = recipient;
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

    public abstract String hash();

    public enum Type {
        CRYPTO,
        NFT
    }
}
