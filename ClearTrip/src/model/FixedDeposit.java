package model;

import java.math.BigDecimal;

public class FixedDeposit {
    private final BigDecimal fdAmount;
    private int remainingTransactions;
    private boolean active;

    public FixedDeposit(BigDecimal fdAmount, int requiredTransactions, boolean active) {
        this.fdAmount = fdAmount;
        this.remainingTransactions = requiredTransactions;
        this.active = active;
    }

    public BigDecimal getFdAmount() {
        return fdAmount;
    }

    public int getRemainingTransactions() {
        return remainingTransactions;
    }

    public boolean isActive() {
        return active;
    }
    public void decrement(){
        remainingTransactions--;
    }
    public void deactivate(){
        active = false;
    }
}
