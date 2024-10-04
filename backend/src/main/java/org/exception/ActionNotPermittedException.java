package org.exception;

public class ActionNotPermittedException extends RuntimeException {

    private static final long serialVersionUID = 8561286390827197L;

    public ActionNotPermittedException(String message) {
        super(message);
    }

}
