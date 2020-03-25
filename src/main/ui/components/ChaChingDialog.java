package ui.components;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;
import model.DialogInputException;

// Represents an abstract implementation of a generic dialog used in and specific to the gui application
public abstract class ChaChingDialog<T> extends Dialog<T> {

    // EFFECTS: instantiate dialog with buttons common to the subclass and setup exception handler
    public ChaChingDialog () {
        this.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        this.setupInputExceptionHandler();
    }

    public abstract void validateUserInput() throws DialogInputException;

    public abstract void createInputControls();

    // EFFECTS: setup an event filter for ok button to intercept normal closing event when except is thrown
    public void setupInputExceptionHandler() {
        final Button confirmButton = (Button) this.getDialogPane().lookupButton(ButtonType.OK);
        confirmButton.addEventFilter(ActionEvent.ACTION, e -> {
            try {
                this.validateUserInput();
            } catch (DialogInputException exception) {
                e.consume();
                showErrorDialog(exception.getErrorType(), exception.getErrorMessage());
            }
        });
    };

    public abstract T parseValuesFromDialogResult();

    // EFFECTS: display error message from caught exception
    protected void showErrorDialog(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    // EFFECTS: add subviews to the dialog pane and help subclass call its own value parsing implementation
    protected void setupAndAddSubviews(Node...nodes) {
        this.getDialogPane().setContent(new VBox(8, nodes));
        this.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                return parseValuesFromDialogResult();
            }
            return null;
        });
    }
}
