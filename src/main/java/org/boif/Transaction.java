package org.boif;

import java.sql.Time;
import java.sql.Timestamp;

public class Transaction {
    private String type;
    private double amount;
    private Timestamp ts;

    public Transaction(String type, double amount, Timestamp ts) {
        this.type = type;
        this.amount = amount;
        this.ts = ts;
    }

    @Override
    public String toString() {
        return this.type + " " + String.format("%.2f",amount) + " at " + ts;
    }
}
