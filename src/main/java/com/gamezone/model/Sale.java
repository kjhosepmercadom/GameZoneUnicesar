package com.gamezone.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Transaction registered at the store. A sale is made on a date by a customer,
 * attended by a seller, and contains one or more products. A product bought
 * several times appears several times in the product list.
 */
public class Sale {

    private String id;
    private LocalDate date;
    private Customer customer;
    private Seller seller;
    private List<Product> products;
    private String appliedPromotionName;
    private double discountAmount;
    private double extendedWarrantyCost;

    /**
     * Creates a sale with all its information.
     *
     * @param id       unique sale identifier
     * @param date     date of the sale
     * @param customer customer that made the purchase
     * @param seller   seller that attended the sale
     * @param products products bought; one entry per unit sold
     * @throws IllegalArgumentException if the sale has no products or lacks customer or seller
     */
    public Sale(String id, LocalDate date, Customer customer, Seller seller, List<Product> products) {
        if (customer == null || seller == null) {
            throw new IllegalArgumentException("La venta debe tener un cliente y un vendedor.");
        }
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("La venta debe contener al menos un producto.");
        }
        this.id = id;
        this.date = date;
        this.customer = customer;
        this.seller = seller;
        this.products = new ArrayList<>(products);
    }

    /**
     * Returns the unique sale identifier.
     *
     * @return the sale identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the date of the sale.
     *
     * @return the sale date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the customer that made the purchase.
     *
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Returns the seller that attended the sale.
     *
     * @return the seller
     */
    public Seller getSeller() {
        return seller;
    }

    /**
     * Returns the products bought in the sale.
     *
     * @return a copy of the product list
     */
    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    /**
     * Calculates the total of the sale by adding the price of every product bought.
     *
     * @return the sale total in Colombian pesos
     */
    public double calculateTotal() {
        double total = 0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }

    /**
     * Returns the name of the promotion applied to the sale.
     *
     * @return the promotion name, or {@code null} if no promotion was applied
     */
    public String getAppliedPromotionName() {
        return appliedPromotionName;
    }

    /**
     * Sets the name of the promotion applied to the sale.
     *
     * @param appliedPromotionName the promotion name, or {@code null} if none
     */
    public void setAppliedPromotionName(String appliedPromotionName) {
        this.appliedPromotionName = appliedPromotionName;
    }

    /**
     * Returns the discount granted by the applied promotion.
     *
     * @return the discount amount in Colombian pesos
     */
    public double getDiscountAmount() {
        return discountAmount;
    }

    /**
     * Sets the discount granted by the applied promotion.
     *
     * @param discountAmount the discount amount in Colombian pesos
     */
    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    /**
     * Returns the cost of the extended warranties bought in the sale.
     *
     * @return the extended warranty cost in Colombian pesos
     */
    public double getExtendedWarrantyCost() {
        return extendedWarrantyCost;
    }

    /**
     * Sets the cost of the extended warranties bought in the sale.
     *
     * @param extendedWarrantyCost the extended warranty cost in Colombian pesos
     */
    public void setExtendedWarrantyCost(double extendedWarrantyCost) {
        this.extendedWarrantyCost = extendedWarrantyCost;
    }

    /**
     * Calculates the final total of the sale: subtotal minus the discount plus
     * the cost of the extended warranties.
     *
     * @return the final total in Colombian pesos
     */
    public double calculateFinalTotal() {
        return calculateTotal() - discountAmount + extendedWarrantyCost;
    }

    /**
     * Checks whether the sale can still receive returns: a return is accepted
     * up to thirty calendar days after the sale date.
     *
     * @return {@code true} if today is within the return period
     */
    public boolean canBeReturned() {
        return !LocalDate.now().isAfter(date.plusDays(30));
    }

    /**
     * Builds a receipt in Spanish with the sale details: identifier, date,
     * customer, seller, products bought and total.
     *
     * @return the formatted receipt
     */
    public String generateReceipt() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("========================================\n");
        receipt.append("        GAMEZONE UNICESAR - RECIBO\n");
        receipt.append("========================================\n");
        receipt.append("Venta: ").append(id).append('\n');
        receipt.append("Fecha: ").append(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append('\n');
        receipt.append("Cliente: ").append(customer.getName())
                .append(" (").append(customer.getIdentification()).append(")\n");
        receipt.append("Vendedor: ").append(seller.getName())
                .append(" (").append(seller.getEmployeeCode()).append(")\n");
        receipt.append("----------------------------------------\n");
        for (Product product : products) {
            receipt.append(String.format("%-8s %-20s $%,12.0f%n", product.getId(), product.getTitle(), product.getPrice()));
        }
        receipt.append("----------------------------------------\n");
        receipt.append(String.format("Productos: %d%n", products.size()));
        receipt.append(String.format("Subtotal: $%,.0f%n", calculateTotal()));
        if (appliedPromotionName != null && discountAmount > 0) {
            receipt.append(String.format("Descuento (%s): -$%,.0f%n", appliedPromotionName, discountAmount));
        } else {
            receipt.append("Descuento: no aplica\n");
        }
        if (extendedWarrantyCost > 0) {
            receipt.append(String.format("Garantias extendidas: +$%,.0f%n", extendedWarrantyCost));
        }
        receipt.append(String.format("TOTAL: $%,.0f%n", calculateFinalTotal()));
        receipt.append("========================================");
        return receipt.toString();
    }

    /**
     * Calculates the share of the subtotal that was discounted by the
     * applied promotion. It is used to refund returned items proportionally.
     *
     * @return the discount rate between 0 and 1, zero when the subtotal is zero
     */
    public double calculateDiscountRate() {
        double subtotal = calculateTotal();
        if (subtotal <= 0) {
            return 0;
        }
        return discountAmount / subtotal;
    }
}
