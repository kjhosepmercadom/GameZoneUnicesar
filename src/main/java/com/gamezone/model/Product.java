package com.gamezone.model;

import java.util.Objects;

/**
 * Abstract base class for every sellable product in GameZone.
 * Holds the attributes and behaviour common to all product types
 * (video games, consoles, and any future product category).
 *
 * @author Dair
 */
public abstract class Product {

    private String id;
    private String name;
    private double price;
    private int stock;

    /**
     * Creates a new product with its common attributes.
     *
     * @param id    unique identifier of the product
     * @param name  display name of the product
     * @param price unit price, must not be negative
     * @param stock available quantity, must not be negative
     */
    public Product(String id, String name, double price, int stock) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    /**
     * Returns the unique identifier of the product.
     *
     * @return the product id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the product.
     *
     * @param id the new product id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the display name of the product.
     *
     * @return the product name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the display name of the product.
     *
     * @param name the new product name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the unit price of the product.
     *
     * @return the product price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the unit price of the product.
     *
     * @param price the new price, must not be negative
     */
    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.price = price;
    }

    /**
     * Returns the available stock of the product.
     *
     * @return the current stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the available stock of the product.
     *
     * @param stock the new stock, must not be negative
     */
    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        this.stock = stock;
    }

    /**
     * Builds a human-readable description of the product, including
     * the attributes specific to its subtype. Each product subtype
     * must implement this on its own.
     *
     * @return the full description of the product
     */
    public abstract String getDescription();

    /**
     * Returns a string representation combining the description,
     * price and stock of the product.
     *
     * @return a readable summary of the product
     */
    @Override
    public String toString() {
        return getDescription() + " | price: " + price + " | stock: " + stock;
    }

    /**
     * Compares this product to another object based on their id.
     *
     * @param o the object to compare with
     * @return true if both products share the same id
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        Product other = (Product) o;
        return Objects.equals(id, other.id);
    }

    /**
     * Returns the hash code of this product, based on its id.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}