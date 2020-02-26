package model;

import static org.junit.jupiter.api.Assertions.*;

import model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

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
    public void testAddTransaction() {
        Transaction transaction = new Transaction(5, Transaction.TransactionType.EXPENSE, "Transport");
        this.account.addTransaction(transaction);
        assertEquals(-5,this.account.getSurplus());
    }

    @Test
    public void testGetTransactions() {
        Transaction transaction = new Transaction(5, Transaction.TransactionType.EXPENSE, "Transport");
        this.account.addTransaction(transaction);
        assertEquals(-5, this.account.getTransactions().get(0).getAmount());
        assertEquals("Transport", this.account.getTransactions().get(0).getTag());
    }

    @Test
    public void testUpdateTransaction() {
        Transaction transaction = new Transaction(5, Transaction.TransactionType.EXPENSE, "Transport");
        this.account.addTransaction(transaction);
        this.account.updateTransaction(0, 20, "Transport");
        assertEquals(-20,this.account.getSurplus());
    }

    @Test
    public void testRemoveTransaction() {
        Transaction transaction = new Transaction(5, Transaction.TransactionType.EXPENSE, "Transport");
        this.account.addTransaction(transaction);
        assertEquals(4995.0, this.account.getSurplus());
        this.account.removeTransaction(0);
        assertEquals(5000, this.account.getSurplus());
    }

    @Test
    public void testGetTransactionsByTags() {
        Transaction firstTransaction = new Transaction(5, Transaction.TransactionType.EXPENSE, "Transport");
        Transaction secondTransaction = new Transaction(10, Transaction.TransactionType.EXPENSE, "Food");
        this.account.addTransaction(firstTransaction);
        this.account.addTransaction(secondTransaction);
        assertEquals(5,this.account.getTransactionsByTags("Transport"));
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

    @Test
    public void testToString() {
        String textUI = "===== Account Summary =====\n* Budget: 5000.00 *\n***** INCOME *****\nTutoring: 10.00\n***** EXPENSE *****\nTransport: 5.00\n";
        Transaction firstTransaction = new Transaction(5, Transaction.TransactionType.EXPENSE, "Transport");
        Transaction secondTransaction = new Transaction(10, Transaction.TransactionType.INCOME, "Tutoring");
        this.account.addTransaction(firstTransaction);
        this.account.addTransaction(secondTransaction);
        assertEquals(textUI, account.toString());
    }

    @Test
    public void testSerialize() {
        Transaction firstTransaction = new Transaction(5, Transaction.TransactionType.EXPENSE, "Transport");
        Transaction secondTransaction = new Transaction(10, Transaction.TransactionType.EXPENSE, "Food");
        this.account.addTransaction(firstTransaction);
        this.account.addTransaction(secondTransaction);
        String expectedOutput = "Monthly Spending,5000.00,2\nTransport,EXPENSE,5.00\nFood,EXPENSE,10.00\n";
        assertEquals(expectedOutput, this.account.serialize());
    }

    @Test
    public void testDeserialize() {
        assertEquals(0, this.account.getTransactions().size());
        String input = "Monthly Spending,5000.00,2\nStationery,EXPENSE,20.00\nReimbursement,INCOME,150.00";
        Scanner scanner = new Scanner(input);
        this.account.deserialize(scanner);
        assertEquals("Stationery",this.account.getTransactions().get(0).getTag());
        assertEquals(20,this.account.getTransactions().get(0).getNetAmount());
        assertEquals("Reimbursement",this.account.getTransactions().get(1).getTag());
        assertEquals(150,this.account.getTransactions().get(1).getNetAmount());
    }
}