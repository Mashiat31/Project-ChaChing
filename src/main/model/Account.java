package model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.Saveable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Represents a checking account, contains a list of transaction records, a budget and and description

public class Account implements Saveable<Account> {

    private StringProperty description;
    private ListProperty<Transaction> transactions;
    private DoubleProperty budget;
    // EFFECTS: Each account begins with an empty list of transactions, and has given a budget and description

    public Account(String description, double budget) {
        ObservableList<Transaction> observableList = FXCollections.observableArrayList();
        this.transactions = new SimpleListProperty<>(observableList);
        this.description = new SimpleStringProperty(description);
        this.budget = new SimpleDoubleProperty(budget);
    }
    // EFFECTS: Dummy constructor for implementing to Saveable interface

    public Account() {}
    // EFFECTS: Returns set budget of an account

    public double getBudget() {
        return budget.get();
    }
    // EFFECTS: Returns DoubleProperty version of primitive type variable budget for gui implementation

    public DoubleProperty budgetProperty() {
        return budget;
    }
    // EFFECTS: Returns description of an account

    public String getDescription() {
        return description.get();
    }
    // EFFECTS: Accept modification to description of an account

    public void setDescription(String description) {
        this.description.set(description);
    }
    // EFFECTS: Returns StringProperty version of String type variable description for gui implementation

    public StringProperty descriptionProperty() {
        return description;
    }
    // EFFECTS: Accept modification to the budget of an account

    public void setBudget(double budget) {
        this.budget.set(budget);
    }
    // EFFECTS: Returns whether the user of the account has exceeded the account's imposed budget limit

    public boolean isOverBudgetLimit() {
        return this.getBudget() + getTotalByTransactionType(Transaction.TransactionType.EXPENSE) < 0;
    }
    // EFFECTS: Returns the overall sum of all transaction activity

    public double getSurplus() {
        return this.getBudget() + getTotalByTransactionType(Transaction.TransactionType.EXPENSE)
                + getTotalByTransactionType(Transaction.TransactionType.INCOME);
    }
    // EFFECTS: Returns a set of tags from recorded transactions

    public ArrayList<String> getTransactionTags(String transactionType) {
        ArrayList<String> tags = new ArrayList<>();
        for (Transaction transaction: this.transactions.get()) {
            if (!tags.contains(transaction.getTag()) && transaction.getType().equals(transactionType)) {
                tags.add(transaction.getTag());
            }
        }
        return tags;
    }
    // EFFECTS: Returns all recorded transactions

    public ObservableList<Transaction> getTransactions() {
        return transactions.get();
    }
    // EFFECTS: Add a new transaction to existing account's list of transactions

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }
    // EFFECTS: Update a particular transaction in the list by its index

    public void updateTransaction(int index, double amount, String tag) {
        Transaction transaction = this.transactions.get(index);
        transaction.setAmount(amount);
        transaction.setTag(tag);
    }
    // EFFECTS: Remove a particular transaction in the list by its index

    public void removeTransaction(int index) {
        this.transactions.remove(index);
    }
    // EFFECTS: Retrieve a subset of transactions by querying a certain tag

    public ArrayList<Transaction> getTransactionsByTags(String tag) {
        ArrayList<Transaction> taggedTransactions = new ArrayList<>();
        for (Transaction transaction : this.transactions) {
            if (transaction.getTag().equals(tag)) {
                taggedTransactions.add(transaction);
            }
        }
        return taggedTransactions;
    }

    public Map<String, Double> getTaggedTransactionSumPair(String transactionType) {
        Map<String, Double> sumPair = new HashMap<>();
        for (String tag: this.getTransactionTags(transactionType)) {
            double total = 0;
            for (Transaction transaction: this.getTransactionsByTags(tag)) {
                total += transaction.getNetAmount();
            }
            sumPair.put(tag, total);
        }
        return sumPair;
    }

    // EFFECTS: Returns transactions either by INCOME or EXPENSE
    public double getTotalByTransactionType(Transaction.TransactionType type) {
        double total = 0;
        for (Transaction transaction : this.transactions) {
            if (type == Transaction.TransactionType.INCOME) {
                if (transaction.getAmount() > 0) {
                    total += transaction.getAmount();
                }
            } else {
                if (transaction.getAmount() < 0) {
                    total += transaction.getAmount();
                }
            }
        }
        return total;
    }
    // EFFECTS: Returns a String representation of the overall summary of an account

    public String toString() {
        String title = "===== Account Summary =====\n";
        String income = "***** INCOME *****\n";
        for (Transaction transaction : this.transactions) {
            if (transaction.getAmount() > 0) {
                income = income.concat(transaction.toString());
            }
        }
        String expense = "***** EXPENSE *****\n";
        for (Transaction transaction : this.transactions) {
            if (transaction.getAmount() < 0) {
                expense = expense.concat(transaction.toString());
            }
        }
        String budget = String.format("* Budget: %.2f *\n", this.budget.get());
        return title.concat(budget).concat(income).concat(expense);
    }
    // EFFECTS: Takes all of its stored transaction and account info from variables to parsable text for saving to file

    @Override
    public String serialize() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%s,%.2f,%d\n", this.description.get(),
                this.budget.get(), this.transactions.size()));
        for (Transaction transaction: this.transactions) {
            result.append(transaction.serialize());
        }
        return result.toString();
    }
    // EFFECTS: Retrieve and parse saved text and transform them
    // back into variables hence initializing an Account object

    @Override
    public void deserialize(Scanner scanner) {
        String line = scanner.nextLine();
        String[] tokens = line.split(",");
        this.description = new SimpleStringProperty(tokens[0]);
        this.budget = new SimpleDoubleProperty(Double.parseDouble(tokens[1]));
        int numTransaction = Integer.parseInt(tokens[2]);
        ObservableList<Transaction> observableList = FXCollections.observableArrayList();
        this.transactions = new SimpleListProperty<>(observableList);
        for (int i = 0; i < numTransaction; i++) {
            Transaction t = new Transaction();
            t.deserialize(scanner);
            this.transactions.add(t);
        }
    }
}
