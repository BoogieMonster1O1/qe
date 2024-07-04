package com.shrishdeshpande.qe.api.transaction;

public class ContractTransaction extends CryptoTransaction {
    public ContractTransaction(String sender, String recipient, double amount) {
        super(sender, recipient, amount);
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
