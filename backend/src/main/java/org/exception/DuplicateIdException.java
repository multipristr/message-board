package org.exception;

public class DuplicateIdException extends RuntimeException {

    private static final long serialVersionUID = 6667894514461034699L;

    public DuplicateIdException(String message) {
        super(message);
    }

}
