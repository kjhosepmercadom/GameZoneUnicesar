package com.gamezone.model;

import java.time.LocalDate;

/**
 * Basic warranty generated automatically for every console sold. It covers
 * only factory defects for six months and has no additional cost.
 */
public class BasicWarranty extends Warranty {

    /**
     * Creates a basic warranty.
     *
     * @param id        unique warranty identifier
     * @param product   product covered by the warranty
     * @param sale      sale in which the product was bought
     * @param startDate first day of coverage
     */
    public BasicWarranty(String id, Product product, Sale sale, LocalDate startDate) {
        super(id, product, sale, startDate);
    }

    /**
     * Returns the duration of the basic warranty.
     *
     * @return six months
     */
    @Override
    public int getDurationInMonths() {
        return 6;
    }

    /**
     * Returns the name of the basic warranty type.
     *
     * @return the warranty type name
     */
    @Override
    public String getWarrantyType() {
        return "Garantia Basica";
    }

    /**
     * The basic warranty has no additional cost for the customer.
     *
     * @return zero
     */
    @Override
    public double getAdditionalCost() {
        return 0.0;
    }
}
