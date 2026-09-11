package org.boif;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

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

    public double getBalance(Account account) {

        return account.getBalance();

    }
    public void depositfunds(Account account, double deposit){
        if(deposit<1){

            System.out.println("Deposit amount has to be atleast $1.");
            logger.error("Account with accountId :{} was unable to complete transaction due to a lack of funds.\n", account.getAccountId());
            return;
        }

        account.setBalance(account.getBalance() + deposit);
        Account updatedAccount = bankingRepository.depositFunds(account);
        if(updatedAccount == null){

            System.out.println("Transaction unsuccessful");
            logger.error("Account with accountId :{} was unable to complete transactions\n", account.getAccountId());


        }
        else{

            System.out.println("Transaction successful. Thank you for banking with us."
                    + "\nCurrent Balance: $" + updatedAccount.getBalance());
            bankingRepository.recordTransaction(account, "DEPOSIT", deposit);
            logger.info("\nAccount with accountId: {} transaction was successful: \n", account.getAccountId());

        }


    }
    public void withdraw(Account account, double amount) {
        if (amount > getBalance(account)) {

            System.out.println("Insufficient Funds");
            logger.error("\nAccount with accountId: {} transaction was unsuccessful: Insufficient funds: \n", account.getAccountId());
            return;
        }
        account.setBalance(account.getBalance() - amount);
        Account updatedAccount = bankingRepository.depositFunds(account);
        if(updatedAccount == null){

            System.out.println("Transaction unsuccessful");
            logger.error("Account with accountId: {} was unable to complete transactions\n", account.getAccountId());


        }
        else{

            System.out.println("Transaction successful. Thank you for Banking with us."
                    + "\nCurrent Balance: $" + updatedAccount.getBalance());
            logger.info("\nAccount with accountId: {} transaction was successful\n", account.getAccountId());
            bankingRepository.recordTransaction(account, "WITHDRAWAL", amount);

        }

    }


    public String signupUser(String accountId, String pinNumber) {

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

    public void transferBetweenAccounts(Account account1, String accountId, double transferAmount) {

        if (transferAmount <= 0) {
            System.out.println("Transfer amount has be greater than $0.");
            logger.error("Account with accountId {} Transfer attempt failed due to zero or negative transfer.", account1.getAccountId());
            return;
        }

        if(transferAmount > account1.getBalance())
        {
            System.out.println("Insufficient funds");
            logger.error("Account with accountId {} Transfer attempt failed due to insufficient funds.", account1.getAccountId());
            return;
        }

        Account account2 = bankingRepository.loginUser(accountId);
        if (account2 == null) {
            System.out.println("Account with accountId " + accountId + " does not exist");
            logger.error("Account with accountId {} Transfer attempt failed due to inexistent target account.", account1.getAccountId());
            return;
        }

        account1.setBalance(account1.getBalance() - transferAmount);
        account2.setBalance(account2.getBalance() + transferAmount);
        bankingRepository.makeTransfer(account1, account2);
        logger.info("Account with accountId {} Transaction successful. Transfer sent. $\n" + account1.getBalance(), account1.getAccountId());
        logger.info("Account with accountId {} Transaction successful. Transfer sent. $\n" + account2.getBalance(), account2.getAccountId());

        bankingRepository.recordTransaction(account1, "TRANSFER_OUT", transferAmount);
        bankingRepository.recordTransaction(account2, "TRANSFER_IN", transferAmount);

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
