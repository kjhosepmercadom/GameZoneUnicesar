package com.gamezone.model;

import java.time.LocalDate;

/**
 * Promotion that applies a percentage discount over the subtotal of the sale.
 */
public class PercentageDiscount extends Promotion {

    private double percentage;

    /**
     * Creates a percentage promotion.
     *
     * @param id         unique promotion identifier
     * @param name       commercial name
     * @param startDate  first valid day
     * @param endDate    last valid day
     * @param percentage discount percentage between 0 and 100
     */
    public PercentageDiscount(String id, String name, LocalDate startDate, LocalDate endDate, double percentage) {
        super(id, name, startDate, endDate);
        this.percentage = percentage;
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
     * Applies the percentage to the subtotal of the sale.
     *
     * @param sale the sale to evaluate
     * @return the discount amount
     */
    @Override
    public double calculateDiscount(Sale sale) {
        return sale.calculateTotal() * percentage / 100;
    }

    /**
     * Builds the description of the percentage promotion.
     *
     * @return the promotion description
     */
    @Override
    public String getDescription() {
        return String.format("[Porcentaje] %s | %.1f%% sobre el total", describePeriod(), percentage);
    }
}
