package model;

import persistence.CSVSerializable;

import java.util.ArrayList;
import java.util.Scanner;

public class Transaction implements CSVSerializable<Transaction> {

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

    public Transaction() {}

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
        return String.format("%s: %.2f\n",this.getTag(), this.getNetAmount());
    }

    @Override
    public String serialize() {
        return String.format("%s,%s,%.2f\n", this.getTag(),this.type.toString(), this.getNetAmount());
    }

    @Override
    public void deserialize(Scanner scanner) {
        String line = scanner.nextLine();
        String[] tokens = line.split(",");
        this.tag = tokens[0];
        this.type = TransactionType.valueOf(tokens[1]);
        this.amount = Double.parseDouble(tokens[2]);
    }

}