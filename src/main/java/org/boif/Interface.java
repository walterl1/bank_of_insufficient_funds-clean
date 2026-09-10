package org.boif;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class Interface {
    public static void main(String[] args) {

        BankService bankService = new BankService();
        final Logger logger = LoggerFactory.getLogger(BankService.class);


        System.out.println("Welcome to Bank of Insufficient Funds");

        try (Scanner scanner = new Scanner(System.in)) {
            String accountId = "";
            String pin = "";


            System.out.println("Press 1 for Registration. Press 2 if you have an existing account.");
            String signupLogin = scanner.nextLine();

            if(signupLogin.equals("1")){

                bankService.signupUser();
                logger.info("New account registration.");

            }


            while (true) {
                System.out.print("Enter your account ID: ");
                accountId = scanner.nextLine();
                System.out.print("Enter your PIN: ");
                pin = scanner.nextLine();

                 Account loggedIn = bankService.validateAccount(accountId, pin);

                 if(loggedIn == null){

                     System.out.println("Invalid credentials. Please try again.");
                     logger.info("Account with accountId {} login attempt failed.", loggedIn.getAccountId());

                 } else {

                     System.out.println("\nLog in attempt successful.");
                     System.out.println("\nWelcome.");
                     logger.info("Account with accountId {} login attempt successful.", loggedIn.getAccountId());


                     break;
                 }

            }


                System.out.println();

            boolean on = true;
            while(on) {
                System.out.println("Actions:\n");
                System.out.println("1. Deposit");
                System.out.println("2. Withdraw");
                System.out.println("3. Transfer");
                System.out.println("4. Check Balance");
                System.out.println("5. Exit\n");


                System.out.print("Please select an action (1, 2, 3, 4, 5): \n");

                int action = scanner.nextInt();
                scanner.nextLine();
                Account account1 = bankService.getLoggedInAccount(accountId, pin);

                if(account1.getBalance() < 0){
                    System.out.println("Please note that you account is negative and needs to " +
                            "be taken care soon.  ");

                }


                switch (action) {

                    case 1: {
                        System.out.println("\nYou total balance is: " + account1.getBalance());
                        System.out.println("Please enter the amount to Deposit:  ");
                        String deposit = scanner.nextLine();
                        double depositAmount = Double.parseDouble(deposit);
                        bankService.depositfunds(account1, depositAmount);
                        break;


                    }
                    case 2: {
                        System.out.println("\nYou total balance is: " + bankService.getBalance(account1));
                        System.out.println("Please enter the amount to Withdraw:  ");
                        String withdraw = scanner.nextLine();
                        double wd = Double.parseDouble(withdraw);
                        bankService.withdraw(account1, wd);
                        break;
                    }
                    case 3: {
                        System.out.println("\nYou total balance is: " + bankService.getBalance(account1));
                        System.out.println("\nPlease enter the amount to Transfer:  ");
                        String transfer = scanner.nextLine();
                        double transferAmount = Double.parseDouble(transfer);
                        System.out.println("\nPlease enter the account Id you wish to make a transfer to:  ");
                        String accounId = scanner.nextLine();
                        bankService.transferBetweenAccounts(account1, accounId, transferAmount);
                        break;
                    }
                    case 4: {
                        System.out.println("\nYour total balance is: " + bankService.getBalance(account1));
                        break;
                    }
                    case 5: {
                        System.out.println("\nThank you for using Bank of Insufficient Funds.");
                        on = false;
                        break;
                    }
                    default: {
                        System.out.println();
                        System.out.println("Incorrect Input");
                        System.out.println("Please select again only use 1,2,3,4 or 5\n");
                    }
                }

            }
        }
    }
}
