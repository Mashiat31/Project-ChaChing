package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Transaction {
    enum TransactionType {
        EXPENSE,
        INCOME
    }

    private TransactionType type;
    private ArrayList<String> tags;
    private double amount;
    private Date date;

    public Transaction(double amount, TransactionType type, ArrayList<String> tags, Date date) {
        this.date = date;
        this.amount = amount;
        this.type = type;
        this.tags = tags;
    }

    public double getAmount() {
        return this.type == TransactionType.INCOME ? this.amount : - this.amount;
    }

    public Date getDate() {
        return this.date;
    }

    public double getNetAmount() {
        return this.amount;
    }

    public ArrayList<String> getTags() {
        return this.tags;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String toString() {
        return String.format("");
    }

}