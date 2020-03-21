package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

// Unit tests for Transaction model
public class TransactionTest {
    private Transaction transaction;

    // EFFECTS: setup a default transaction record before each run of the following tests
    @BeforeEach
    public void runBefore() {
        this.transaction = new Transaction(15, Transaction.TransactionType.EXPENSE, "Food");
    }

    // EFFECTS: test for values being set into the correct corresponding fields
    @Test
    public void testConstructor() {
        assertEquals(15, transaction.getNetAmount());
        assertEquals(-15, transaction.getAmount());
        assertEquals("Food", transaction.getTag());
    }
    // EFFECTS: test set amount by asserting amount before and after updating its value
    @Test
    public void testSetAmount() {
        assertEquals(15, transaction.getNetAmount());
        this.transaction.setAmount(20);
        assertEquals(20, transaction.getNetAmount());
    }
    // EFFECTS: test set tag by asserting tag before and after updating its value
    @Test
    public void testSetTag() {
        assertEquals("Food", transaction.getTag());
        this.transaction.setTag("Transport");
        assertEquals("Transport", transaction.getTag());
    }
    // EFFECTS: test toString by updating the transaction and asserting its return value from toString()
    // with an expected string output
    @Test
    public void testToString() {
        this.transaction.setAmount(30);
        this.transaction.setTag("Phone Bills");
        assertEquals("Phone Bills: 30.00\n", this.transaction.toString());
    }
    // EFFECTS: test serialization by updating the transaction and asserting its return value from serialize()
    // with an expected parsable string output
    @Test
    public void testSerialize() {
        assertNotEquals(20, this.transaction.getNetAmount());
        assertNotEquals("Stationery", this.transaction.getTag());
        this.transaction.setAmount(20);
        this.transaction.setTag("Stationery");
        String expectedOutput = "Stationery,EXPENSE,20.00\n";
        assertEquals(expectedOutput, this.transaction.serialize());
    }
    // EFFECTS: test deserialization by updating the transaction and takes a parsable string to deserialize()
    // and assert the transaction getters to see if values matches from the parsable string
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