package model;

import javafx.collections.ObservableList;
import persistence.FileOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for the FileOperator class
public class FileOperatorTest {

    FileOperator operator;

    // EFFECTS: initialize an instance of FileOperator before each test with test file path
    @BeforeEach
    public void runBefore() {
        operator = new FileOperator("./data/loadtest.csv");
    }

    // EFFECTS: test load() by comparing values in assertion to show that values of accounts and its transactions
    // come out of deserialization matches the test file
    @Test
    public void testLoad() {
        try {
            ObservableList<Account> accounts = operator.load();
            assertEquals(2, accounts.size());
            assertEquals("Testing Purpose", accounts.get(0).getDescription());
            assertEquals("Testing Purpose 2", accounts.get(1).getDescription());
        } catch (IOException e) {
            fail("Load Accounts from file failed");
        }
    }

    // EFFECTS: test whether exception will be triggered if a non existing path is set
    @Test
    public void testLoadFail() {
        assertThrows(FileNotFoundException.class, () -> {
            operator.setPath("./data/nonexist.csv");
            ObservableList<Account> accounts = operator.load();
        });
    }

    // EFFECTS: test save() by creating a new account with a transaction and serialize it and save it to disk
    // check in assertion to see whether read value from file saved previously matched expected string
    @Test
    public void testSave() {
        try {
            String testSaveFilePath = "./data/savetest.csv";
            ObservableList<Account> accounts = operator.load();
            Transaction transaction = new Transaction(50, Transaction.TransactionType.INCOME, "Tutoring");
            accounts.get(0).addTransaction(transaction);
            operator.setPath(testSaveFilePath);
            operator.save(accounts);
            File savedFile = new File(testSaveFilePath);
            Scanner scanner = new Scanner(savedFile);
            String expectedFirstLine = "Testing Purpose,2000.00,3";
            assertEquals(scanner.nextLine(), expectedFirstLine);
        } catch (IOException e) {
            fail("Save Account failed");
        }
    }
}
