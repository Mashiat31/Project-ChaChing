package ui;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    @FXML
    private void addTransaction() {
        if (currentAccount != null) {
            Dialog<Transaction> dialog = new Dialog<>();
            dialog.setTitle("New Transaction");
            dialog.setHeaderText("Enter details for your new transaction");
            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            Label transactionTypeLabel = new Label("Type:");
            RadioButton expenseRadioButton = new RadioButton("Expense");
            expenseRadioButton.setSelected(true);
            RadioButton incomeRadioButton = new RadioButton("Income");
            ToggleGroup radioGroup = new ToggleGroup();
            expenseRadioButton.setToggleGroup(radioGroup);
            incomeRadioButton.setToggleGroup(radioGroup);
            HBox options = new HBox(8, expenseRadioButton, incomeRadioButton);
            VBox optionsFormField = new VBox(8, transactionTypeLabel, options);
            TextField amountTextField = new TextField();
            Label amountNameLabel = new Label("Amount:");
            HBox amountFormField = new HBox(8, amountNameLabel, amountTextField);
            TextField tagTextField = new TextField();
            Label tagNameLabel = new Label("Tag:");
            HBox tagFormField = new HBox(8, tagNameLabel, tagTextField);
            dialogPane.setContent(new VBox(8, optionsFormField, amountFormField, tagFormField));
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
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addTransactionMenuButton.setDisable(true);
        saveMenuButton.setDisable(true);
        saveAsMenuButton.setDisable(true);
        accountListView.setCellFactory(param -> new ListCell<Account>() {
            @Override
            protected void updateItem(Account item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.description.get() == null) {
                    setText(null);
                } else {
                    setText(item.description.getValue());
                }
            }
        });
        accountListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Account>() {
            @Override
            public void changed(ObservableValue<? extends Account> observable, Account oldValue, Account newValue) {
                // Your action here
                currentAccount = accounts.get(accountListView.getSelectionModel().getSelectedIndices().get(0));
                budgetLabel.setText(String.format("Budget: %.2f", currentAccount.getBudget()));
                balanceLabel.setText(String.format("Balance: %.2f", currentAccount.getSurplus()));
                transactionTable.setItems(currentAccount.getTransactions());
                addTransactionMenuButton.setDisable(false);
            }
        });
        accountListView.setItems(accounts);
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

    @FXML
    private void quitApplication() {
        Platform.exit();
        System.exit(0);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
