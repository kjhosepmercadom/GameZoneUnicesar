package com.gamezone.model;

import java.time.LocalDate;

/**
 * Promotion that applies a percentage discount only to the products of a
 * target category of the sale.
 */
public class CategoryDiscount extends Promotion {

    private double percentage;
    private String targetCategory;

    /**
     * Creates a category promotion.
     *
     * @param id             unique promotion identifier
     * @param name           commercial name
     * @param startDate      first valid day
     * @param endDate        last valid day
     * @param percentage     discount percentage between 0 and 100
     * @param targetCategory category that receives the discount: VIDEOGAME or CONSOLE
     */
    public CategoryDiscount(String id, String name, LocalDate startDate, LocalDate endDate,
                            double percentage, String targetCategory) {
        super(id, name, startDate, endDate);
        this.percentage = percentage;
        this.targetCategory = targetCategory;
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
     * Returns the category that receives the discount.
     *
     * @return the target category
     */
    public String getTargetCategory() {
        return targetCategory;
    }

    /**
     * Sets the category that receives the discount.
     *
     * @param targetCategory the new target category
     */
    public void setTargetCategory(String targetCategory) {
        this.targetCategory = targetCategory;
    }

    /**
     * Adds the prices of the products of the target category and applies the
     * percentage only to that sum.
     *
     * @param sale the sale to evaluate
     * @return the discount amount, zero if the sale has no product of the category
     */
    @Override
    public double calculateDiscount(Sale sale) {
        double categoryTotal = 0;
        for (Product product : sale.getProducts()) {
            if (belongsToTargetCategory(product)) {
                categoryTotal += product.getPrice();
            }
        }
        return categoryTotal * percentage / 100;
    }

    /**
     * Builds the description of the category promotion.
     *
     * @return the promotion description
     */
    @Override
    public String getDescription() {
        return String.format("[Categoria] %s | %.1f%% en %s", describePeriod(), percentage, targetCategory);
    }

    private boolean belongsToTargetCategory(Product product) {
        if ("VIDEOGAME".equalsIgnoreCase(targetCategory)) {
            return product instanceof VideoGame;
        }
        if ("CONSOLE".equalsIgnoreCase(targetCategory)) {
            return product instanceof Console;
        }
        return false;
    }
}
