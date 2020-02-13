package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransactionTest {
    private Transaction transaction;

    @BeforeEach
    public void runBefore() {
        this.transaction = new Transaction(15, Transaction.TransactionType.EXPENSE, "Food");
    }

    @Test
    public void testConstructor() {
        assertEquals(15, transaction.getNetAmount());
        assertEquals(-15, transaction.getAmount());
        assertEquals("Food", transaction.getTag());
    }

    @Test
    public void testSetAmount() {
        assertEquals(15, transaction.getNetAmount());
        this.transaction.setAmount(20);
        assertEquals(20, transaction.getNetAmount());
    }

    @Test
    public void testSetTag() {
        assertEquals("Food", transaction.getTag());
        this.transaction.setTag("Transport");
        assertEquals("Transport", transaction.getTag());
    }


}