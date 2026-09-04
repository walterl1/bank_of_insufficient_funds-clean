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
    
    public boolean withdraw(String accountId, double amount) {
        if (amount > getBalance(accountId)) {
            return false;
        }
        return true;
    }

   
        

    
}
