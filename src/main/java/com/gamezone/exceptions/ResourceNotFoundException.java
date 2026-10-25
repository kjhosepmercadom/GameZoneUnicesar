package com.gamezone.exceptions;

/**
 * Thrown when a resource (product, customer, seller, sale...) is searched by
 * its identifier and does not exist in the system.
 */
public class ResourceNotFoundException extends GameZoneException {

    /**
     * Creates the exception with a descriptive message in Spanish.
     *
     * @param resourceType name of the resource, for example "producto"
     * @param resourceId   identifier that was searched
     */
    public ResourceNotFoundException(String resourceType, String resourceId) {
        super("El " + resourceType + " con identificador " + resourceId + " no fue encontrado.", "GZ-404");
    }
}
