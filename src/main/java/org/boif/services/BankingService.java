package org.boif.services;

import org.boif.account.BankAccount;
import org.boif.repository.BankRepository;
import org.boif.user.UserAccount;

import java.util.List;
import java.util.Scanner;

public class BankingService {

    private UserAccount userAccount;
    BankRepository bankRepository = new BankRepository();
    Scanner scanner = new Scanner(System.in);


    public static boolean validatePassword(String pin, String confirmPin) {

        if (!pin.equals(confirmPin)) {

            System.out.println("Pin numbers do not match.Please try again.");
            return false;

        }
        if(pin.length() != 6){

            System.out.println("Pin number must be six digits in length. Please try again.");
            return false;
        }

        return true;

    }

    public void addNewBankAccount(UserAccount userAccount, String accountType) {

        bankRepository.addNewBankAccount(userAccount, accountType);

    }

    public UserAccount loginUser(String uname, String pin){

        UserAccount account = bankRepository.loginUser(uname, pin);
        return account;

    }

    public List<BankAccount> returnListOfAccounts(UserAccount userAccount){

        List<BankAccount> bankAccounts =  bankRepository.loginUser(userAccount);
        return bankAccounts;

    }

    public void SignUpUser(UserAccount userAccount){

        bankRepository.signupUser(userAccount);


    }

    public void registerBankAccount(UserAccount userAccount) {

        bankRepository.registerBankAccount(userAccount);
    }

    public void withdrawFunds(double amount, BankAccount bankAccount) {

        if(amount > bankAccount.getAccountBalance()){

            System.out.println("Insufficient Funds");
        }
        else {
            bankAccount.setAccountBalance(bankAccount.getAccountBalance() - amount);
            bankRepository.updateAccountBalance(bankAccount);
            System.out.println("Withdraw amount: $" + amount + "\nCurrent balance: $" + bankAccount.getAccountBalance()+ "\n");

        }
    }

    public void depositFunds(double amount, BankAccount bankAccount) {

        bankAccount.setAccountBalance(bankAccount.getAccountBalance() + amount);
        bankRepository.addFunds(bankAccount);
        System.out.println("Deposit amount: $" + amount + "\nCurrent balance: $" + bankAccount.getAccountBalance() + "\n");
    }

    public void makeTransfer(BankAccount sendingBankAccount, BankAccount recipientBankAccount, int transferAmount) {

        sendingBankAccount.setAccountBalance(sendingBankAccount.getAccountBalance() - transferAmount);
        recipientBankAccount.setAccountBalance(recipientBankAccount.getAccountBalance() + transferAmount);
        bankRepository.updateTransfer(sendingBankAccount, recipientBankAccount);
    }
}
