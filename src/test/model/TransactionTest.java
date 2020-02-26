package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

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

    @Test
    public void testToString() {
        this.transaction.setAmount(30);
        this.transaction.setTag("Phone Bills");
        assertEquals("Phone Bills: 30.00\n", this.transaction.toString());
    }

    @Test
    public void testSerialize() {
        assertNotEquals(20, this.transaction.getNetAmount());
        assertNotEquals("Stationery", this.transaction.getTag());
        this.transaction.setAmount(20);
        this.transaction.setTag("Stationery");
        String expectedOutput = "Stationery,EXPENSE,20.00\n";
        assertEquals(expectedOutput, this.transaction.serialize());
    }

    @Test
    public void testDeserialize() {
        assertNotEquals(20, this.transaction.getNetAmount());
        assertNotEquals("Stationery", this.transaction.getTag());
        String input = "Stationery,EXPENSE,20.00\n";
        Scanner scanner = new Scanner(input);
        this.transaction.deserialize(scanner);
        assertEquals(20, this.transaction.getNetAmount());
        assertEquals("Stationery", this.transaction.getTag());
    }


}