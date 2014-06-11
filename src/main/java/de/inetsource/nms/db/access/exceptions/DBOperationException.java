package de.inetsource.nms.db.access.exceptions;

public class DBOperationException extends Exception {
    public DBOperationException(String message, Throwable cause) {
        super(message, cause);
    }
    public DBOperationException(String message) {
        super(message);
    }
}
