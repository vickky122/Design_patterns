package service;

import model.FixedDeposit;
import model.Transaction;
import model.Wallet;
import model.enums.TransactionSource;
import model.enums.TransactionType;

import java.math.BigDecimal;

public class FixedDepositService {
    private static final int REQUIRED_TRANSACTION_COUNT=5;
    private static final BigDecimal INTEREST=new BigDecimal("10.0000");
    public void startFixedDeposit(Wallet wallet,BigDecimal fdAmount){
        FixedDeposit current=wallet.getFixedDeposit();
        if(current!=null && current.isActive()){
            throw new IllegalArgumentException("Fixed deposit already exists for wallet " + wallet.getName());
        }

        FixedDeposit fd=new FixedDeposit(fdAmount,REQUIRED_TRANSACTION_COUNT,true);
        wallet.setFixedDeposit(fd);
    }

    public void onBalanceChange(Wallet wallet){
        FixedDeposit fd=wallet.getFixedDeposit();
        if(fd==null || !fd.isActive()){
            return;
        }
        if(wallet.getBalance().compareTo(fd.getFdAmount())<0){
            fd.deactivate();
            return;
        }
        fd.decrement();

        if(fd.getRemainingTransactions()<=0){
            wallet.credit(INTEREST);
            Transaction tx=new Transaction(
                    "FDInterest", TransactionType.CREDIT,INTEREST, TransactionSource.FD_INTEREST
            );
            wallet.addTransaction(tx);
            fd.deactivate();

        }
    }
}
