package persistence;

import model.Account;
import ui.ChaChing;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileOperator {
    private String path;

    public FileOperator(String path) {
        this.path = path;
    }

    public void load(ChaChing app) throws FileNotFoundException {
        FileReader reader = new FileReader(path);
        Scanner scanner = new Scanner(reader);
        ArrayList<Account> accounts = new ArrayList<>();
        while(scanner.hasNext()) {
            Account account = new Account();
            account.deserialize(scanner);
            accounts.add(account);
        }
        app.restoreAccounts(accounts);
        scanner.close();
    }

    public void save(ChaChing app) throws IOException {
        FileWriter writer = new FileWriter(path);
        for (Account account: app.getAccounts()) {
            writer.write(account.serialize());
        }
        writer.close();
    }


}
