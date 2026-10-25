package com.gamezone.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    /**
     * Returns the name of the warranty type shown to the user.
     *
     * @return the warranty type name
     */
    public abstract String getWarrantyType();

    /**
     * Returns the additional cost this warranty adds to the sale.
     *
     * @return the additional cost in Colombian pesos
     */
    public abstract double getAdditionalCost();

    /**
     * Checks whether the warranty covers the given date.
     *
     * @param date the date to check
     * @return {@code true} if the date is between the start and end dates, both included
     */
    public boolean isActive(LocalDate date) {
        return date != null && !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * Builds a certificate in Spanish with the warranty details.
     *
     * @return the formatted warranty certificate
     */
    public String generateWarrantyCertificate() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder certificate = new StringBuilder();
        certificate.append("----------------------------------------\n");
        certificate.append("CERTIFICADO DE GARANTIA ").append(id).append('\n');
        certificate.append("Tipo: ").append(getWarrantyType()).append('\n');
        certificate.append("Producto: ").append(product.getId()).append(" - ").append(product.getTitle()).append('\n');
        certificate.append("Venta: ").append(sale.getId())
                .append(" | Cliente: ").append(sale.getCustomer().getName()).append('\n');
        certificate.append("Vigencia: ").append(startDate.format(format))
                .append(" a ").append(endDate.format(format))
                .append(" (").append(getDurationInMonths()).append(" meses)\n");
        certificate.append(String.format("Costo adicional: $%,.0f%n", getAdditionalCost()));
        certificate.append("Estado: ").append(isActive(LocalDate.now()) ? "VIGENTE" : "VENCIDA").append('\n');
        certificate.append("----------------------------------------");
        return certificate.toString();
    }
}
