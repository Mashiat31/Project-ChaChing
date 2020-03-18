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

public class FileOperatorTest {

    FileOperator operator;

    @BeforeEach
    public void runBefore() {
        operator = new FileOperator("./data/loadtest.csv");
    }

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

    @Test
    public void testLoadFail() {
        assertThrows(FileNotFoundException.class, () -> {
            operator.setPath("./data/nonexist.csv");
            ObservableList<Account> accounts = operator.load();
        });
    }

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
