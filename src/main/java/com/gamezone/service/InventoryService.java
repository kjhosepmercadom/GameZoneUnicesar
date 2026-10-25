package com.gamezone.service;

import com.gamezone.model.Product;

/**
 * Abstraction of an inventory of sellable items. The sales and return
 * services work with this interface instead of the concrete product or
 * accessory services, so a new kind of item only needs a new implementation.
 */
public interface InventoryService {

    /**
     * Finds an item of this inventory by its identifier.
     *
     * @param id the item identifier
     * @return the item found, or {@code null} if it is not part of this inventory
     */
    Product findById(String id);

    /**
     * Deducts a sold quantity from the stock of an item and persists the change.
     *
     * @param itemId   the item identifier
     * @param quantity the quantity sold
     */
    void updateStock(String itemId, int quantity);

    /**
     * Adds returned units back to the stock of an item and persists the change.
     *
     * @param itemId   the item identifier
     * @param quantity the quantity returned
     */
    void restoreStock(String itemId, int quantity);
}
