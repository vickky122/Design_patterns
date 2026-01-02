package repository;

import model.Transaction;
import model.Wallet;
import model.enums.TransactionSource;
import model.enums.TransactionType;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryWalletRepository implements WalletRepository{
    private final Map<String, Wallet> store=new ConcurrentHashMap<>();

    @Override
    public boolean exists(String name){
        return store.containsKey(name);
    }

    @Override
    public Wallet createWallet(String name, BigDecimal initialBalance){
        if(exists(name)){
            throw new IllegalArgumentException("Wallet with name " + name + " already exists");
        }
        Wallet wallet=new Wallet(name,initialBalance);
        store.put(name,wallet);
        Transaction initial=new Transaction(
                "INITIAL", TransactionType.CREDIT,initialBalance, TransactionSource.INITIAL
        );
        wallet.addTransaction(initial);
        return wallet;
    }
    @Override
    public Wallet getWallet(String name){
        if(!exists(name)){
            throw new IllegalArgumentException("Wallet with name " + name + " does not exist");
        }
        return store.get(name);
    }
    @Override
    public Collection<Wallet> getAllWallets(){
        return store.values();
    }
}
