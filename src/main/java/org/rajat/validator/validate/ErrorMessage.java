package org.rajat.validator.validate;

public class ErrorMessage {

    private String message;
    private ErrorMessageType errorMessageType;

    public enum ErrorMessageType {
        WARN,
        ERROR
    }

    public ErrorMessage(ErrorMessageType errorMessageType, String message) {
        this.message = message;
        this.errorMessageType = errorMessageType;
    }

    public String getMessage() {
        return message;
    }

    public ErrorMessageType getErrorMessageType() {
        return errorMessageType;
    }

    @Override
    public String toString() {
        return "[" + this.errorMessageType + "]: " + this.message;
    }
}
