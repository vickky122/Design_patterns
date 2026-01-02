package model;

import model.enums.TransactionSource;
import model.enums.TransactionType;

import java.math.BigDecimal;

public class Transaction {
    private final String counterParty;
    private final BigDecimal amount;
    private final TransactionType type;
    private final TransactionSource source;
    private final long timestamp;

    public Transaction(String counterParty, TransactionType type,BigDecimal amount, TransactionSource source) {
        this.counterParty = counterParty;
        this.amount = amount;
        this.type = type;
        this.source = source;
        this.timestamp = System.currentTimeMillis();
    }
    public String getCounterParty(){
        return counterParty;
    }
    public BigDecimal getAmount(){
        return amount;
    }
    public TransactionType getType(){
        return type;
    }
    public TransactionSource getSource(){
        return source;
    }
    public long getTimestamp(){
        return timestamp;
    }
}
