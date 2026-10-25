package com.gamezone.model;

import java.time.LocalDate;

/**
 * Abstract base class for the warranties granted on the products sold. Every
 * warranty belongs to a product of a sale and is valid from the sale date
 * until an end date that depends on the duration of each warranty type.
 */
public abstract class Warranty {

    private String id;
    private Product product;
    private Sale sale;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Creates a warranty. The end date is calculated automatically from the
     * start date and the duration defined by the concrete warranty type.
     *
     * @param id        unique warranty identifier
     * @param product   product covered by the warranty
     * @param sale      sale in which the product was bought
     * @param startDate first day of coverage (the sale date)
     */
    public Warranty(String id, Product product, Sale sale, LocalDate startDate) {
        this.id = id;
        this.product = product;
        this.sale = sale;
        this.startDate = startDate;
        this.endDate = startDate.plusMonths(getDurationInMonths());
    }

    /**
     * Returns the warranty identifier.
     *
     * @return the identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the product covered by the warranty.
     *
     * @return the covered product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Returns the sale in which the covered product was bought.
     *
     * @return the sale
     */
    public Sale getSale() {
        return sale;
    }

    /**
     * Returns the first day of coverage.
     *
     * @return the start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Returns the last day of coverage.
     *
     * @return the end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Returns the duration of the warranty in months.
     *
     * @return the duration in months
     */
    public abstract int getDurationInMonths();
}
