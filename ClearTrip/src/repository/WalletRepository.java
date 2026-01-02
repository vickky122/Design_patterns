package repository;

import model.Wallet;

import java.math.BigDecimal;
import java.util.Collection;

public interface WalletRepository {
    boolean exists(String name);
    Wallet createWallet(String name, BigDecimal initialBalance);
    Wallet getWallet(String name);
    Collection<Wallet> getAllWallets();
}
