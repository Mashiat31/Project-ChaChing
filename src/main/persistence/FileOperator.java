package persistence;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Account;
import ui.ChaChing;

import java.io.*;
import java.util.Scanner;

public class FileOperator {
    private String path;

    public FileOperator() {

    }

    public FileOperator(String path) {
        this.path = path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ObservableList<Account> load() throws FileNotFoundException {
        FileReader reader = new FileReader(path);
        Scanner scanner = new Scanner(reader);
        ObservableList<Account> accounts = FXCollections.observableArrayList();
        while (scanner.hasNext()) {
            Account account = new Account();
            account.deserialize(scanner);
            accounts.add(account);
        }
        scanner.close();
        return accounts;
    }

    public void save(ObservableList<Account> accounts) throws IOException {
        FileWriter writer = new FileWriter(path);
        for (Account account: accounts) {
            writer.write(account.serialize());
        }
        writer.close();
    }


}
