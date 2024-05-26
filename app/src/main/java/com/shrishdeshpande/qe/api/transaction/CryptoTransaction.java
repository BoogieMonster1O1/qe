package com.shrishdeshpande.qe.api.transaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shrishdeshpande.qe.util.ChronoUtils;
import org.apache.commons.codec.digest.DigestUtils;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class CryptoTransaction extends Transaction {
    private final double amount;

    @JsonCreator
    public CryptoTransaction(@JsonProperty("sender") String sender, @JsonProperty("recipient") String recipient, @JsonProperty("timestamp") long timestamp, @JsonProperty("amount") double amount) {
        super(sender, recipient, timestamp);
        this.amount = amount;
    }

    public CryptoTransaction(String sender, String recipient, double amount) {
        this(sender, recipient, System.currentTimeMillis(), amount);
    }

    @Override
    public Type getType() {
        return Type.CRYPTO;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String readable() {
        String local = ChronoUtils.convertUnixMillisToLocalDateTime(timestamp);
        return String.format("%s : %s -> %s: %f", local, sender, recipient, amount);
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
