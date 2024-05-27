package com.shrishdeshpande.qe.api.transaction;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.shrishdeshpande.qe.util.IpcRepresentable;
import org.jetbrains.annotations.NotNull;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "transtype",
        defaultImpl = CryptoTransaction.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CryptoTransaction.class, name = "crypto"),
//        @JsonSubTypes.Type(value = NftTransaction.class, name = "nft")
})
public abstract class Transaction implements IpcRepresentable, Comparable<Transaction> {
    protected final String sender;

    protected final String recipient;

    protected final long timestamp;

    public Transaction(String sender, String recipient, long timestamp) {
        this.sender = sender;
        this.recipient = recipient;
        this.timestamp = timestamp;
    }

    public abstract Type getType();

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(@NotNull Transaction o) {
        return Long.compare(this.timestamp, o.timestamp);
    }

    public abstract String hash();

    public String readable() {
        return String.format("%s -> %s: %s", sender, recipient, timestamp);
    }

    public enum Type {
        CRYPTO,
        NFT
    }
}
