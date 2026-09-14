package org.boif;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import org.boif.Transaction;

public class BankService {


    final Logger logger = LoggerFactory.getLogger(BankService.class);
    Scanner scanner = new Scanner(System.in);
    BankingRepository bankingRepository = new BankingRepository();


    public Account validateAccount(String accountId, String pin) {

        Account account1 = bankingRepository.loginUser(accountId, pin);

        if(account1 != null){

             return  account1;

        }else{

            return null;
        }

    }

    public String signupUser(String accountId, String pinNumber) throws SQLException {

        List<Account> allAccounts = bankingRepository.getAllAccounts();

        for(int i = 0; i < allAccounts.size(); i++){

            if(Objects.equals(allAccounts.get(0).getAccountId(), accountId))
                return "Username already exist";
        }

        if(pinNumber.length() != 6){

            return "pin has to be exactly 6 characters";
        }


        pinNumber.toLowerCase().trim();
        accountId.toLowerCase().trim();
        Account signupAccount = new Account(accountId, pinNumber, 0);
        boolean res = bankingRepository.signupUser(signupAccount);
        if (res) {
            logger.info("Successfully created account with accountId " + accountId + "!");
            return "Successfully created account!";
        } else {
            logger.error("Failed to create account with accountId " + accountId + ".");
            return "Failed to create account.";
        }
    }

    public Account getLoggedInAccount(String accountId, String pin) {

        Account account1 = bankingRepository.loginUser(accountId, pin);
        return account1;
    }

    public double getBalance(Account account) {

        return account.getBalance();

    }

    public String depositfunds(Account account, double deposit){

        if(deposit<1){


            logger.error("Account with accountId :{} was unable to complete transaction due to a lack of funds.\n", account.getAccountId());
            return "Deposit amount has to be more than $1.00";
        }

        account.setBalance(account.getBalance() + deposit);
        Account updatedAccount = bankingRepository.depositFunds(account);
        if(updatedAccount == null){

            logger.error("Account with accountId :{} was unable to complete transactions\n", account.getAccountId());
            return "Transaction unsuccessful";


        }
        else{


            bankingRepository.recordTransaction(account, "DEPOSIT", deposit);
            logger.info("\nAccount with accountId: {} transaction was successful: \n", account.getAccountId());
            return "Transaction successful. Thank you for banking with us."
                    + "\nCurrent Balance: $" + updatedAccount.getBalance();

        }


    }

    public String withdraw(Account account, double amount) {
        if (amount > getBalance(account)) {

            logger.error("\nAccount with accountId: {} transaction was unsuccessful: Insufficient funds: \n", account.getAccountId());
            return "insufficient funds\n";
        }
        account.setBalance(account.getBalance() - amount);
        Account updatedAccount = bankingRepository.depositFunds(account);
        if(updatedAccount == null){

            logger.error("Account with accountId: {} was unable to complete transactions\n", account.getAccountId());
            return "Transaction unsuccessful\n";


        }
        else{
            logger.info("\nAccount with accountId: {} transaction was successful\n", account.getAccountId());
            bankingRepository.recordTransaction(account, "WITHDRAWAL", amount);
            return  "Transaction successful. Thank you for Banking with us."
                    + "\nCurrent Balance: $" + updatedAccount.getBalance();
        }

    }

    public String transferBetweenAccounts(Account account1, String accountId, double transferAmount) {

        if (transferAmount <= 0) {
            logger.error("Account with accountId {} Transfer attempt failed due to zero or negative transfer.", account1.getAccountId());
            return "Transfer amount has to be greater than $0";
        }

        if(transferAmount > account1.getBalance())
        {
            logger.error("Account with accountId {} Transfer attempt failed due to insufficient funds.", account1.getAccountId());
            return "Insufficient funds \n";
        }

        if (accountId.equals(account1.getAccountId())) {
            logger.error("Account with accountId {} Transfer attempt failed due to insufficient funds.", account1.getAccountId());
            return "Cannot transfer to the same accountId";
        }

        Account account2 = bankingRepository.loginUser(accountId);
        if (account2 == null) {
            logger.error("Account with accountId {} Transfer attempt failed due to inexistent target account.", account1.getAccountId());
            return "Account with accountId " + accountId + " does not exist";
        }

        account1.setBalance(account1.getBalance() - transferAmount);
        account2.setBalance(account2.getBalance() + transferAmount);

        boolean res = bankingRepository.makeTransfer(account1, account2);
        if (!res) {
            account1.setBalance(account1.getBalance() + transferAmount);
            logger.error("Account with accountId {} Transfer attempt failed.", account1.getAccountId());
            return "Failed to transfer balance.";
        }

        logger.info("Account with accountId {} Transaction successful. Transfer sent. $\n" + account1.getBalance(), account1.getAccountId());
        logger.info("Account with accountId {} Transaction successful. Transfer sent. $\n" + account2.getBalance(), account2.getAccountId());

        bankingRepository.recordTransaction(account1, "TRANSFER_OUT", transferAmount);
        bankingRepository.recordTransaction(account2, "TRANSFER_IN", transferAmount);
        return "Please allow one business day for transfer to complete.";
    }

    public String getTransactionHistory(Account account) {
        StringBuilder sb = new StringBuilder();
        List<Transaction> transactions = bankingRepository.getTransactions(account);
        if (transactions.size() == 0) {
            return "No transactions found.\n";
        }

        for (int i = 0; i < transactions.size(); i++) {
            sb.append(i + 1 + ": " + transactions.get(i).toString() + "\n");
        }

        return sb.toString();
    }



}
