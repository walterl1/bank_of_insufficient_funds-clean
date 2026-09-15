package org.boif;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Scanner;

public class Interface {
    public static void main(String[] args) {

        BankService bankService = new BankService();
        final Logger logger = LoggerFactory.getLogger(BankService.class);


        System.out.println("\nWelcome to Bank of Insufficient Funds");

        try (Scanner scanner = new Scanner(System.in)) {
            String accountId = "";
            String pin = "";
            String signupOrLogin = "";

            while (!signupOrLogin.equals("1") && !signupOrLogin.equals("2")) {
                System.out.print("Enter 1 for Registration or 2 for Login: ");
                signupOrLogin = scanner.nextLine().trim();

                if(signupOrLogin.equals("1")){
                    System.out.println("\nThank you for choosing Bank of Insufficient Funds. Please enter an" +
                            " account Id to use for logging in.");
                    accountId = scanner.nextLine();
                    System.out.println("\nPlease enter a 6-digit pin number that you will remember.");
                    pin = scanner.nextLine();

                    String result = bankService.signupUser(accountId, pin);
                    System.out.println("\n" + result);

                    signupOrLogin = "";
                } else if (signupOrLogin.equals("2")) {
                    break;
                }
            }



            while (true) {
                System.out.print("Enter your account ID: ");
                accountId = scanner.nextLine().trim();
                System.out.print("Enter your PIN: ");
                pin = scanner.nextLine().trim();

                System.out.println();

                 Account loggedIn = bankService.validateAccount(accountId, pin);

                 if(loggedIn == null){

                     System.out.println("Invalid credentials. Press enter to try again or press 1 for registration.");
                     signupOrLogin = scanner.nextLine();

                     if(signupOrLogin.equals("1")) {
                         System.out.println("Thank you for choosing Bank of Insufficient Funds. Please enter an" +
                                 " account Id to use for logging in.");
                         accountId = scanner.nextLine();
                         System.out.println("\nPlease enter a 6-digit pin number that you will remember.");
                         pin = scanner.nextLine();
                         signupOrLogin = "";
                         String result = bankService.signupUser(accountId, pin);
                         System.out.println(result);


                     }

                 } else {

                     System.out.println("\nLog in attempt successful.");
                     System.out.println("\nWelcome!");


                     break;
                 }

            }


            boolean on = true;
            while(on) {
                System.out.println();
                System.out.println("Actions:\n");
                System.out.println("1. Deposit");
                System.out.println("2. Withdraw");
                System.out.println("3. Transfer");
                System.out.println("4. Check Balance");
                System.out.println("5. View Transactions History");
                System.out.println("6. Exit\n");


                System.out.print("Please select an action (1, 2, 3, 4, 5, 6): ");
            int action=0;
            try {
                String ac = scanner.nextLine();
                action = Integer.parseInt(ac);
            } catch (NumberFormatException exception) {
                System.out.println("\nPlease enter only a number from 1 to 6");
                continue;
            }

                System.out.println();

                Account account1 = bankService.getLoggedInAccount(accountId, pin);

                if(account1.getBalance() < 0){
                    System.out.println("Please note that you account is negative and needs to " +
                            "be taken care soon.  ");

                }


                switch (action) {

                    case 1: {
                        while(true) {
                            try {
<<<<<<< HEAD
                                System.out.println("\nYou total balance is: " + account1.getBalance());
                                System.out.println("Please enter the amount to Deposit (or type 'back' to go back):  ");
=======
                                System.out.println("Your total balance is: " + account1.getBalance());
                                System.out.print("Please enter the amount to deposit: ");
>>>>>>> origin/master
                                String deposit = scanner.nextLine();
                                if(deposit.equalsIgnoreCase("back")){
                                    break;
                                }
                                double depositAmount = Double.parseDouble(deposit);
                                String depositMessage = bankService.depositfunds(account1, depositAmount);
                                System.out.println(depositMessage);
                                if (depositMessage.contains("successful.")) {
                                    break;
                                }
                            }catch (NumberFormatException exception){
                                System.out.println("Please enter numbers only for deposit. Example: 100");
                            }
                        }
                        break;
                    }
                    case 2: {
                        while(true) {
                            try {
<<<<<<< HEAD
                                System.out.println("\nYou total balance is: " + bankService.getBalance(account1));
                                System.out.println("Please enter the amount to Withdraw (or type 'back' to go back):  ");
=======
                                System.out.println("Your total balance is: " + bankService.getBalance(account1));
                                System.out.println("Please enter the amount to Withdraw:  ");
>>>>>>> origin/master
                                String withdraw = scanner.nextLine();
                                if(withdraw.equalsIgnoreCase("back")){
                                    break;
                                }
                                double wd = Double.parseDouble(withdraw);
                                String withdrawalMessage = bankService.withdraw(account1, wd);
                                System.out.println(withdrawalMessage);
                                if(withdrawalMessage.contains("Transaction successful.")){
                                    break;
                                }
                            } catch (NumberFormatException exception) {
                                System.out.println("Please enter numbers only for withdrawal. Example: 100");
                            }
                        }
                        break;

                    }
                    case 3: {
                        while(true) {
                            try {
<<<<<<< HEAD
                                System.out.println("\nYou total balance is: " + bankService.getBalance(account1));
                                System.out.println("\nPlease enter the amount to Transfer (or type 'back' to go back):  ");
=======
                                System.out.println("Your total balance is: " + bankService.getBalance(account1));
                                System.out.println("Please enter the amount to Transfer:  ");
>>>>>>> origin/master
                                String transfer = scanner.nextLine();
                                if(transfer.equalsIgnoreCase("back")){
                                    break;
                                }
                                double transferAmount = Double.parseDouble(transfer);

<<<<<<< HEAD
                                System.out.println("\nPlease enter the account Id you wish to make a transfer to (or type back to go back):  ");
=======

                                System.out.println("Please enter the account Id you wish to make a transfer to:  ");
>>>>>>> origin/master
                                String accounId = scanner.nextLine();
                                if(accounId.equalsIgnoreCase("back")){
                                    break;
                                }
                                String transferMessage = bankService.transferBetweenAccounts(account1, accounId, transferAmount);
                                System.out.println(transferMessage);
                                if(transferMessage.contains("one business day for transfer")){
                                    break;
                                }
                            }catch (NumberFormatException exception){
                                System.out.println("Please enter numbers only for the transfer. Example: 100");
                            }
                        }
                        break;

                    }
                    case 4: {
                        System.out.println("\nYour total balance is: " + bankService.getBalance(account1));
                        break;
                    }
                    case 5: {
                        System.out.println("Your transactions history: ");
                        System.out.println(bankService.getTransactionHistory(account1));
                        break;
                    }
                    case 6: {
                        System.out.println("\nThank you for using Bank of Insufficient Funds.");
                        on = false;
                        break;
                    }
                    default: {
                        System.out.println();
                        System.out.println("Incorrect Input");
                        System.out.println("Please choose again only select 1,2,3,4,5 or 6\n");
                    }
                }

            }
        } catch (Exception e) {
            logger.error("Banking service unavailable", e);
            System.out.println("Service unavailable. Please try again later.");
        }
    }
}
