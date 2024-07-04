package com.shrishdeshpande.qe.api.transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shrishdeshpande.qe.util.ChronoUtils;
import org.apache.commons.codec.digest.DigestUtils;

public class MintTransaction extends Transaction {
    private final String indivisibleId;

    @JsonCreator
    public MintTransaction(@JsonProperty("recipient") String minter, @JsonProperty("timestamp") long timestamp, @JsonProperty("indivisibleId") String indivisibleId) {
        super("MINT", minter, timestamp);
        this.indivisibleId = indivisibleId;
    }

    public MintTransaction(String minter, String indivisibleId) {
        this(minter, System.currentTimeMillis(), indivisibleId);
    }

    @Override
    public Type getType() {
        return Type.NFT_MINT;
    }

    public String getIndivisibleId() {
        return indivisibleId;
    }

    public String hash() {
        return DigestUtils.sha256Hex(String.format("%s%s%s%d", sender, recipient, indivisibleId, timestamp));
    }

    @Override
    public String readable() {
        return String.format("Minted %s to %s at %s", indivisibleId, recipient, timestamp);
    }
}
