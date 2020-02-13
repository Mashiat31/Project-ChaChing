package ui;

import model.Account;

import java.util.ArrayList;
import java.util.Scanner;

public class Screen {
    private ArrayList<Account> accounts;

    public static void main(String[] args) {
        Screen app = new Screen();
        Scanner menuChoice = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            Screen.displayMenu();
            switch (menuChoice.nextInt()) {
                case 1:
                    app.createAccount();
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    Screen.displayMenu();
            }
        }
    }

    public Screen() {
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
        System.out.println("4. Exit");
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
        System.out.println("***** Account created successfully *****");
        System.out.println("> Account Summary: ");
        System.out.println(String.format("Description: %s", account.description));
        System.out.println(String.format("Current balance: %f", account.getSurplus()));
        System.out.println(">>>>> Returning to main menu <<<<<");
    }

    public void showAccounts() {

        System.out.println("---------ChaChing: View Accounts --------");

        for (Account account: this.accounts) {
            System.out.println(account.toString());
        }
    }

    public void addTransaction() {
        System.out.println("************ChaChing: Add Transaction ******");
        for (int i = 0; i < this.accounts.size(); i += 1) {
            System.out.println(String.format("%d. %s", i + 1, this.accounts.get(i)));

        }
        System.out.println("Please choose an account:");


    }
}