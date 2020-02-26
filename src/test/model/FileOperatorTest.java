package model;

import ui.ChaChing;
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
    private ChaChing app;

    @BeforeEach
    public void runBefore() {

        app = new ChaChing();
        System.setIn(new ByteArrayInputStream("Testing Purpose\n2000\n".getBytes()));
        app.createAccount();
        System.setIn(new ByteArrayInputStream("1\n2\n31\nStationery\n".getBytes()));
        app.addTransaction();
        System.setIn(new ByteArrayInputStream("1\n1\n150\nReimbursement\n".getBytes()));
        app.addTransaction();
    }

    @Test
    public void testLoad() {
        try {
            System.setIn(new ByteArrayInputStream("./data/loadtest.csv\nY\n".getBytes()));
            app.loadAccounts();
            assertEquals(2, app.getAccounts().size());
            assertEquals("Testing Purpose", app.getAccounts().get(0).description);
            assertEquals("Testing Purpose 2", app.getAccounts().get(1).description);
        } catch (IOException e) {
            fail("Load Accounts from file failed");
        }
    }

    @Test
    public void testLoadFail() {
        assertThrows(FileNotFoundException.class, () -> {
            System.setIn(new ByteArrayInputStream("./data/nonexist.csv\nY\n".getBytes()));
            app.loadAccounts();
        });
    }

    @Test
    public void testSave() {
        try {
            System.setIn(new ByteArrayInputStream("./data/savetest.csv".getBytes()));
            app.saveAccounts();
            File savedFile = new File("./data/savetest.csv");
            Scanner scanner = new Scanner(savedFile);
            String expectedFirstLine = "Testing Purpose,2000.00,2";
            assertEquals(scanner.nextLine(), expectedFirstLine);
        } catch (IOException e) {
            fail("Save Account failed");
        }
    }

    @Test
    public void testSaveMultipleAccounts() {
        try {
            System.setIn(new ByteArrayInputStream("Testing Purpose 2\n1031\n".getBytes()));
            app.createAccount();
            System.setIn(new ByteArrayInputStream("2\n2\n31\nTransport\n".getBytes()));
            app.addTransaction();
            System.setIn(new ByteArrayInputStream("2\n1\n150\nGov Incentive\n".getBytes()));
            app.addTransaction();
            System.setIn(new ByteArrayInputStream("./data/savetest2.csv".getBytes()));
            app.saveAccounts();
            File savedFile = new File("./data/savetest2.csv");
            Scanner scanner = new Scanner(savedFile);
            String expectedFirstLine = "Testing Purpose,2000.00,2";
            String expectedFourthLine = "Testing Purpose 2,1031.00,2";
            assertEquals(scanner.nextLine(), expectedFirstLine);
            for(int i = 0; i< 2;i++) {
                scanner.nextLine();
            }
            assertEquals(scanner.nextLine(), expectedFourthLine);
        } catch (IOException e) {
            fail("Save Account failed");
        }
    }

    @Test
    public void testOverwritten() {
        try {
            assertEquals(1,app.getAccounts().size());
            assertEquals(31, app.getAccounts().get(0).getTransactions().get(0).getNetAmount());
            System.setIn(new ByteArrayInputStream("./data/loadtest.csv\nY\n".getBytes()));
            app.loadAccounts();
            assertNotEquals(1, app.getAccounts().size());
            assertNotEquals(31,app.getAccounts().get(0).getTransactions().get(0).getNetAmount());
            assertEquals(20, app.getAccounts().get(0).getTransactions().get(0).getNetAmount());
        } catch (IOException e) {
            fail("Failed overwritten test");
        }
    }
}
