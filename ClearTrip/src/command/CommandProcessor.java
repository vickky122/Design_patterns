package command;

import service.OfferService;
import service.WalletService;

public class CommandProcessor {

    private final WalletService walletService;
    private final OfferService offerService;

    public CommandProcessor(WalletService walletService, OfferService offerService) {
        this.walletService = walletService;
        this.offerService = offerService;
    }

    public void process(String line) {
        if (line == null || line.trim().isEmpty()) {
            return;
        }

        String[] parts = line.trim().split("\\s+");
        String command = parts[0];

        switch (command) {
            case "CreateWallet":
                if (parts.length != 3) {
                    System.out.println("Invalid CreateWallet command");
                    return;
                }
                walletService.createWallet(parts[1], parts[2]);
                break;

            case "TransferMoney":
                if (parts.length != 4) {
                    System.out.println("Invalid TransferMoney command");
                    return;
                }
                walletService.transferMoney(parts[1], parts[2], parts[3]);
                break;

            case "Statement":
                if (parts.length != 2) {
                    System.out.println("Invalid Statement command");
                    return;
                }
                walletService.printStatement(parts[1]);
                break;

            case "Overview":
                walletService.printOverview();
                break;

            case "Offer2":
                offerService.applyOffer2();
                break;

            case "FixedDeposit":
                if (parts.length != 3) {
                    System.out.println("Invalid FixedDeposit command");
                    return;
                }
                walletService.startFixedDeposit(parts[1], parts[2]);
                break;

            default:
                System.out.println("Unknown command: " + command);
        }
    }
}
