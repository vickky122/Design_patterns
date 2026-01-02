package service;

import model.FixedDeposit;
import model.Transaction;
import model.Wallet;
import model.enums.TransactionSource;
import model.enums.TransactionType;
import repository.WalletRepository;
import service.FixedDepositService;
import service.OfferService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WalletService {

    private final WalletRepository walletRepository;
    private final OfferService offerService;
    private final FixedDepositService fixedDepositService;
    private static final BigDecimal MIN_TRANSFER =
            new BigDecimal("0.0001").setScale(4, RoundingMode.HALF_UP);

    public WalletService(WalletRepository walletRepository,
                         OfferService offerService,
                         FixedDepositService fixedDepositService) {
        this.walletRepository = walletRepository;
        this.offerService = offerService;
        this.fixedDepositService = fixedDepositService;
    }

    public void createWallet(String name, String amountStr) {
        if (walletRepository.exists(name)) {
            System.out.println("Wallet already exists for " + name);
            return;
        }
        BigDecimal amount = parseAmount(amountStr);
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            System.out.println("Initial amount cannot be negative");
            return;
        }
        walletRepository.createWallet(name, amount);
    }

    public void transferMoney(String fromName, String toName, String amountStr) {
        if (fromName.equals(toName)) {
            System.out.println("Cannot transfer to same account");
            return;
        }

        Wallet from = walletRepository.getWallet(fromName);
        Wallet to = walletRepository.getWallet(toName);

        if (from == null || to == null) {
            System.out.println("One of the accounts does not exist");
            return;
        }

        BigDecimal amount = parseAmount(amountStr);

        if (amount.compareTo(MIN_TRANSFER) < 0) {
            System.out.println("Minimum transfer is " + MIN_TRANSFER);
            return;
        }
        if (from.getBalance().compareTo(amount) < 0) {
            System.out.println("Insufficient balance in " + fromName);
            return;
        }

        from.debit(amount);
        to.credit(amount);

        Transaction fromTx = new Transaction(
                toName,
                TransactionType.DEBIT,
                amount,
                TransactionSource.TRANSFER
        );
        Transaction toTx = new Transaction(
                fromName,
                TransactionType.CREDIT,
                amount,
                TransactionSource.TRANSFER
        );

        from.addTransaction(fromTx);
        to.addTransaction(toTx);
        from.incrementTransferCount();
        to.incrementTransferCount();

        fixedDepositService.onBalanceChange(from);
        fixedDepositService.onBalanceChange(to);

        offerService.applyOffer1IfEligible(from, to);
    }

    public void printStatement(String name) {
        Wallet wallet = walletRepository.getWallet(name);
        if (wallet == null) {
            System.out.println("Wallet does not exist for " + name);
            return;
        }

        for (Transaction tx : wallet.getTransactions()) {
            String typeStr = tx.getType() == TransactionType.CREDIT ? "credit" : "debit";
            String amountStr = strip(tx.getAmount());

            switch (tx.getSource()) {
                case TRANSFER:
                    System.out.println(tx.getCounterParty() + " " + typeStr + " " + amountStr);
                    break;
                case OFFER1:
                    System.out.println("Offer1 " + typeStr + " " + amountStr);
                    break;
                case OFFER2:
                    System.out.println("Offer2 " + typeStr + " " + amountStr);
                    break;
                case FD_INTEREST:
                    System.out.println("FDInterest " + typeStr + " " + amountStr);
                    break;
                case INITIAL:

                    break;
            }
        }

        FixedDeposit fd = wallet.getFixedDeposit();
        if (fd != null && fd.isActive()) {
            System.out.println("FD " + strip(fd.getFdAmount())
                    + " remainingTx " + fd.getRemainingTransactions());
        }
    }

    public void printOverview() {
        List<Wallet> wallets = new ArrayList<>(walletRepository.getAllWallets());
        wallets.sort(Comparator.comparingLong(Wallet::getCreatedAt));

        for (Wallet wallet : wallets) {
            StringBuilder sb = new StringBuilder();
            sb.append(wallet.getName())
                    .append(" ")
                    .append(strip(wallet.getBalance()));

            FixedDeposit fd = wallet.getFixedDeposit();
            if (fd != null && fd.isActive()) {
                sb.append(" FD:")
                        .append(strip(fd.getFdAmount()))
                        .append(" remainingTx:")
                        .append(fd.getRemainingTransactions());
            }

            System.out.println(sb);
        }
    }

    public void startFixedDeposit(String name, String amountStr) {
        Wallet wallet = walletRepository.getWallet(name);
        if (wallet == null) {
            System.out.println("Wallet does not exist for " + name);
            return;
        }
        BigDecimal fdAmount = parseAmount(amountStr);
        if (fdAmount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("FD amount must be positive");
            return;
        }
        fixedDepositService.startFixedDeposit(wallet, fdAmount);
    }

    private BigDecimal parseAmount(String amountStr) {
        BigDecimal value = new BigDecimal(amountStr);
        return value.setScale(4, RoundingMode.HALF_UP);
    }

    private String strip(BigDecimal value) {
        return value.stripTrailingZeros().toPlainString();
    }
}
