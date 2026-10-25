package com.gamezone.exceptions;

/**
 * Thrown when an input value does not meet the format, range or type
 * criteria: negative price, negative quantity, empty text, among others.
 */
public class InvalidDataException extends GameZoneException {

    /**
     * Creates the exception with a descriptive message in Spanish.
     *
     * @param fieldName name of the invalid field
     * @param problem   description of the problem
     */
    public InvalidDataException(String fieldName, String problem) {
        super("El campo " + fieldName + " " + problem, "GZ-400");
    }
}
