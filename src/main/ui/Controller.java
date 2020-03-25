package ui;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Account;
import model.Transaction;
import persistence.FileOperator;
import ui.components.AccountDialog;
import ui.components.ExpenseDistributionView;
import ui.components.TransactionDialog;
import ui.components.TransactionTableView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

// Represents the controller for responding to all actions taken by user in gui application
public class Controller implements Initializable {

    private Stage stage;
    private ObservableList<Account> accounts;
    private FileOperator operator;
    private Account currentAccount;

    @FXML
    private ListView<Account> accountListView;

    @FXML
    private Button addTransactionMenuButton;

    @FXML
    private MenuItem saveMenuButton;

    @FXML
    private MenuItem saveAsMenuButton;

    @FXML
    private MenuItem editAccountMenuButton;

    @FXML
    private TransactionTableView transactionTable;

    @FXML
    private Text balanceLabel;

    @FXML
    private Text budgetLabel;

    @FXML
    private Button chartButton;
    // EFFECTS: Instantiate the application state with an empty list of accounts,
    // add an observer to enable Save As button when more than one account is detected in the list
    public Controller() {
        this.operator = new FileOperator();
        this.accounts = FXCollections.observableArrayList(
                new Callback<Account, Observable[]>() {
                    @Override
                    public Observable[] call(Account param) {
                        return new Observable[]{
                                param.descriptionProperty(),
                                param.budgetProperty()
                        };
                    }
                }
        );
        this.accounts.addListener((ListChangeListener<Account>) c -> {
            while (c.next()) {
                if (c.wasAdded() && saveAsMenuButton.isDisable()) {
                    saveAsMenuButton.setDisable(false);
                }
            }
        });
    }
    // EFFECTS: Display file browser in a dialog and load accounts from file by deserializing it
    @FXML
    private void openFile() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open your personal finance records");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Finance Records", "*.csv")
        );
        File fileChosen = fileChooser.showOpenDialog(this.stage);
        if (fileChosen != null) {
            this.operator.setPath(fileChosen.getAbsolutePath());
            this.accounts.addAll(operator.load());
            this.saveMenuButton.setDisable(false);
            this.saveAsMenuButton.setDisable(false);
        }
    }
    // EFFECTS: Save current accounts data to predefined path
    @FXML
    private void saveFile() throws IOException {
        operator.save(this.accounts);
    }
    // EFFECTS: Display file browser for user to choose an alternative file path for saving accounts data
    @FXML
    private void saveFileAs() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Finance Records", "*.csv")
        );
        File fileChosen = fileChooser.showSaveDialog(this.stage);
        this.operator.setPath(fileChosen.getAbsolutePath());
        this.operator.save(accounts);
    }
    // EFFECTS: Display input dialog to gather account creation details from user to add newly created
    // account to the account list
    @FXML
    private void addAccount() {
        AccountDialog dialog = new AccountDialog();
        Optional<Account> optionalAccount = dialog.showAndWait();
        optionalAccount.ifPresent((Account account) -> {
            this.accounts.add(account);
        });
    }

    // EFFECTS: Display input dialog to allow user to edit their previously saved account information
    @FXML
    private void editAccount() {
        AccountDialog dialog = new AccountDialog(currentAccount);
        Optional<Account> optionalAccount = dialog.showAndWait();
        optionalAccount.ifPresent((Account account) -> {
            currentAccount.setDescription(account.getDescription());
            currentAccount.setBudget(account.getBudget());
        });
    }

    // EFFECTS: Display input dialog with filled input from previously stored transaction and update
    // with changes
    public Optional<Transaction> editTransaction(Transaction existingTransaction) {
        TransactionDialog dialog = new TransactionDialog(existingTransaction);
        return dialog.showAndWait();
    }
    // EFFECTS: Display input dialog for user to input transaction creation details and add
    // newly created transaction to the viewing account's transaction list
    @FXML
    private void addTransaction() {
        TransactionDialog dialog = new TransactionDialog();
        dialog.showAndWait().ifPresent((Transaction transaction) -> {
            this.currentAccount.addTransaction(transaction);
        });
    }
    // EFFECTS: Gather all expense transactions and create a pie chart from it and display it
    @FXML
    private void showExpenseDistribution() {
        ExpenseDistributionView distribution = new ExpenseDistributionView(currentAccount);
        distribution.show();
    }
    // EFFECTS: Handles accounts update in list view and respond to item selection in list view
    private void setupAccountListViewEventListeners() {
        accountListView.setCellFactory(param -> new ListCell<Account>() {
            @Override
            protected void updateItem(Account item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getDescription() == null) {
                    setText(null);
                } else {
                    setText(item.getDescription());
                }
            }
        });
        accountListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Account>() {
            @Override
            public void changed(ObservableValue<? extends Account> observable, Account oldValue, Account newValue) {
                currentAccount = accounts.get(accountListView.getSelectionModel().getSelectedIndices().get(0));
                editAccountMenuButton.setDisable(false);
                contextualizeAccountView();
            }
        });
    }
    // EFFECTS: Set default button states, initialize budget and balance text view with current account
    // variable getters and setup table view with transactions
    private void contextualizeAccountView() {
        budgetLabel.setText(String.format("Budget: %.2f", currentAccount.getBudget()));
        balanceLabel.setText(String.format("Balance: %.2f", currentAccount.getSurplus()));
        transactionTable.setItems(currentAccount.getTransactions());
        addTransactionMenuButton.setDisable(false);
        chartButton.setDisable(false);
    }

    // EFFECTS: Set default state to buttons when there is no data to act on
    private void setDefaultButtonStates() {
        addTransactionMenuButton.setDisable(true);
        saveMenuButton.setDisable(true);
        saveAsMenuButton.setDisable(true);
        editAccountMenuButton.setDisable(true);
        chartButton.setDisable(true);
    }
    // EFFECTS: Override default implementation and calls above view setup functions
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDefaultButtonStates();
        setupAccountListViewEventListeners();
        accountListView.setItems(accounts);
        this.transactionTable.setController(this);
    }
    // EFFECTS: Handles quit menu button click and proceed to exit the application
    @FXML
    private void quitApplication() {
        Platform.exit();
        System.exit(0);
    }
    // EFFECTS: Allows for dependency injection for various functions to execute that required application's stage
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
