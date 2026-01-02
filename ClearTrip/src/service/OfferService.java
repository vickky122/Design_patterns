package service;

import model.Transaction;
import model.Wallet;
import model.enums.TransactionSource;
import model.enums.TransactionType;
import repository.WalletRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OfferService {
    private final WalletRepository walletRepository;
    public OfferService(WalletRepository walletRepository){
        this.walletRepository=walletRepository;
    }

    public void applyOffer1IfEligible(Wallet a, Wallet b){
        if(a.getBalance().compareTo(b.getBalance())!=0){
            return;
        }
        BigDecimal reward=new BigDecimal("10.0000");
        a.credit(reward);
        b.credit(reward);

        Transaction txA=new Transaction("Offer1", TransactionType.CREDIT,reward, TransactionSource.OFFER1);
        Transaction txB=new Transaction("Offer1", TransactionType.CREDIT,reward, TransactionSource.OFFER1);
        a.addTransaction(txA);
        b.addTransaction(txB);


    }

    public void applyOffer2(){
        List<Wallet> wallets=new ArrayList<>(walletRepository.getAllWallets());
        if(wallets.isEmpty()){
            return;
        }
        wallets.sort((w1,w2)->{
            if(w1.getTransferCount()!=w2.getTransferCount()){
                return Integer.compare(w2.getTransferCount(), w1.getTransferCount());
            }
            int balanceCmp=w2.getBalance().compareTo(w1.getBalance());
            if(balanceCmp!=0){
                return balanceCmp;
            }
            return Long.compare(w1.getCreatedAt(),w2.getCreatedAt());
        });
        BigDecimal[] rewards={new BigDecimal("10.000"),new BigDecimal("5.000"),new BigDecimal("2.000")};
        int limit=Math.min(3,wallets.size());
        for(int i=0;i<limit;i++){
            wallets.get(i).credit(rewards[i]);
            Transaction tx=new Transaction("Offer2", TransactionType.CREDIT,rewards[i], TransactionSource.OFFER2);
            wallets.get(i).addTransaction(tx);
            wallets.get(i).incrementTransferCount();


        }

    }
}
