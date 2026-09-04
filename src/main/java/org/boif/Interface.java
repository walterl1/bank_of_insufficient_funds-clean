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

            boolean on = true;
            while(on) {
                System.out.println("Actions:");
                System.out.println("1. Deposit");
                System.out.println("2. Withdraw");
                System.out.println("3. Transfer");
                System.out.println("4. Check Balance");
                System.out.println("5. Exit");


                System.out.print("Please select an action (1, 2, 3, 4, 5): ");

                int action = scanner.nextInt();
                scanner.nextLine();

                switch (action) {
                    case 1: {
                        System.out.println("You total balance is: " + bankService.getBalance(accountId));
                        System.out.println("Please enter the amount to Deposit:  ");
                        String deposit = scanner.nextLine();

                        double dp = Double.parseDouble(deposit);
                        double newbalance= bankService.depositfunds(accountId,dp);
                        if(newbalance!=-1){
                            System.out.println("Deposit Successful. New Balance ="+ newbalance);
                        }else{
                            System.out.println("Deposit Unsuccessful. Please try again");
                        }
                        break;
                    }
                    case 2: {
                        System.out.println("You total balance is: " + bankService.getBalance(accountId));
                        System.out.println("Please enter the amount to Withdraw:  ");
                        String withdraw = scanner.nextLine(); 

                        double wd = Double.parseDouble(withdraw);
                        double newbalance = bankService.withdraw(accountId, wd);
                        if(newbalance!=-1){
                            System.out.println("Withdrawal succesful. Current balance = " + newbalance);
                        } else{
                            System.out.println("Insufficient funds");
                        }
                        break;
                    }
                    case 3: {
                        System.out.println("You total balance is: " + bankService.getBalance(accountId));
                        System.out.println("Please enter the amount to Transfer:  ");
                        String transfer = scanner.nextLine(); break;
                    }
                    case 4: {
                        System.out.println("Your total balance is: " + bankService.getBalance(accountId));
                        break;
                    }
                    case 5: {
                        System.out.println("Thank you for using Bank of Insufficient Funds.");
                        on = false;
                        break;
                    }
                    default: {
                        System.out.println();
                        System.out.println("Incorrect Input");
                    }
                }


            }
        }
    }
}
