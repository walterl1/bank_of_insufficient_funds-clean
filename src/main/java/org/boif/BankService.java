package org.boif;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BankService {
    final static Logger logger = LoggerFactory.getLogger(BankService.class);

    public boolean validateAccount(String accountId, String pin) {
        logger.info("Account with accountId {} successfully logged in", accountId);
        return true;
    }

    public double getBalance(String accountId) {
        return 1000.0;

    }
    public double depositfunds(String accountID, double deposit){
        if(deposit<0){
            return -1;
        }

        double total = getBalance(accountID)+deposit;
        return total;
    }
    
    public double withdraw(String accountId, double amount) {
        if (amount > getBalance(accountId)) {
            return -1;
        }
        double currentBalance = getBalance(accountId) - amount;
        return currentBalance;
    }

   
        

    
}
