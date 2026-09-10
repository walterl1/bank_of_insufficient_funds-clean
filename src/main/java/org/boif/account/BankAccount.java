package org.boif.account;

import org.boif.services.BankingService;


public class BankAccount {


        private String accountOwner;
        private String firstName;
        private String lastName;
        public static String nameOfBank = "Bank of Insufficient Funds";
        private long accountNumber;
        private String accountType;
        private double accountBalance;
        BankingService bankService = new BankingService();

        public BankAccount(double accountBalance, long accountNumber){
            this.accountBalance = accountBalance;
            this.accountNumber = accountNumber;
        }


        public void desposit(double amount, BankAccount bankAccount){

            bankService.depositFunds(amount, bankAccount);

        }

        public void withdraw(double amount, BankAccount bankAccount){

            bankService.withdrawFunds(amount, bankAccount);

        }
        public void getBalance(){

            System.out.println("Current balance: $" + accountBalance);
        }

        public String getAccountOwner(){

            return accountOwner;
        }

        public void setAccountOwner(String firstName, String lastName){
            this.firstName = firstName;
            this.lastName = lastName;

            accountOwner = firstName + " " + lastName;
        }

        public String getAccountType(){

            return accountType;
        }

        public void setAccountType(String accountType){
            this.accountType = accountType;
        }

        public long getAccountNumber(){

            return accountNumber;
        }

        public double getAccountBalance() {
            return accountBalance;
        }

        public void setAccountBalance(double accountBalance) {
            this.accountBalance = accountBalance;
        }

        public void setAccountNumber(long accountNumber) {
            this.accountNumber = accountNumber;
        }

        public static String getNameOfBank() {
            return nameOfBank;
        }


        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        @Override
        public String toString() {
            return "   " + firstName + " " + lastName + "'s " + "BankAccount:" +
                    "  AccountOwner='" + accountOwner + '\'' +
                    "  AccountNumber=" + accountNumber +
                    "  AccountType='" + accountType + '\'' +
                    "  AccountBalance= $" + accountBalance + "\n";
        }
}
