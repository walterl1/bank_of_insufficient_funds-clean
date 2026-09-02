package org.boif;

import java.util.Scanner;

public class Interface {
    public static void main(String[] args) {
        BankService bankService = new BankService();
        System.out.println("Welcome to Bank of Insufficient Funds");

        try (Scanner scanner = new Scanner(System.in)) {
            String accountId = "";
            String pin = "";

            while (true) {
                System.out.print("Enter your account ID: ");
                accountId = scanner.nextLine();
                System.out.print("Enter your PIN: ");
                pin = scanner.nextLine();

                if (bankService.validate(accountId, pin))
                    break;

                System.out.println("Invalid credentials. Please try again.");
            }

            System.out.println();

            System.out.println("Actions:");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Transfer");

            System.out.print("Please select an action (1, 2, 3): ");

            String action = scanner.nextLine();
        }
    }
}
