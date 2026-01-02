package ui;


import command.CommandProcessor;
import repository.InMemoryWalletRepository;
import repository.WalletRepository;
import service.FixedDepositService;
import service.OfferService;
import service.WalletService;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        WalletRepository walletRepository = new InMemoryWalletRepository();
        OfferService offerService = new OfferService(walletRepository);
        FixedDepositService fixedDepositService = new FixedDepositService();
        WalletService walletService = new WalletService(walletRepository, offerService, fixedDepositService);
        CommandProcessor processor = new CommandProcessor(walletService, offerService);

        Scanner sc = new Scanner(System.in);
        System.out.println("Digital wallet system");

        while (true) {
            printMenu();
            System.out.print("\nEnter your choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": {
                    System.out.print("ENter user name: ");
                    String name = sc.nextLine().trim();
                    System.out.print("Enter initial amount: ");
                    String amountStr = sc.nextLine().trim();
                    processor.process("CreateWallet " + name + " " + amountStr);
                    break;
                }
                case "2": {
                    System.out.print("Send from: ");
                    String from = sc.nextLine().trim();
                    System.out.print("Send To: ");
                    String to = sc.nextLine().trim();

                    System.out.print("Enter amount: ");
                    String amt = sc.nextLine().trim();
                    processor.process("TransferMoney " + from + " " + to + " " + amt);
                    break;
                }
                case "3": {
                    System.out.print("Enter user name: ");
                    String name = sc.nextLine().trim();
                    processor.process("Statement " + name);
                    break;
                }
                case "4": {
                    processor.process("Overview");
                    break;
                }
                case "5": {
                    System.out.println("Running Offer2");
                    processor.process("Offer2");
                    break;
                }
                case "6": {
                    System.out.print("Enter user name: ");
                    String name = sc.nextLine().trim();
                    System.out.print("Enter FD amount: ");
                    String amountStr = sc.nextLine().trim();
                    processor.process("FixedDeposit " + name + " " + amountStr);
                    break;
                }
                case "0": {
                    System.out.println("Thank you for using our service");
                    return;
                }
                default: {
                    System.out.println("Invalid choice...try again");

                }
                System.out.println();
            }

        }


//        List<String> commands = List.of(
//                "CreateWallet Harry 100",
//                "CreateWallet Ron 95.7",
//                "CreateWallet Hermione 104",
//                "CreateWallet Albus 200",
//                "CreateWallet Draco 500",
//                "Overview",
//                "TransferMoney Albus Draco 30",
//                "TransferMoney Hermione Harry 2",
//                "TransferMoney Albus Ron 5",
//                "Overview",
//                "Statement Harry",
//                "Statement Albus",
//                "Offer2",
//                "Overview",
//
//                "FixedDeposit Harry 100",
//                "TransferMoney Harry Ron 1",
//                "TransferMoney Harry Ron 1",
//                "TransferMoney Harry Ron 1",
//                "TransferMoney Harry Ron 1",
//                "TransferMoney Harry Ron 1",
//                "Statement Harry",
//                "Overview"
//        );

//        for (String cmd : commands) {
//            System.out.println(cmd);
//            processor.process(cmd);
//            System.out.println();
//        }
    }

    private static void printMenu() {
        System.out.println("\nMenu");
        System.out.println("1. Create Wallet");
        System.out.println("2. Transfer Money");
        System.out.println("3. View Statement");
        System.out.println("4. Print Overview");
        System.out.println("5. Run Offer2");
        System.out.println("6. Start Fixed Deposit");
        System.out.println("0. Exit");
    }
}
