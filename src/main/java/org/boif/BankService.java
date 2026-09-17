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
             logger.info("User successfully logged in: {}", accountId);
             return  account1;

        }else{
            logger.error("Incorrect PIN or account ID entered for: {}", accountId);
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

    public String signupUser(String accountId, String pinNumber) {
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
            return "Failed to create account. Please contact a system administrator.";
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


            logger.error("Account with accountId :{} was unable to deposit because they attempted to deposit less than $1. Deposit amount: {}", account.getAccountId(), deposit);
            return "Deposit amount has to be at least $1\n";
        }

        double previousBalance = account.getBalance();
        account.setBalance(previousBalance + deposit);
        Account updatedAccount = bankingRepository.depositFunds(account);
        if(updatedAccount == null){
            account.setBalance(previousBalance);

            logger.error("Account with accountId :{} was unable to complete transactions for deposit amount: {}", account.getAccountId(), deposit);
            return "Transaction unsuccessful. Please contact a system administrator.";


        }
        else{


            bankingRepository.recordTransaction(account, "DEPOSIT", deposit);
            logger.info("Account with accountId: {} deposit was successful. Deposit amount: {}, New balance: {}", account.getAccountId(), deposit, updatedAccount.getBalance());
            return "Transaction successful. Thank you for banking with us."
                    + "\nCurrent Balance: $" + String.format("%.2f",updatedAccount.getBalance());

        }


    }

    public String withdraw(Account account, double amount) {
        if (amount <= 0) {
            logger.error("Account with accountId: {} attempted an invalid withdrawal amount: {}", account.getAccountId(), amount);
            return "Withdrawal amount has to be greater than $0\n";
        }

        if (amount > getBalance(account)) {

            logger.error("Account with accountId: {} withdrawal was unsuccessful: Insufficient funds for withdrawal amount: {}", account.getAccountId(), amount);
            return "Insufficient funds\n";
        }
        double previousBalance = account.getBalance();
        account.setBalance(previousBalance - amount);
        Account updatedAccount = bankingRepository.depositFunds(account);
        if(updatedAccount == null){
            account.setBalance(previousBalance);

            logger.error("Account with accountId: {} was unable to withdraw amount: {}", account.getAccountId(), amount);
            return "Transaction unsuccessful. Please contact a system administrator.\n";


        }
        else{
            logger.info("Account with accountId: {} withdrawal was successful. Withdrawal amount: {}, New balance: {}", account.getAccountId(), amount, updatedAccount.getBalance());
            bankingRepository.recordTransaction(account, "WITHDRAWAL", amount);
            return  "Transaction successful. Thank you for Banking with us."
                    + "\nCurrent Balance: $" + String.format("%.2f",updatedAccount.getBalance());
        }

    }

    public String transferBetweenAccounts(Account account1, String accountId, double transferAmount) {

        if (transferAmount <= 0) {
            logger.error("Account with accountId {} Transfer attempt failed due to zero or negative transfer amount: {}.", account1.getAccountId(), transferAmount);
            return "Transfer amount has to be greater than $0";
        }

        if(transferAmount > account1.getBalance())
        {
            logger.error("Account with accountId {} Transfer attempt failed due to insufficient funds for transfer amount: {}.", account1.getAccountId(), transferAmount);
            return "Insufficient funds \n";
        }

        if (accountId.equals(account1.getAccountId())) {
            logger.error("Account with accountId {} Transfer attempt failed: target accountId {} is the same account.", account1.getAccountId(), accountId);
            return "Cannot transfer to the same accountId";
        }

        Account account2 = bankingRepository.loginUser(accountId);
        if (account2 == null) {
            logger.error("Account with accountId {} Transfer attempt failed due to inexistent target accountId {} for amount {}.", account1.getAccountId(), accountId, transferAmount);
            return "Account with accountId " + accountId + " does not exist";
        }

        double receiverPreviousBalance = account2.getBalance();
        account1.setBalance(account1.getBalance() - transferAmount);
        account2.setBalance(account2.getBalance() + transferAmount);

        boolean res = bankingRepository.makeTransfer(account1, account2);
        if (!res) {
            account1.setBalance(account1.getBalance() + transferAmount);
            account2.setBalance(receiverPreviousBalance);
            logger.error("Account with accountId {} Transfer attempt to accountId {} failed for amount {}.", account1.getAccountId(), accountId, transferAmount);
            return "Failed to transfer balance. Please contact a system administrator.";
        }

        logger.info("Account with accountId {} Transaction successful. Transfer sent to accountId {}. Transfer amount: {} New balance: {}", account1.getAccountId(), account2.getAccountId(), String.format("%.2f",transferAmount), String.format("%.2f",account1.getBalance()));
        logger.info("Account with accountId {} Transaction successful. Transfer received from accountId {}. Transfer amount: {} New balance: {}", account2.getAccountId(), account1.getAccountId(), String.format("%.2f",transferAmount), String.format("%.2f",account2.getBalance()));

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
