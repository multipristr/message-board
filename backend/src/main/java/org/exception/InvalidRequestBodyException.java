package org.exception;

public class InvalidRequestBodyException extends RuntimeException {

    private static final long serialVersionUID = -6612422941549185161L;

    public InvalidRequestBodyException(String message) {
        super(message);
    }

}
