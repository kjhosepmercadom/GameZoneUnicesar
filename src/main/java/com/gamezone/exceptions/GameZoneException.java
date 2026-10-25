package com.gamezone.exceptions;

/**
 * Root of the exception hierarchy of GameZone Unicesar. It is unchecked so
 * that services and repositories do not need to declare it, while the user
 * interface can still catch every system error by its specific type.
 */
public abstract class GameZoneException extends RuntimeException {

    private final String errorCode;

    /**
     * Creates an exception with a message and the generic error code.
     *
     * @param message description of the error
     */
    public GameZoneException(String message) {
        this(message, "GZ-000");
    }

    /**
     * Creates an exception with a message and an error code.
     *
     * @param message   description of the error
     * @param errorCode code that identifies the type of error
     */
    public GameZoneException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Creates an exception with a message, an error code and its original cause.
     *
     * @param message   description of the error
     * @param errorCode code that identifies the type of error
     * @param cause     original exception
     */
    protected GameZoneException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Returns the code that identifies the type of error.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }
}
