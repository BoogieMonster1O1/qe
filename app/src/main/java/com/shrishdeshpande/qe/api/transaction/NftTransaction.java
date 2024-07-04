package com.shrishdeshpande.qe.api.transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NftTransaction extends Transaction {
    private final String indivisibleId;
    private final int qkdNonce;

    @JsonCreator
    public NftTransaction(@JsonProperty("sender") String sender, @JsonProperty("recipient") String recipient, @JsonProperty("timestamp") long timestamp, @JsonProperty("indivisibleId") String indivisibleId, @JsonProperty("qkdNonce") int qkdNonce) {
        super(sender, recipient, timestamp);
        this.indivisibleId = indivisibleId;
        this.qkdNonce = qkdNonce;
    }

    @Override
    public Type getType() {
        return Type.NFT_SALE;
    }

    @Override
    public String hash() {
        return "%s%s%s%d%d".formatted(sender, recipient, indivisibleId, qkdNonce, timestamp);
    }

    @Override
    public String readable() {
        return "Sold %s from %s to %s at %s".formatted(indivisibleId, sender, recipient, timestamp);
    }

    public String getIndivisibleId() {
        return indivisibleId;
    }
}
