package com.gamezone.validation;

import com.gamezone.exceptions.BusinessRuleException;
import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.exceptions.ResourceNotFoundException;
import com.gamezone.model.Product;

/**
 * Validations of the product module. They are invoked by the services before
 * executing a business operation and throw the custom exceptions when a rule
 * is not met. Validators never access the persistence layer.
 */
public final class ProductValidator {

    private ProductValidator() {
    }

    /**
     * Validates the common data of a product.
     *
     * @param id    product identifier, required
     * @param title product title, required
     * @param price unit price, must be positive
     * @param stock initial stock, must not be negative
     * @throws InvalidDataException if any value is invalid
     */
    public static void validateProductData(String id, String title, double price, int stock) {
        requireText(id, "codigo");
        requireText(title, "titulo");
        if (price <= 0) {
            throw new InvalidDataException("precio", "debe ser mayor que cero.");
        }
        if (stock < 0) {
            throw new InvalidDataException("cantidad en inventario", "no puede ser negativo.");
        }
    }

    /**
     * Validates that a product has enough stock for the requested quantity.
     *
     * @param product           the product to check
     * @param requestedQuantity the quantity requested
     * @throws BusinessRuleException if the stock is insufficient
     */
    public static void validateStockAvailability(Product product, int requestedQuantity) {
        if (!product.hasEnoughStock(requestedQuantity)) {
            throw new BusinessRuleException("Stock insuficiente para " + product.getTitle()
                    + " (disponible: " + product.getStock() + ", solicitado: " + requestedQuantity + ").");
        }
    }

    /**
     * Validates that a product was found.
     *
     * @param product   the product found, possibly {@code null}
     * @param productId the identifier that was searched
     * @throws ResourceNotFoundException if the product is {@code null}
     */
    public static void validateProductExists(Product product, String productId) {
        if (product == null) {
            throw new ResourceNotFoundException("producto", productId);
        }
    }

    /**
     * Validates that a text field has content.
     *
     * @param value     the value to check
     * @param fieldName name of the field shown to the user
     * @throws InvalidDataException if the value is null or empty
     */
    public static void requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidDataException(fieldName, "es obligatorio.");
        }
    }
}
