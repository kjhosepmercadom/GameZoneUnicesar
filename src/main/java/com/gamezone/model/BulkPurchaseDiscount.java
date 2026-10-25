package com.gamezone.model;

import java.time.LocalDate;

/**
 * Promotion that applies a percentage discount over the subtotal when the
 * sale includes a minimum number of products.
 */
public class BulkPurchaseDiscount extends Promotion {

    private int minimumQuantity;
    private double percentage;

    /**
     * Creates a bulk purchase promotion.
     *
     * @param id              unique promotion identifier
     * @param name            commercial name
     * @param startDate       first valid day
     * @param endDate         last valid day
     * @param minimumQuantity minimum number of products the sale must include
     * @param percentage      discount percentage between 0 and 100
     */
    public BulkPurchaseDiscount(String id, String name, LocalDate startDate, LocalDate endDate,
                                int minimumQuantity, double percentage) {
        super(id, name, startDate, endDate);
        this.minimumQuantity = minimumQuantity;
        this.percentage = percentage;
    }

    /**
     * Returns the minimum number of products required.
     *
     * @return the minimum quantity
     */
    public int getMinimumQuantity() {
        return minimumQuantity;
    }

    /**
     * Sets the minimum number of products required.
     *
     * @param minimumQuantity the new minimum quantity
     */
    public void setMinimumQuantity(int minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    /**
     * Returns the discount percentage.
     *
     * @return the percentage between 0 and 100
     */
    public double getPercentage() {
        return percentage;
    }

    /**
     * Sets the discount percentage.
     *
     * @param percentage the new percentage between 0 and 100
     */
    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    /**
     * Applies the percentage to the subtotal when the sale reaches the minimum
     * quantity of products.
     *
     * @param sale the sale to evaluate
     * @return the discount amount, zero if the minimum quantity is not reached
     */
    @Override
    public double calculateDiscount(Sale sale) {
        if (sale.getProducts().size() < minimumQuantity) {
            return 0;
        }
        return sale.calculateTotal() * percentage / 100;
    }

    /**
     * Builds the description of the bulk purchase promotion.
     *
     * @return the promotion description
     */
    @Override
    public String getDescription() {
        return String.format("[Volumen] %s | %.1f%% desde %d productos", describePeriod(), percentage, minimumQuantity);
    }
}
