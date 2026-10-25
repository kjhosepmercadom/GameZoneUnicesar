package com.gamezone.validation;

import com.gamezone.exceptions.BusinessRuleException;
import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.exceptions.ResourceNotFoundException;
import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;

import java.util.List;

/**
 * Validations of the sales module. They are invoked by the services before
 * executing a business operation and throw the custom exceptions when a rule
 * is not met. Validators never access the persistence layer.
 */
public final class SaleValidator {

    private SaleValidator() {
    }

    /**
     * Validates that a sale requests at least one item.
     *
     * @param itemIds identifiers of the items requested
     * @throws BusinessRuleException if the list is null or empty
     */
    public static void validateHasItems(List<String> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) {
            throw new BusinessRuleException("La venta debe contener al menos un producto.");
        }
    }

    /**
     * Validates the data needed to create a sale: customer and seller must be
     * present and the product list must not be empty.
     *
     * @param customer customer of the sale
     * @param seller   seller of the sale
     * @param products products of the sale
     * @throws InvalidDataException  if the customer or the seller is missing
     * @throws BusinessRuleException if the sale has no products
     */
    public static void validateSaleData(Customer customer, Seller seller, List<Product> products) {
        if (customer == null) {
            throw new InvalidDataException("cliente", "es obligatorio.");
        }
        if (seller == null) {
            throw new InvalidDataException("vendedor", "es obligatorio.");
        }
        if (products == null || products.isEmpty()) {
            throw new BusinessRuleException("La venta debe contener al menos un producto.");
        }
    }

    /**
     * Validates that a sale was found.
     *
     * @param sale   the sale found, possibly {@code null}
     * @param saleId the identifier that was searched
     * @throws ResourceNotFoundException if the sale is {@code null}
     */
    public static void validateSaleExists(Sale sale, String saleId) {
        if (sale == null) {
            throw new ResourceNotFoundException("venta", saleId);
        }
    }
}
