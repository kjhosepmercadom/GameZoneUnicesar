package com.gamezone.model;

/**
 * Abstract base class for every product sold by GameZone Unicesar.
 * It holds the information shared by all products: identifier, title,
 * unit price and quantity available in inventory.
 */
public abstract class Product {

    private String id;
    private String title;
    private double price;
    private int stock;

    /**
     * Creates a product with its common information.
     *
     * @param id    unique product identifier
     * @param title commercial title of the product
     * @param price unit price in Colombian pesos
     * @param stock quantity available in inventory
     */
    public Product(String id, String title, double price, int stock) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.stock = stock;
    }

    /**
     * Returns the unique product identifier.
     *
     * @return the product identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique product identifier.
     *
     * @param id the new product identifier
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the commercial title of the product.
     *
     * @return the product title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the commercial title of the product.
     *
     * @param title the new product title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the unit price of the product.
     *
     * @return the unit price in Colombian pesos
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the unit price of the product.
     *
     * @param price the new unit price in Colombian pesos
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Returns the quantity available in inventory.
     *
     * @return the available stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the quantity available in inventory.
     *
     * @param stock the new available stock
     */
    public void setStock(int stock) {
        this.stock = stock;
    }
}
