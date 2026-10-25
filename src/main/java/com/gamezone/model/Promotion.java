package com.gamezone.model;

import java.time.LocalDate;

/**
 * Abstract base class for the promotional campaigns of the store. Every
 * promotion has an identifier, a name and a validity period; each concrete
 * type defines its own discount calculation strategy.
 */
public abstract class Promotion {

    private String id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Creates a promotion with its common information.
     *
     * @param id        unique promotion identifier
     * @param name      commercial name shown in the receipts
     * @param startDate first day the promotion is valid
     * @param endDate   last day the promotion is valid
     */
    public Promotion(String id, String name, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Returns the promotion identifier.
     *
     * @return the identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the promotion identifier.
     *
     * @param id the new identifier
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the commercial name of the promotion.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the commercial name of the promotion.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the first day the promotion is valid.
     *
     * @return the start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Sets the first day the promotion is valid.
     *
     * @param startDate the new start date
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns the last day the promotion is valid.
     *
     * @return the end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Sets the last day the promotion is valid.
     *
     * @param endDate the new end date
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Checks whether the promotion is valid on a given date. Both limits of
     * the validity period are included.
     *
     * @param date the date to check
     * @return {@code true} if the date is within the validity period
     */
    public boolean isActive(LocalDate date) {
        return date != null && !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * Calculates the discount, in Colombian pesos, that this promotion would
     * grant to the given sale. Each promotion type implements its own rule.
     *
     * @param sale the sale to evaluate
     * @return the discount amount, zero if the promotion does not apply
     */
    public abstract double calculateDiscount(Sale sale);

    /**
     * Builds a description of the promotion including its specific rule.
     *
     * @return the promotion description
     */
    public abstract String getDescription();

    /**
     * Builds the common part of the description shared by every promotion type.
     *
     * @return the identifier, name and validity period
     */
    protected String describePeriod() {
        return String.format("%s - %s | Vigencia: %s a %s", id, name, startDate, endDate);
    }
}
