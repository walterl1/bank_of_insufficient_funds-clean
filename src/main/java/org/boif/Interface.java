package org.boif;

import java.util.Scanner;

public class Interface {
    public static void main(String[] args) {
        System.out.println("Welcome to Bank of Insufficient Funds");

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter your account ID: ");
            String accountId = scanner.nextLine();
            System.out.print("Enter your PIN: ");
            String pin = scanner.nextLine();
        }
    }
}
