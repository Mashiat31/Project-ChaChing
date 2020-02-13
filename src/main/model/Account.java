package model;

import java.util.ArrayList;

public class Account {
    public String description;
    private ArrayList<Transaction> transactions;
    private double budget;

    public Account(String description, double budget) {
        this.transactions = new ArrayList<Transaction>();
        this.description = description;
        this.budget = budget;
    }

    public boolean isOverBudgetLimit() {
        return this.budget + getTotalByTransactionType(Transaction.TransactionType.EXPENSE) < 0;
    }

    public double getSurplus() {
        return this.budget + getTotalByTransactionType(Transaction.TransactionType.EXPENSE) + getTotalByTransactionType(Transaction.TransactionType.INCOME);
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

    public double getTransactionsByTags(String tag) {
        double amount = 0;
        for (Transaction transaction: this.transactions) {
            if (transaction.getTag().equals(tag)) {
                amount += transaction.getAmount();
                break;
            }
        }
        return amount;
    }

    public double getTotalByTransactionType(Transaction.TransactionType type) {
        double total = 0;
        for (Transaction transaction: this.transactions) {
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
        for (Transaction transaction: this.transactions) {
            if (transaction.getAmount() > 0) {
                income = income.concat(transaction.toString());
            }
        }
        String expense = "***** EXPENSE *****\n";
        for (Transaction transaction: this.transactions) {
            if (transaction.getAmount() < 0) {
                expense = expense.concat(transaction.toString());
            }
        }
        String budget = String.format("* Budget: %f *\n", this.budget);
        return title.concat(budget).concat(income).concat(expense);
    }
}