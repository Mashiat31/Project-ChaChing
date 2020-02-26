package ui;

import model.Account;
import model.Transaction;
import persistence.FileOperator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ChaChing {
    private ArrayList<Account> accounts;

    public static void main(String[] args) throws IOException {
        ChaChing app = new ChaChing();
        Scanner menuChoice = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            ChaChing.displayMenu();
            switch (menuChoice.nextInt()) {
                case 1:
                    app.createAccount();
                    break;
                case 2:
                    app.showAccounts();
                    break;
                case 3:
                    app.addTransaction();
                    break;
                case 4:
                    app.loadAccounts();
                    break;
                case 5:
                    app.saveAccounts();
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    ChaChing.displayMenu();
            }
        }
    }

    public ChaChing() {
        this.accounts = new ArrayList<Account>();
    }

    public ArrayList<Account> getAccounts() {
        return this.accounts;
    }

    public static void displayMenu() {
        System.out.println("===== ChaChing: Personal Finance Manager ====");
        System.out.println("1. Create new account");
        System.out.println("2. View current accounts");
        System.out.println("3. Add transaction to account");
        System.out.println("4. Open existing file");
        System.out.println("5. Save to file");
        System.out.println("6. Exit");
    }

    public void createAccount() {
        Scanner prompt = new Scanner(System.in);
        System.out.println("===== ChaChing: Create account ====");
        System.out.println("> Please enter a description for your account");
        String description = prompt.nextLine();
        System.out.println("> Please enter a budget for this account");
        double budget = prompt.nextDouble();
        Account account = new Account(description, budget);
        this.accounts.add(account);
        System.out.println("***** Account created successfully *****\n");
        System.out.println("> Account Summary: ");
        System.out.println(String.format("Description: %s", account.description));
        System.out.println(String.format("Current balance: %f", account.getSurplus()));
        System.out.println(">>>>> Returning to main menu <<<<<\n");
    }

    public void loadAccounts() throws FileNotFoundException {
        Scanner prompt = new Scanner(System.in);
        System.out.println("> Please enter the path your file is located:");
        String path = prompt.nextLine();
        FileOperator operator = new FileOperator(path);
        if (accounts.size()>0) {
            System.out.println("!!!!! WARNING: Existing records will be overwritten !!!!!\n");
            System.out.println("> Do you still wish to proceed? (y/N)");
            String proceed = prompt.nextLine();
            if (!proceed.toLowerCase().equals("y")) {
                return;
            }
        }
        operator.load(this);
    }

    public void saveAccounts() throws IOException {
        Scanner prompt = new Scanner(System.in);
        System.out.println("> Please enter the path your file is located:");
        String path = prompt.nextLine();
        FileOperator operator = new FileOperator(path);
        if (accounts.size() == 0) {
            System.out.println("!!!!! There is no record found !!!!!\n");
            return;
        }
        System.out.println("***** Account details has been successfully written into file *****\n");
        operator.save(this);
    }

    public void showAccounts() {
        System.out.println("===== ChaChing: View accounts ====");
        for (Account account: this.accounts) {
            System.out.println(account.toString());
        }
        System.out.println(">>>>> Returning to main menu <<<<<");
    }

    public void addTransaction() {
        Scanner prompt = new Scanner(System.in);
        System.out.println("===== ChaChing: Add Transaction ====");
        for (int i = 0; i < this.accounts.size(); i += 1) {
            System.out.println(String.format("%d. %s", i + 1, this.accounts.get(i)));
        }
        System.out.println("> Please choose an account:");
        int accountIndex = Integer.parseInt(prompt.nextLine());
        Account targetAccount = this.accounts.get(accountIndex - 1);
        System.out.println("> Account selected");
        System.out.println("> Is your transaction an income or expense ?");
        System.out.println("1. Income");
        System.out.println("2. Expense");
        Transaction.TransactionType type = Integer.parseInt(prompt.nextLine()) == 1 ? Transaction.TransactionType.INCOME :
                Transaction.TransactionType.EXPENSE;
        System.out.println("> Please enter the amount:");
        double amount = Double.parseDouble(prompt.nextLine());
        System.out.println("> Please tag(s) your transaction:");
        String tag = prompt.nextLine();
        Transaction transaction = new Transaction(amount, type, tag);
        targetAccount.addTransaction(transaction);
        System.out.println("***** Transaction recorded successfully *****");
        System.out.println(">>>>> Returning to main menu <<<<<\n");
    }

    public void restoreAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }
}