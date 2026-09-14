package com.gamezone.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a sale transaction in GameZone, involving a client,
 * a vendor, and one or more purchased products.
 */
public class Sale {

    private String id;
    private LocalDate date;
    private Client client;
    private Vendor vendor;
    private List<Product> products;

    /**
     * Creates a new sale transaction.
     *
     * @param id     unique identifier of the sale
     * @param date   date when the sale was made
     * @param client the client who made the purchase
     * @param vendor the vendor who processed the sale
     * @throws IllegalArgumentException if client or vendor is null
     */
    public Sale(String id, LocalDate date, Client client, Vendor vendor) {
        if (client == null || vendor == null) {
            throw new IllegalArgumentException("Client and Vendor cannot be null");
        }
        this.id = id;
        this.date = date;
        this.client = client;
        this.vendor = vendor;
        this.products = new ArrayList<>();
    }

    /**
     * Adds a product to this sale.
     *
     * @param product the product to add to the sale list
     */
    public void addProduct(Product product) {
        if (product != null) {
            this.products.add(product);
        }
    }

    /**
     * Calculates the total price of the sale by summing the price
     * of every product included.
     *
     * @return the total amount of the sale
     */
    public double calculateTotal() {
        double total = 0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }

    /**
     * Gets the unique identifier of the sale.
     *
     * @return the sale ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the date of the sale.
     *
     * @return the sale date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Gets the client associated with the sale.
     *
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    /**
     * Gets the vendor who processed the sale.
     *
     * @return the vendor
     */
    public Vendor getVendor() {
        return vendor;
    }

    /**
     * Gets a shallow copy of the list of products in the sale to maintain encapsulation.
     *
     * @return a new list containing the products
     */
    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    /**
     * Compares this sale with another object for equality based on its unique identifier.
     *
     * @param o the object to compare
     * @return true if both sales share the same ID, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sale sale = (Sale) o;
        return Objects.equals(id, sale.id);
    }

    /**
     * Returns the hash code based on the sale ID.
     *
     * @return hash code value for this sale
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
