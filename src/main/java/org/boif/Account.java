package org.boif;

public class Account {
    private String accountId;
    private String pin;
    private double balance;

    public Account(String accountId, String pin) {
        this.accountId = accountId;
        this.pin = pin;
        this.balance = 0;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getPin() {
        return pin;
    }
}
