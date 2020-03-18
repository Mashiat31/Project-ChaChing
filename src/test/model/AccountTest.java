package model;

import static org.junit.jupiter.api.Assertions.*;

import model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountTest {
    private Account account;

    @BeforeEach
    public void runBefore() {
        this.account = new Account("Monthly Spending", 5000);
    }

    @Test
    public void testConstructor() {
        assertEquals(5000, account.getSurplus());
    }

    @Test
    public void testGetBudget() {
        assertEquals(5000, account.getBudget());
    }

    @Test
    public void testSetBudget() {
        account.setBudget(4000);
        assertEquals(4000, account.getBudget());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Monthly Spending", account.getDescription());
    }

    @Test
    public void testSetDescription() {
        account.setDescription("Quarterly Spending");
        assertEquals("Quarterly Spending", account.getDescription());
    }

    @Test
    public void testAddTransaction() {
        Transaction transaction = new Transaction(5, Transaction.TransactionType.EXPENSE, "Transport");
        this.account.addTransaction(transaction);
        assertEquals(4995,this.account.getSurplus());
    }

    @Test
    public void testUpdateTransaction() {
        Transaction transaction = new Transaction(5, Transaction.TransactionType.EXPENSE, "Transport");
        this.account.addTransaction(transaction);
        this.account.updateTransaction(0, 20, "Transport");
        assertEquals(4980,this.account.getSurplus());
    }

    @Test
    public void testRemoveTransaction() {
        Transaction transaction = new Transaction(5, Transaction.TransactionType.EXPENSE, "Transport");
        account.addTransaction(transaction);
        assertEquals(1, this.account.getTransactions().size());
        account.removeTransaction(this.account.getTransactions().size()-1);
        assertEquals(0, this.account.getTransactions().size());
    }

    @Test
    public void testGetTransactionsByTags() {
        Transaction firstTransaction = new Transaction(5, Transaction.TransactionType.EXPENSE, "Transport");
        Transaction secondTransaction = new Transaction(10, Transaction.TransactionType.EXPENSE, "Food");
        this.account.addTransaction(firstTransaction);
        this.account.addTransaction(secondTransaction);
        assertEquals(1,this.account.getTransactionsByTags("Transport").size());
        assertEquals(-5, this.account.getTransactionsByTags("Transport").get(0).getAmount());
    }

    @Test
    public void testGetTotalByTransactionType() {
        Transaction firstTransaction = new Transaction(5, Transaction.TransactionType.EXPENSE, "Transport");
        Transaction secondTransaction = new Transaction(10, Transaction.TransactionType.INCOME, "Tutoring");
        this.account.addTransaction(firstTransaction);
        this.account.addTransaction(secondTransaction);
        assertEquals(10,this.account.getTotalByTransactionType(Transaction.TransactionType.INCOME));
    }

    @Test
    public void testIsOverBudgetLimit() {
        Transaction firstTransaction = new Transaction(6000, Transaction.TransactionType.EXPENSE, "Transport");
        this.account.addTransaction(firstTransaction);
        assertTrue(this.account.isOverBudgetLimit());
    }
}