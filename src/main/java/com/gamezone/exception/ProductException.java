package com.gamezone.exception;

/**
 * Custom exception to handle product domain business rule violations.
 *
 * @author Dair
 */
public class ProductException extends RuntimeException {

    /**
     * Constructs a new ProductException with the specified detail message.
     *
     * @param message detailed description of the domain error
     */
    public ProductException(String message) {
        super(message);
    }

    /**
     * Constructs a new ProductException with the specified detail message and cause.
     *
     * @param message detailed description of the domain error
     * @param cause   root cause exception
     */
    public ProductException(String message, Throwable cause) {
        super(message, cause);
    }
}