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

    @FXML
    private void openFile() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open your personal finance records");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Finance Records", "*.csv")
        );
        File fileChosen = fileChooser.showOpenDialog(this.stage);
        if(fileChosen != null) {
            this.operator.setPath(fileChosen.getAbsolutePath());
            this.accounts.addAll(operator.load());
            this.saveMenuButton.setDisable(false);
            this.saveAsMenuButton.setDisable(false);
        }
    }

    @FXML
    private void saveFile() throws IOException {
        operator.save(this.accounts);
    }

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

    private Dialog createDialogView(String title, String headerText, Node ...nodes) {
        Dialog dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialogPane.setContent(new VBox(8, nodes));
        return dialog;
    }

    private Optional<Transaction> editTransation(Transaction existingTransaction) {
        RadioButton expenseRadioButton = new RadioButton("Expense");
        RadioButton incomeRadioButton = new RadioButton("Income");
        if (existingTransaction.getType().equals("EXPENSE")) {
            expenseRadioButton.setSelected(true);
        } else {
            incomeRadioButton.setSelected(true);
        }
        ToggleGroup radioGroup = new ToggleGroup();
        expenseRadioButton.setToggleGroup(radioGroup);
        incomeRadioButton.setToggleGroup(radioGroup);
        VBox optionsFormField = new VBox(8, new Label("Type:"), new HBox(8, expenseRadioButton, incomeRadioButton));
        TextField amountTextField = new TextField(String.format("%.2f", existingTransaction.getNetAmount()));
        HBox amountFormField = new HBox(8, new Label("Amount:"), amountTextField);
        TextField tagTextField = new TextField(existingTransaction.getTag());
        HBox tagFormField = new HBox(8, new Label("Tag:"), tagTextField);
        Dialog<Transaction> dialog = createDialogView("Edit Transaction", "Update details for your transaction", optionsFormField, amountFormField, tagFormField);
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                double amount = Double.parseDouble(amountTextField.getText());
                Transaction.TransactionType type = expenseRadioButton.isSelected()? Transaction.TransactionType.EXPENSE: Transaction.TransactionType.INCOME;
                return new Transaction(amount, type, tagTextField.getText());
            }
            return null;
        });
        return dialog.showAndWait();
    }

    @FXML
    private void addTransaction() {
        RadioButton expenseRadioButton = new RadioButton("Expense");
        RadioButton incomeRadioButton = new RadioButton("Income");
        expenseRadioButton.setSelected(true);
        ToggleGroup radioGroup = new ToggleGroup();
        expenseRadioButton.setToggleGroup(radioGroup);
        incomeRadioButton.setToggleGroup(radioGroup);
        VBox optionsFormField = new VBox(8, new Label("Type:"), new HBox(8, expenseRadioButton, incomeRadioButton));
        TextField amountTextField = new TextField();
        HBox amountFormField = new HBox(8, new Label("Amount:"), amountTextField);
        TextField tagTextField = new TextField();
        HBox tagFormField = new HBox(8, new Label("Tag:"), tagTextField);
        Dialog<Transaction> dialog = createDialogView("New Transaction", "Enter details for your new transaction", optionsFormField, amountFormField, tagFormField);
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                double amount = Double.parseDouble(amountTextField.getText());
                Transaction.TransactionType type = expenseRadioButton.isSelected()? Transaction.TransactionType.EXPENSE: Transaction.TransactionType.INCOME;
                return new Transaction(amount, type, tagTextField.getText());
            }
            return null;
        });
        Optional<Transaction> optionalTransaction = dialog.showAndWait();
        optionalTransaction.ifPresent((Transaction transaction) -> {
            this.currentAccount.addTransaction(transaction);
        });
    }

    @FXML
    private void showPieChart() {
        PieChart pieChart = new PieChart();
        for(String tag: currentAccount.getTransactionTags("EXPENSE")) {
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

    private void contextualizeAccountView() {
        budgetLabel.setText(String.format("Budget: %.2f", currentAccount.getBudget()));
        balanceLabel.setText(String.format("Balance: %.2f", currentAccount.getSurplus()));
        transactionTable.setItems(currentAccount.getTransactions());
        addTransactionMenuButton.setDisable(false);
        chartButton.setDisable(false);
    }

    private void sanitizeTableColumnsDisplayValue() {
        transactionTypeColumn.setCellValueFactory(
                transactionStringCellDataFeatures -> new SimpleStringProperty(transactionStringCellDataFeatures.getValue().getType())
        );
        transactionAmountColumn.setCellValueFactory(
                new PropertyValueFactory<Transaction, String>("amount")
        );
        transactionTagColumn.setCellValueFactory(
                new PropertyValueFactory<Transaction, String>("tag")
        );
    }

    private void setupTransactionTableView() {
        transactionTable.setEditable(true);
        transactionTable.setRowFactory(tableView -> {
            final TableRow<Transaction> row = new TableRow<>();
            final ContextMenu menu = new ContextMenu();
            MenuItem editItem = new MenuItem("Edit");
            MenuItem removeItem = new MenuItem("Remove");
            removeItem.setOnAction(e -> {
                transactionTable.getItems().remove(row.getItem());
            });
            editItem.setOnAction(e -> {
                Optional<Transaction> optionalTransaction = editTransation(row.getItem());
                optionalTransaction.ifPresent((Transaction transaction) -> {
                    row.getItem().setAmount(transaction.getNetAmount());
                    row.getItem().setTag(transaction.getTag());
                    row.getItem().setType(Transaction.TransactionType.valueOf(transaction.getType()));
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

    private void setDefaultButtonStates() {
        addTransactionMenuButton.setDisable(true);
        saveMenuButton.setDisable(true);
        saveAsMenuButton.setDisable(true);
        chartButton.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDefaultButtonStates();
        setupAccountListViewEventListeners();
        accountListView.setItems(accounts);
        setupTransactionTableView();
        sanitizeTableColumnsDisplayValue();
    }

    @FXML
    private void quitApplication() {
        Platform.exit();
        System.exit(0);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
