package model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.Saveable;

import java.util.ArrayList;
import java.util.Scanner;

public class Account implements Saveable<Account> {


    private StringProperty description;
    private ListProperty<Transaction> transactions;
    private DoubleProperty budget;

    public Account(String description, double budget) {
        ObservableList<Transaction> observableList = FXCollections.observableArrayList();
        this.transactions = new SimpleListProperty<>(observableList);
        this.description = new SimpleStringProperty(description);
        this.budget = new SimpleDoubleProperty(budget);
    }

    public Account() {}

    public double getBudget() {
        return budget.get();
    }

    public DoubleProperty budgetProperty() {
        return budget;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setBudget(double budget) {
        this.budget.set(budget);
    }

    public boolean isOverBudgetLimit() {
        return this.getBudget() + getTotalByTransactionType(Transaction.TransactionType.EXPENSE) < 0;
    }

    public double getSurplus() {
        return this.getBudget() + getTotalByTransactionType(Transaction.TransactionType.EXPENSE)
                + getTotalByTransactionType(Transaction.TransactionType.INCOME);
    }


    public ArrayList<String> getTransactionTags(String transactionType) {
        ArrayList<String> tags = new ArrayList<>();
        for (Transaction transaction: this.transactions.get()){
            if(!tags.contains(transaction.getTag()) && transaction.getType().equals(transactionType)) {
                tags.add(transaction.getTag());
            }
        }
        return tags;
    }

    public ObservableList<Transaction> getTransactions() {
        return transactions.get();
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public void updateTransaction(int index, double amount, String tag) {
        Transaction transaction = this.transactions.get(index);
        transaction.setAmount(amount);
        transaction.setTag(tag);
    }

    public void removeTransaction(int index) {
        this.transactions.remove(index);
    }

    public ArrayList<Transaction> getTransactionsByTags(String tag) {
        ArrayList<Transaction> taggedTransactions = new ArrayList<>();
        for (Transaction transaction : this.transactions) {
            if (transaction.getTag().equals(tag)) {
                taggedTransactions.add(transaction);
            }
        }
        return taggedTransactions;
    }

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

    @Override
    public String serialize() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%s,%.2f,%d\n", this.description.get(), this.budget.get(), this.transactions.size()));
        for (Transaction transaction: this.transactions) {
            result.append(transaction.serialize());
        }
        return result.toString();
    }

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