package model;

public class DialogInputException extends Exception {

    private String errorType;
    private String errorMessage;

    public DialogInputException(String errorType, String errorMessage) {
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }

    public String getErrorType() {
        return errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
