package model;

import persistence.CSVSerializable;

import java.util.ArrayList;
import java.util.Scanner;

public class Account implements CSVSerializable<Account> {
    public String description;
    private ArrayList<Transaction> transactions;
    private double budget;

    public Account(String description, double budget) {
        this.transactions = new ArrayList<Transaction>();
        this.description = description;
        this.budget = budget;
    }

    public Account() {}

    public boolean isOverBudgetLimit() {
        return this.budget + getTotalByTransactionType(Transaction.TransactionType.EXPENSE) < 0;
    }

    public double getSurplus() {
        return this.budget + getTotalByTransactionType(Transaction.TransactionType.EXPENSE)
                + getTotalByTransactionType(Transaction.TransactionType.INCOME);
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public void updateTransaction(int index, double amount, String tag) {
        Transaction transaction = this.transactions.get(index);
        transaction.setAmount(amount);
        transaction.setTag(tag);
    }

    public ArrayList<Transaction> getTransactions() {
        return this.transactions;
    }

    public void removeTransaction(int index) {
        this.transactions.remove(index);
    }

    public double getTransactionsByTags(String tag) {
        double amount = 0;
        for (Transaction transaction : this.transactions) {
            if (transaction.getTag().equals(tag)) {
                amount += transaction.getAmount();
                break;
            }
        }
        return amount;
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
        String budget = String.format("* Budget: %.2f *\n", this.budget);
        return title.concat(budget).concat(income).concat(expense);
    }

    @Override
    public String serialize() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("%s,%.2f,%d\n", this.description, this.budget, this.transactions.size()));
        for (Transaction transaction: this.transactions) {
            result.append(transaction.serialize());
        }
        return result.toString();
    }

    @Override
    public void deserialize(Scanner scanner){
        String line = scanner.nextLine();
        String[] tokens = line.split(",");
        this.description = tokens[0];
        this.budget = Double.parseDouble(tokens[1]);
        int numTransaction = Integer.parseInt(tokens[2]);
        this.transactions = new ArrayList<>();
        for(int i = 0; i < numTransaction; i++) {
            Transaction t = new Transaction();
            t.deserialize(scanner);
            this.transactions.add(t);
        }
    }
}