package ui.components;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.DialogInputException;
import model.Transaction;

// Represents dialog view for both adding and editing transaction class object
public class TransactionDialog extends ChaChingDialog<Transaction> {

    private final RadioButton expenseRadioButton = new RadioButton("Expense");
    private final RadioButton incomeRadioButton = new RadioButton("Income");
    private final ToggleGroup radioGroup = new ToggleGroup();
    private final TextField amountTextField = new TextField();
    private final TextField tagTextField = new TextField();

    // EFFECTS: Default constructor for displaying dialog view for adding new transaction
    public TransactionDialog() {
        super();
        this.setTitle("New Transaction");
        this.setHeaderText("Enter details for your new transaction");
        expenseRadioButton.setSelected(true);
        this.createInputControls();
    }

    // EFFECTS: Overload constructor to for displaying editing variant of the dialog view
    public TransactionDialog(Transaction transaction) {
        super();
        this.setTitle("Edit Transaction");
        this.setHeaderText("Update details for your transaction");
        this.expenseRadioButton.setSelected(transaction.getType().equals("EXPENSE"));
        this.incomeRadioButton.setSelected(transaction.getType().equals("INCOME"));
        this.amountTextField.setText(String.format("%.2f", transaction.getNetAmount()));
        this.tagTextField.setText(transaction.getTag());
        this.createInputControls();
    }

    // EFFECTS: Validate field(s) with potential parse issue caused by human error
    @Override
    public void validateUserInput() throws DialogInputException {
        try {
            double _unused = Double.parseDouble(amountTextField.getText());
        } catch (NumberFormatException exception) {
            throw new DialogInputException("Validation Error", "Amount field contains invalid character(s)");
        }
    }

    // EFFECTS: Extract values from dialog text fields
    @Override
    public Transaction parseValuesFromDialogResult() {
        double amount = Double.parseDouble(amountTextField.getText());
        Transaction.TransactionType type
                = expenseRadioButton.isSelected()
                ? Transaction.TransactionType.EXPENSE : Transaction.TransactionType.INCOME;
        return new Transaction(amount, type, tagTextField.getText());
    }

    // EFFECTS: Configure input control view spacings and layout and add them to dialog view
    @Override
    public void createInputControls() {
        this.expenseRadioButton.setToggleGroup(this.radioGroup);
        this.incomeRadioButton.setToggleGroup(this.radioGroup);
        final VBox optionsFormField = new VBox(
                8,
                new Label("Type:"),
                new HBox(8, this.expenseRadioButton, this.incomeRadioButton)
        );
        final HBox amountFormField = new HBox(8, new Label("Amount:"), this.amountTextField);
        final HBox tagFormField = new HBox(8, new Label("Tag:"), this.tagTextField);
        this.setupAndAddSubviews(optionsFormField, amountFormField, tagFormField);
    }
}
