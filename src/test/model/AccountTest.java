package model;

import static org.junit.jupiter.api.Assertions.*;

import model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Unit tests for the Account model
public class AccountTest {
    private Account account;
    // EFFECTS: setup a new account before each test run
    @BeforeEach
    public void runBefore() {
        this.account = new Account("Monthly Spending", 5000);
    }
    // EFFECTS: test whether constructor from runBefore() has correctly set budget before any transaction occurs
    @Test
    public void testConstructor() {
        assertEquals(5000, account.getSurplus());
    }
    // EFFECTS: test getBudget() by asserting budget getter function with value set in runBefore()
    @Test
    public void testGetBudget() {
        assertEquals(5000, account.getBudget());
    }
    // EFFECTS: test setBudget() by updating the budget in the account and check for its value by asserting it
    @Test
    public void testSetBudget() {
        account.setBudget(4000);
        assertEquals(4000, account.getBudget());
    }
    // EFFECTS: test getDescription() by asserting description getter function with value set in runBefore()
    @Test
    public void testGetDescription() {
        assertEquals("Monthly Spending", account.getDescription());
    }
    // EFFECTS: test setDescription() by updating the description in the account and check for its value by asserting it
    @Test
    public void testSetDescription() {
        account.setDescription("Quarterly Spending");
        assertEquals("Quarterly Spending", account.getDescription());
    }
    // EFFECTS: test adddTransaction() by adding a new transaction to the account and asserts account get surplus function
    // to see if new transaction has an effect on overall transaction activity in the account
    @Test
    public void testAddTransaction() {
        Transaction transaction = new Transaction(5, Transaction.TransactionType.EXPENSE, "Transport");
        this.account.addTransaction(transaction);
        assertEquals(4995,this.account.getSurplus());
    }
    // EFFECTS: test updateTransaction() by adding then updating the transaction and asserts account get surplus
    // function to see if updated transaction value is reflected on overall transaction activity in the account
    @Test
    public void testUpdateTransaction() {
        Transaction transaction = new Transaction(5, Transaction.TransactionType.EXPENSE, "Transport");
        this.account.addTransaction(transaction);
        this.account.updateTransaction(0, 20, "Transport");
        assertEquals(4980,this.account.getSurplus());
    }
    // EFFECTS: test removeTransaction() by adding then removing a new transaction and asserts the total number of
    // transactions in an account
    @Test
    public void testRemoveTransaction() {
        Transaction transaction = new Transaction(5, Transaction.TransactionType.EXPENSE, "Transport");
        account.addTransaction(transaction);
        assertEquals(1, this.account.getTransactions().size());
        account.removeTransaction(this.account.getTransactions().size()-1);
        assertEquals(0, this.account.getTransactions().size());
    }
    // EFFECTS: test getTransactionsByTags() by adding new transactions with specific tags then retrieve them to
    // compare its value in assertion with a given tag
    @Test
    public void testGetTransactionsByTags() {
        Transaction firstTransaction = new Transaction(5, Transaction.TransactionType.EXPENSE, "Transport");
        Transaction secondTransaction = new Transaction(10, Transaction.TransactionType.EXPENSE, "Food");
        this.account.addTransaction(firstTransaction);
        this.account.addTransaction(secondTransaction);
        assertEquals(1,this.account.getTransactionsByTags("Transport").size());
        assertEquals(-5, this.account.getTransactionsByTags("Transport").get(0).getAmount());
    }
    // EFFECTS: test getTotalByTransactionType() by adding a few new transactions with different type and check
    // in assertion whether the sum of income was only affected by the newly added income type transaction
    @Test
    public void testGetTotalByTransactionType() {
        Transaction firstTransaction = new Transaction(5, Transaction.TransactionType.EXPENSE, "Transport");
        Transaction secondTransaction = new Transaction(10, Transaction.TransactionType.INCOME, "Tutoring");
        this.account.addTransaction(firstTransaction);
        this.account.addTransaction(secondTransaction);
        assertEquals(10,this.account.getTotalByTransactionType(Transaction.TransactionType.INCOME));
    }
    // EFFECTS: test isOverBudgetLimit() by adding a new transaction with number larger than imposed limit and
    // check in assertion whether added transaction will trigger the function to return false
    @Test
    public void testIsOverBudgetLimit() {
        Transaction firstTransaction = new Transaction(6000, Transaction.TransactionType.EXPENSE, "Transport");
        this.account.addTransaction(firstTransaction);
        assertTrue(this.account.isOverBudgetLimit());
    }
}