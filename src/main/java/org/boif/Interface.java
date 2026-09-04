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

                if (bankService.validateAccount(accountId, pin))
                    break;

                System.out.println("Invalid credentials. Please try again.");
            }


                System.out.println();
            while(true) {
                System.out.println("Actions:");
                System.out.println("1. Deposit");
                System.out.println("2. Withdraw");
                System.out.println("3. Transfer");
                System.out.println("4. Check Balance");
                System.out.println("5. Exit");


                System.out.print("Please select an action (1, 2, 3, 4, 5): ");

                String action = scanner.nextLine();

                if (action.equals("1")) {
                    System.out.println("You total balance is: " + bankService.getBalance(accountId));
                    System.out.println("Please enter the amount to Deposit:  ");
                    String deposit = scanner.nextLine(); break;
                    

                } else if (action.equals("2")) {
                    System.out.println("You total balance is: " + bankService.getBalance(accountId));
                    System.out.println("Please enter the amount to Withdraw:  ");
                    String withdraw = scanner.nextLine(); 
                    if (bankService.withdraw(accountId, Double.parseDouble(withdraw))){
                        System.out.println("Insufficient funds");
                    } else{
                        System.out.println("Withdrawal succesful. Current balance =" + bankService.getBalance(accountId));
                    }
                } else if (action.equals("3")) {
                    System.out.println("You total balance is: " + bankService.getBalance(accountId));
                    System.out.println("Please enter the amount to Transfer:  ");
                    String transfer = scanner.nextLine(); break;
                } else if (action.equals("4")) {
                    System.out.println("Your total balance is: " + bankService.getBalance(accountId));
                } else if (action.equals("5")) {
                    System.out.println("Thank you for using Bank of Insufficient Funds.");
                    break;
                } else {
                    System.out.println();
                    System.out.println("Incorrect Input");


                }

            }
        }
    }
}
