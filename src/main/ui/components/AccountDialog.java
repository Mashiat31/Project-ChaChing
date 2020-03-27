package ui.components;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import model.Account;
import model.DialogInputException;

// Represents an account dialog for adding and editing account info
public class AccountDialog extends ChaChingDialog<Account> {

    private final TextField accountNameTextField = new TextField();
    private final TextField budgetTextField = new TextField();

    // EFFECTS: Constructor to instantiate new account dialog for creating new account
    public AccountDialog() {
        this.setTitle("New Account");
        this.setHeaderText("Enter details for your account information");
        this.createInputControls();
    }
    // EFFECTS: Constructor to instantiate new account dialog for editing a given account

    public AccountDialog(Account account) {
        this.setTitle("Edit Account");
        this.setHeaderText("Update your account information");
        this.accountNameTextField.setText(account.getDescription());
        this.budgetTextField.setText(String.format("%.2f", account.getBudget()));
        this.createInputControls();
    }

    // EFFECTS: Overrides super class method's to parse dialog result to Account type object
    @Override
    public Account parseValuesFromDialogResult() {
        return new Account(
                this.accountNameTextField.getText(),
                Double.parseDouble(budgetTextField.getText())
        );
    }

    // EFFECTS: Set up input controls and layout for the necessary fields in dialog
    @Override
    public void createInputControls() {
        final Label accountNameLabel = new Label("Account Name:");
        final HBox accountNameFormField = new HBox(8, accountNameLabel, this.accountNameTextField);
        final Label budgetNameLabel = new Label("Budget:");
        final HBox budgetFormField = new HBox(8, budgetNameLabel, this.budgetTextField);
        Platform.runLater(accountNameTextField::requestFocus);
        this.setupAndAddSubviews(
                accountNameFormField,
                budgetFormField
        );
    }

    // EFFECTS: Validate field(s) with potential parse issue caused by human error
    @Override
    public void validateUserInput() throws DialogInputException {
        try {
            double unused = Double.parseDouble(budgetTextField.getText());
        } catch (NumberFormatException exception) {
            throw new DialogInputException("Validation Error", "Budget field contains invalid character(s)");
        }
    }
}
