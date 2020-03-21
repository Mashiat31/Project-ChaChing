package model;

import javafx.beans.property.*;
import persistence.Saveable;
import persistence.Saveable;

import java.util.ArrayList;
import java.util.Scanner;

// Represents the record of a transaction, either be expense or income, with a tag and amount transacted

public class Transaction implements Saveable<Transaction> {

    public enum TransactionType {
        EXPENSE,
        INCOME
    }

    private ObjectProperty<TransactionType> type;
    private StringProperty tag;
    private DoubleProperty amount;

    // EFFECTS: Instantiate a new transaction with the given amount, tag and type
    public Transaction(double amount, TransactionType type, String tag) {
        this.amount = new SimpleDoubleProperty(amount);
        this.type = new SimpleObjectProperty<>(type);
        this.tag = new SimpleStringProperty(tag);
    }
    // EFFECTS: Dummy constructor for implementing Saveable interface
    public Transaction() {}
    // EFFECTS: Returns the amount of the transaction, use negative to encapsulate the type being expense
    public double getAmount() {
        return this.type.get() == TransactionType.INCOME ? this.amount.get() : - this.amount.get();
    }
    // EFFECTS: Returns the net amount of the transaction
    public double getNetAmount() {
        return this.amount.get();
    }
    // EFFECTS: Returns the tagged String of the transaction
    public String getTag() {
        return this.tag.get();
    }
    // EFFECTS: Returns the String representation of the enum type
    public String getType() {
        return type.get().toString();
    }
    // EFFECTS: Accept modification to the transaction type
    public Transaction setType(TransactionType type) {
        this.type.set(type);
        return this;
    }
    // EFFECTS: Accept modification to the amount transacted
    public Transaction setAmount(double amount) {
        this.amount.set(amount);
        return this;
    }
    // EFFECTS: Accept modification to the tag of the transaction
    public Transaction setTag(String tag) {
        this.tag.set(tag);
        return this;
    }
    // EFFECTS: Returns a String representation of the transaction summary
    public String toString() {
        return String.format("%s: %.2f\n",this.getTag(), this.getNetAmount());
    }

    // EFFECTS: Takes all of its variables and transform them into parsable string of text
    @Override
    public String serialize() {
        return String.format("%s,%s,%.2f\n", this.getTag(),this.type.get().toString(), this.getNetAmount());
    }
    // Retrieve and parse saved text and transform them back into variables hence initializing a Transaction object
    @Override
    public void deserialize(Scanner scanner) {
        String line = scanner.nextLine();
        String[] tokens = line.split(",");
        this.tag = new SimpleStringProperty(tokens[0]);
        this.type = new SimpleObjectProperty<>(TransactionType.valueOf(tokens[1]));
        this.amount = new SimpleDoubleProperty(Double.parseDouble(tokens[2]));
    }

}