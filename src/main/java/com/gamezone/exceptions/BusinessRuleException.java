package com.gamezone.exceptions;

/**
 * Thrown when an operation violates a business rule: insufficient stock,
 * duplicated customer, sale without products, among others.
 */
public class BusinessRuleException extends GameZoneException {

    /**
     * Creates the exception.
     *
     * @param message description of the violated rule
     */
    public BusinessRuleException(String message) {
        super(message, "GZ-409");
    }
}
