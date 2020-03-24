package ui.components;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import model.Transaction;
import ui.Controller;

import java.io.IOException;

public class TransactionTableView extends TableView<Transaction> {

    @FXML
    private TableColumn<Transaction, String> transactionTypeColumn;

    @FXML
    private TableColumn<Transaction, String> transactionAmountColumn;

    @FXML
    private TableColumn<Transaction, String> transactionTagColumn;

    public EventHandler<ActionEvent> editTransactionAction;
    private Controller controller;

    public TransactionTableView() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TransactionTable.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.initializeCellValueFactory();
        this.setupRowActions();
    }

    // EFFECTS: Transform transaction data into table view displayable variables
    private void initializeCellValueFactory() {
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
    private void setupRowActions() {
        this.setRowFactory(tableView -> {
            final TableRow<Transaction> row = new TableRow<>();
            final ContextMenu menu = new ContextMenu();
            MenuItem editItem = new MenuItem("Edit");
            MenuItem removeItem = new MenuItem("Remove");
            removeItem.setOnAction(this::removeTransactionAction);
            editItem.setOnAction(this::editTransactionAction);
            menu.getItems().addAll(editItem, removeItem);
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(menu)
                            .otherwise((ContextMenu)null));
            return row;
        });
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private void editTransactionAction(ActionEvent actionEvent) {
        Transaction selectedTransaction = this.getSelectionModel().getSelectedItem();
        controller.editTransaction(selectedTransaction).ifPresent((Transaction transaction) -> {
            selectedTransaction.setAmount(
                    transaction.getNetAmount()
            ).setTag(
                    transaction.getTag()
            ).setType(
                    Transaction.TransactionType.valueOf(transaction.getType())
            );
            this.refresh();
        });
    }

    private void removeTransactionAction(ActionEvent actionEvent) {
        this.getItems().remove(this.getSelectionModel().getSelectedItem());
    }


}
