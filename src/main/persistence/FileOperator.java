package persistence;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Account;
import ui.ChaChing;

import java.io.*;
import java.util.Scanner;


public class FileOperator {
    private String path;
    // EFFECTS: Default constructor when no path is given or will be set in a later time

    public FileOperator() {}
    // EFFECTS: Instantiating this class with a given path for reading file from / writing file to

    public FileOperator(String path) {
        this.path = path;
    }
    // EFFECTS: Accept modification to update file path to read from or write to

    public void setPath(String path) {
        this.path = path;
    }

    // EFFECTS: Performs file io operation to read file from disk and returns deserialized result
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
    // EFFECTS: Performs file io operation to write string of serialized text into file and save to disk

    public void save(ObservableList<Account> accounts) throws IOException {
        FileWriter writer = new FileWriter(path);
        for (Account account: accounts) {
            writer.write(account.serialize());
        }
        writer.close();
    }


}
