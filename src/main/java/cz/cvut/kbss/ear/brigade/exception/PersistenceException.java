package cz.cvut.kbss.ear.brigade.exception;

import cz.cvut.kbss.ear.brigade.exception.EarException;

public class PersistenceException extends EarException {

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(Throwable cause) {
        super(cause);
    }
}
