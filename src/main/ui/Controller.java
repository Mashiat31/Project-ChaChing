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
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
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
    private TableColumn<Transaction, String> transactionTypeColumn;

    @FXML
    private TableColumn<Transaction, String> transactionAmountColumn;

    @FXML
    private TableColumn<Transaction, String> transactionTagColumn;

    @FXML
    private TableView<Transaction> transactionTable;

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
        Dialog<Account> dialog = new Dialog<>();
        dialog.setTitle("New Account");
        dialog.setHeaderText("Enter details for your account information");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField accountNameTextField = new TextField();
        Label accountNameLabel = new Label("Account Name:");
        HBox accountNameFormField = new HBox(8, accountNameLabel, accountNameTextField);
        TextField budgetTextField = new TextField();
        Label budgetNameLabel = new Label("Budget:");
        HBox budgetFormField = new HBox(8, budgetNameLabel, budgetTextField);
        dialogPane.setContent(new VBox(8, accountNameFormField, budgetFormField));
        Platform.runLater(accountNameTextField::requestFocus);
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                return new Account(accountNameTextField.getText(), Double.parseDouble(budgetTextField.getText()));
            }
            return null;
        });
        Optional<Account> optionalAccount = dialog.showAndWait();
        optionalAccount.ifPresent((Account account) -> {
            this.accounts.add(account);
        });
    }
    // EFFECTS: Returns a skeleton object for dialog view
    private Dialog createDialogView(String title, String headerText, Node...nodes) {
        Dialog dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialogPane.setContent(new VBox(8, nodes));
        return dialog;
    }
    // EFFECTS: Extract values from dialog text fields
    private Transaction getValueFromTransactionDialog(TextField amountTextField, TextField tagTextField, RadioButton expenseRadioButton) {
        double amount = Double.parseDouble(amountTextField.getText());
        Transaction.TransactionType type
                = expenseRadioButton.isSelected()
                ? Transaction.TransactionType.EXPENSE : Transaction.TransactionType.INCOME;
        return new Transaction(amount, type, tagTextField.getText());
    }
    // EFFECTS: Display input dialog with filled input from previously stored transaction and update
    // with changes
    private Optional<Transaction> editTransaction(Transaction existingTransaction) {
        ToggleGroup radioGroup = new ToggleGroup();
        RadioButton expenseRadioButton = new RadioButton("Expense");
        RadioButton incomeRadioButton = new RadioButton("Income");
        expenseRadioButton.setToggleGroup(radioGroup);
        incomeRadioButton.setToggleGroup(radioGroup);
        expenseRadioButton.setSelected(existingTransaction.getType().equals("EXPENSE"));
        incomeRadioButton.setSelected(existingTransaction.getType().equals("INCOME"));
        VBox optionsFormField = new VBox(8, new Label("Type:"), new HBox(8, expenseRadioButton, incomeRadioButton));
        TextField amountTextField = new TextField(String.format("%.2f", existingTransaction.getNetAmount()));
        HBox amountFormField = new HBox(8, new Label("Amount:"), amountTextField);
        TextField tagTextField = new TextField(existingTransaction.getTag());
        HBox tagFormField = new HBox(8, new Label("Tag:"), tagTextField);
        Dialog<Transaction> dialog
                = createDialogView("Edit Transaction",
                "Update details for your transaction", optionsFormField, amountFormField, tagFormField);
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                return getValueFromTransactionDialog(amountTextField, tagTextField, expenseRadioButton);
            }
            return null;
        });
        return dialog.showAndWait();
    }
    // EFFECTS: Display input dialog for user to input transaction creation details and add
    // newly created transaction to the viewing account's transaction list
    @FXML
    private void addTransaction() {
        ToggleGroup radioGroup = new ToggleGroup();
        RadioButton expenseRadioButton = new RadioButton("Expense");
        RadioButton incomeRadioButton = new RadioButton("Income");
        expenseRadioButton.setSelected(true);
        expenseRadioButton.setToggleGroup(radioGroup);
        incomeRadioButton.setToggleGroup(radioGroup);
        VBox optionsFormField = new VBox(8, new Label("Type:"), new HBox(8, expenseRadioButton, incomeRadioButton));
        TextField amountTextField = new TextField();
        HBox amountFormField = new HBox(8, new Label("Amount:"), amountTextField);
        TextField tagTextField = new TextField();
        HBox tagFormField = new HBox(8, new Label("Tag:"), tagTextField);
        Dialog<Transaction> dialog = createDialogView("New Transaction",
                "Enter details for your new transaction", optionsFormField, amountFormField, tagFormField);
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                return getValueFromTransactionDialog(amountTextField, tagTextField, expenseRadioButton);
            }
            return null;
        });
        dialog.showAndWait().ifPresent((Transaction transaction) -> {
            this.currentAccount.addTransaction(transaction);
        });
    }
    // EFFECTS: Gather all expense transactions and create a pie chart from it and display it
    @FXML
    private void showPieChart() {
        PieChart pieChart = new PieChart();
        for (String tag: currentAccount.getTransactionTags("EXPENSE")) {
            double expenseTotal = 0;
            for (Transaction transaction: currentAccount.getTransactionsByTags(tag)) {
                expenseTotal += transaction.getNetAmount();
            }
            PieChart.Data slice = new PieChart.Data(tag, expenseTotal);
            pieChart.getData().add(slice);
        }
        pieChart.setLegendSide(Side.LEFT);
        Dialog dialog = new Dialog();
        dialog.setTitle("Expense Overview");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK);
        dialogPane.setContent(new VBox(8, pieChart));
        dialog.show();
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
    // EFFECTS: Transform transaction data into table view displayable variables
    private void sanitizeTableColumnsDisplayValue() {
        transactionTypeColumn.setCellValueFactory(
                transactionStringCellDataFeatures
                    -> new SimpleStringProperty(transactionStringCellDataFeatures.getValue().getType())
        );
        transactionAmountColumn.setCellValueFactory(
                new PropertyValueFactory<Transaction, String>("amount")
        );
        transactionTagColumn.setCellValueFactory(
                new PropertyValueFactory<Transaction, String>("tag")
        );
    }
    // EFFECTS: Set up table view's right click context menu and its click event handler
    private void setupTransactionTableView() {
        transactionTable.setRowFactory(tableView -> {
            final TableRow<Transaction> row = new TableRow<>();
            final ContextMenu menu = new ContextMenu();
            MenuItem editItem = new MenuItem("Edit");
            MenuItem removeItem = new MenuItem("Remove");
            removeItem.setOnAction(e -> {
                transactionTable.getItems().remove(row.getItem());
            });
            editItem.setOnAction(e -> {
                editTransaction(row.getItem()).ifPresent((Transaction transaction) -> {
                    row.getItem().setAmount(transaction.getNetAmount()).setTag(transaction.getTag()).setType(Transaction.TransactionType.valueOf(transaction.getType()));
                    tableView.refresh();
                });
            });
            menu.getItems().addAll(editItem,removeItem);
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(menu)
                            .otherwise((ContextMenu)null));
            return row;
        });
    }
    // EFFECTS: Set default state to buttons when there is no data to act on
    private void setDefaultButtonStates() {
        addTransactionMenuButton.setDisable(true);
        saveMenuButton.setDisable(true);
        saveAsMenuButton.setDisable(true);
        chartButton.setDisable(true);
    }
    // EFFECTS: Override default implementation and calls above view setup functions
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDefaultButtonStates();
        setupAccountListViewEventListeners();
        accountListView.setItems(accounts);
        setupTransactionTableView();
        sanitizeTableColumnsDisplayValue();
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
