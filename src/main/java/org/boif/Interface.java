package org.boif;

import java.util.Scanner;

public class Interface {
    public static void main(String[] args) {
        System.out.println("Welcome to Bank of Insufficient Funds");

        try (Scanner scanner = new Scanner(System.in)) {
            String accountId = "";
            String pin = "";

            while (true) {
                System.out.print("Enter your account ID: ");
                accountId = scanner.nextLine();
                System.out.print("Enter your PIN: ");
                pin = scanner.nextLine();

                if (BankService.validate(accountId, pin))
                    break;

                System.out.println("Invalid credentials. Please try again.");
            }
        }
    }
}
