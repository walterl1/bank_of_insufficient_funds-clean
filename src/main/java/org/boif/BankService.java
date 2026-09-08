package org.boif;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BankService {
    final Logger logger = LoggerFactory.getLogger(BankService.class);

    private Account account = null;

    public boolean validateAccount(String accountId, String pin) {
        logger.info("Account with accountId {} successfully logged in", accountId);

        account = new Account(accountId, pin);
        return true;
    }

    public double getBalance(String accountId) {
        return account.getBalance();

    }
    public double depositfunds(String accountID, double deposit){
        if(deposit<0){
            return -1;
        }

        double total = getBalance(accountID)+deposit;
        return total;
    }
    
    public boolean withdraw(String accountId, double amount) {
        if (amount > getBalance(accountId)) {
            return false;
        }
        return true;
    }

    public boolean logout() {
        logger.info("Account with accountId {} successfully logged out", account.getAccountId());
        account = null;
        return true;
    }
        

    
}
