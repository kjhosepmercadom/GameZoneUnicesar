package com.gamezone.exceptions;

/**
 * Thrown when a data file cannot be read or written: corrupted file, denied
 * permission, full disk, among others. It keeps the original cause.
 */
public class PersistenceException extends GameZoneException {

    /**
     * Creates the exception.
     *
     * @param message description of the failed operation and file
     * @param cause   original exception, usually an {@link java.io.IOException}
     */
    public PersistenceException(String message, Throwable cause) {
        super(message, "GZ-500", cause);
    }
}
