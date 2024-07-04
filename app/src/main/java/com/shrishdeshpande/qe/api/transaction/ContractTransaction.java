package com.shrishdeshpande.qe.api.transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ContractTransaction extends CryptoTransaction {
    @JsonCreator
    public ContractTransaction(@JsonProperty("sender") String sender, @JsonProperty("recipient") String recipient, @JsonProperty("timestamp") long timestamp, @JsonProperty("amount") double amount) {
        super(sender, recipient, timestamp, amount);
    }

    public ContractTransaction(String sender, String recipient, double amount) {
        this(sender, recipient, System.currentTimeMillis(), amount);
    }

    @Override
    public Type getType() {
        return Type.CONTRACT;
    }

    @Override
    public String readable() {
        return super.readable() + " (contract)";
    }
}
