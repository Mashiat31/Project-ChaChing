package model;

import java.util.ArrayList;

public class  Account {
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
        return this.budget + getTotalByTransactionType(Transaction.TransactionType.EXPENSE);
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public void updateTransaction(int index, double amount, ArrayList<String> tags) {
        Transaction transaction = this.transactions.get(index);
        transaction.setAmount(amount);
        transaction.setTags(tags);
    }

    public void removeTransaction(int index) {
        this.transactions.remove(index);
    }

    public double getTransactionsByTags(String[] tags) {
        double amount = 0;
        for (Transaction transaction: this.transactions) {
            for (String tag: tags) {
                if (transaction.getTags().contains(tag)) {
                    amount += transaction.getAmount();
                    break;
                }
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
}