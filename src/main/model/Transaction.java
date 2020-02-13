package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Transaction {
    public enum TransactionType {
        EXPENSE,
        INCOME
    }

    private TransactionType type;
    private String tag;
    private double amount;

    public Transaction(double amount, TransactionType type, String tag) {
        this.amount = amount;
        this.type = type;
        this.tag = tag;
    }

    public double getAmount() {
        return this.type == TransactionType.INCOME ? this.amount : - this.amount;
    }

    public double getNetAmount() {
        return this.amount;
    }

    public String getTag() {
        return this.tag;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String toString() {
        return String.format("%s: %f\n",this.getTag(), this.getNetAmount());
    }

}