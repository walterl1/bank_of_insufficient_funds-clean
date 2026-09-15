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


    public String validateAccountId(String accountId) {
        if (accountId == null || accountId.isBlank()) {
            return "Account ID cannot be empty.";
        }
        accountId = accountId.trim();
        if (accountId.length() < 4 || accountId.length() > 20) {
            return "Account ID must be between 4 and 20 characters.";
        }
        if (!accountId.matches("^[a-zA-Z0-9_]+$")) {
            return "Account ID may only contain letters, numbers, and underscores.";
        }
        return null; // null means valid
    }

    public String validatePin(String pin) {
        if (pin == null || pin.isBlank()) {
            return "PIN cannot be empty.";
        }
        pin = pin.trim();
        if (!pin.matches("^\\d{6}$")) {
            return "PIN must be exactly 6 digits (0-9).";
        }
        return null; // null means valid
    }


    public Account validateAccount(String accountId, String pin) {

        Account account1 = bankingRepository.loginUser(accountId, pin);

        if(account1 != null){

             return  account1;

        }else{

            return null;
        }

    }

    public String validateCredentials(String accountId, String pin) {
        String idErr = validateAccountId(accountId);
        if (idErr != null) return idErr;

        String pinErr = validatePin(pin);
        if (pinErr != null) return pinErr;

        return null;
    }

    public String promptForAccountId() {
        while (true) {
            System.out.print("Enter Account ID: ");
            String input = scanner.nextLine();
            String error = validateAccountId(input);
            if (error == null) {
                return input.trim().toLowerCase();
            }
            System.out.println("Invalid input: " + error);
        }
    }

    public String promptForPin() {
        while (true) {
            System.out.print("Enter 6-digit PIN: ");
            String input = scanner.nextLine();
            String error = validatePin(input);
            if (error == null) {
                return input.trim();
            }
            System.out.println("Invalid input: " + error);
        }
    }

    public String signupUser(String accountId, String pinNumber) throws SQLException {
        // 1. Validate format first
        String validationError = validateCredentials(accountId, pinNumber);
        if (validationError != null) {
            return validationError;
        }

        // 2. Normalize
        accountId = accountId.trim().toLowerCase();
        pinNumber = pinNumber.trim();

        // 3. Check for existing account
        List<Account> allAccounts = bankingRepository.getAllAccounts();
        for (int i = 0; i < allAccounts.size(); i++) {
            if (Objects.equals(allAccounts.get(i).getAccountId(), accountId)) {
                return "Username already exists";
            }
        }

        // 4. Persist
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
            return "Deposit amount has to at least $1\n";
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
        if (amount <= 0) {
            logger.error("Account with accountId: {} attempted an invalid withdrawal amount: {}", account.getAccountId(), amount);
            return "Withdrawal amount has to be greater than $0\n";
        }

        if (amount > getBalance(account)) {

            logger.error("\nAccount with accountId: {} transaction was unsuccessful: Insufficient funds: \n", account.getAccountId());
            return "Insufficient funds\n";
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
