package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Wallet {
    private final String name;
    private BigDecimal balance;
    private final long createdAt;
    private  final List<Transaction> transactions;
    private int transferCount;
    private FixedDeposit fixedDeposit;

    public Wallet(String name, BigDecimal initialBalance){
        this.name=name;
        this.balance=initialBalance;
        this.createdAt=System.currentTimeMillis();
        this.transactions=new ArrayList<>();
        this.transferCount=0;

    }

    public String getName(){
        return name;
    }
    public BigDecimal getBalance(){
        return balance;
    }
    public long getCreatedAt(){
        return createdAt;
    }
    public List<Transaction> getTransactions(){
        return transactions;
    }
    public int getTransferCount(){
        return transferCount;
    }
    public FixedDeposit getFixedDeposit(){
        return fixedDeposit;
    }
    public void setFixedDeposit(FixedDeposit fixedDeposit){
        this.fixedDeposit=fixedDeposit;
    }
    public void incrementTransferCount(){
        transferCount++;
    }

    public void credit(BigDecimal amount){
        balance=balance.add(amount);
    }
    public void debit(BigDecimal amount){
        balance=balance.subtract(amount);
    }

    public void addTransaction( Transaction tx){
        transactions.add(tx);
    }
}
