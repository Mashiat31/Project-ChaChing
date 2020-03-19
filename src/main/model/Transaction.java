package model;

import javafx.beans.property.*;
import persistence.Saveable;
import persistence.Saveable;

import java.util.ArrayList;
import java.util.Scanner;

public class Transaction implements Saveable<Transaction> {

    public enum TransactionType {
        EXPENSE,
        INCOME
    }

    private ObjectProperty<TransactionType> type;
    private StringProperty tag;
    private DoubleProperty amount;

    public Transaction(double amount, TransactionType type, String tag) {
        this.amount = new SimpleDoubleProperty(amount);
        this.type = new SimpleObjectProperty<>(type);
        this.tag = new SimpleStringProperty(tag);
    }

    public Transaction() {}

    public double getAmount() {
        return this.type.get() == TransactionType.INCOME ? this.amount.get() : - this.amount.get();
    }

    public double getNetAmount() {
        return this.amount.get();
    }

    public String getTag() {
        return this.tag.get();
    }

    public String getType() {
        return type.get().toString();
    }

    public void setType(TransactionType type) {
        this.type.set(type);
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public void setTag(String tag) {
        this.tag.set(tag);
    }

    public String toString() {
        return String.format("%s: %.2f\n",this.getTag(), this.getNetAmount());
    }

    @Override
    public String serialize() {
        return String.format("%s,%s,%.2f\n", this.getTag(),this.type.get().toString(), this.getNetAmount());
    }

    @Override
    public void deserialize(Scanner scanner) {
        String line = scanner.nextLine();
        String[] tokens = line.split(",");
        this.tag = new SimpleStringProperty(tokens[0]);
        this.type = new SimpleObjectProperty<>(TransactionType.valueOf(tokens[1]));
        this.amount = new SimpleDoubleProperty(Double.parseDouble(tokens[2]));
    }

}