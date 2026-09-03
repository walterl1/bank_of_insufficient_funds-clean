package org.boif;

public class BankService {
    public boolean validate(String accountId, String pin) {
        return true;
    }

    public double getBalance(String accountId) {
        return 1000.0;

    }
    
    public boolean withdraw(String accountId, double amount) {
        if (amount > getBalance(accountId)) {
            return false;
        }
        return true;
    }

   
        

    
}
