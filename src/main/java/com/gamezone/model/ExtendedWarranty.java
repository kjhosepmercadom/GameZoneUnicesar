package com.gamezone.model;

import java.time.LocalDate;

/**
 * Extended warranty that the customer can buy for a console. It covers
 * factory defects and accidental damage for twelve months and costs ten
 * percent of the product price.
 */
public class ExtendedWarranty extends Warranty {

    private static final double COST_RATE = 0.10;

    /**
     * Creates an extended warranty.
     *
     * @param id        unique warranty identifier
     * @param product   product covered by the warranty
     * @param sale      sale in which the product was bought
     * @param startDate first day of coverage
     */
    public ExtendedWarranty(String id, Product product, Sale sale, LocalDate startDate) {
        super(id, product, sale, startDate);
    }

    /**
     * Returns the duration of the extended warranty.
     *
     * @return twelve months
     */
    @Override
    public int getDurationInMonths() {
        return 12;
    }

    /**
     * Returns the name of the extended warranty type.
     *
     * @return the warranty type name
     */
    @Override
    public String getWarrantyType() {
        return "Garantia Extendida";
    }

    /**
     * The extended warranty costs ten percent of the price of the covered product.
     *
     * @return the additional cost in Colombian pesos
     */
    @Override
    public double getAdditionalCost() {
        return getProduct().getPrice() * COST_RATE;
    }
}
