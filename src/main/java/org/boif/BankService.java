package org.boif;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Scanner;

public class BankService {


    final Logger logger = LoggerFactory.getLogger(BankService.class);
    Scanner scanner = new Scanner(System.in);
    private Account account = null;
    BankingRepository bankingRepository = new BankingRepository();


    public Account validateAccount(String accountId, String pin) {

        Account account1 = bankingRepository.loginUser(accountId, pin);

        if(account1 != null){

             return  account1;

        }else{

            return null;
        }

    }

    public double getBalance(Account account) {
        return account.getBalance();

    }
    public void depositfunds(Account account, double deposit){
        if(deposit<1){

            System.out.println("Deposit amount has to be atleast $1.");
            return;
        }

        account.setBalance(account.getBalance() + deposit);
        Account updatedAccount = bankingRepository.depositFunds(account);
        if(updatedAccount == null){

            System.out.println("Transaction unsuccessful");
            logger.info("Account with accountId :{} was unable to complete transactions\n", account.getAccountId());
            return;

        }
        else{

            System.out.println("Transaction successful. Thank you for banking with us."
                    + "\nCurrent Balance: $" + updatedAccount.getBalance());
            logger.info("\nAccount with accountId: {} transaction was successful: \n", account.getAccountId());
            return;

        }


    }
    public void withdraw(Account account, double amount) {
        if (amount > getBalance(account)) {

            System.out.println("Insufficient Funds");
            return;
        }
        account.setBalance(account.getBalance() - amount);
        Account updatedAccount = bankingRepository.depositFunds(account);
        if(updatedAccount == null){

            System.out.println("Transaction unsuccessful");
            logger.info("Account with accountId: {} was unable to complete transactions\n", account.getAccountId());
            return;

        }
        else{

            System.out.println("Transaction successful. Thank you for Banking with us."
                    + "\nCurrent Balance: $" + updatedAccount.getBalance());
            logger.info("\nAccount with accountId: {} transaction was successful\n", account.getAccountId());
            return;

        }

    }

    public boolean logout() {
        logger.info("Account with accountId {} successfully logged out", account.getAccountId());
        account = null;
        return true;
    }


    public void signupUser() {
        System.out.println("Thank you for choosing Bank of Insufficient Funds. Please enter an" +
                " account Id to use for logging in.");
        String accountId = scanner.nextLine();
        System.out.println("\nPlease enter a 6-digit pin number that you will remember.");
        String pinNumber = scanner.nextLine();


        Account signupAccount = new Account(accountId, pinNumber, 0);
        bankingRepository.signupUser(signupAccount);


    }


    public Account getLoggedInAccount(String accountId, String pin) {

       Account account1 = bankingRepository.loginUser(accountId, pin);
       return account1;
    }
}
