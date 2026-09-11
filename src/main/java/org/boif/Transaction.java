package org.boif;

import java.sql.Time;

public class Transaction {
    private String type;
    private double amount;
    private Time ts;

    public Transaction(String type, double amount, Time ts) {
        this.type = type;
        this.amount = amount;
        this.ts = ts;
    }

    @Override
    public String toString() {
        return this.type + " " + amount + " at " + ts;
    }
}
