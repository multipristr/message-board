package org.exception;

public class MissingEntityException extends RuntimeException {

    private static final long serialVersionUID = 6145516761098889113L;

    public MissingEntityException(String message) {
        super(message);
    }

}
