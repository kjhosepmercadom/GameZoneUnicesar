package com.gamezone.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Return of one or more products bought in a previous sale. A return can be
 * partial: it contains only the products the customer gives back, one entry
 * per returned unit.
 */
public class Return {

    private String id;
    private LocalDate date;
    private Sale sale;
    private List<Product> returnedProducts;
    private String reason;
    private double refundAmount;

    /**
     * Creates a return. The refund amount starts at zero and is set by
     * {@link #calculateRefundAmount()}.
     *
     * @param id               unique return identifier
     * @param date             date of the return
     * @param sale             original sale the products belong to
     * @param returnedProducts products given back, one entry per unit
     * @param reason           reason given by the customer
     */
    public Return(String id, LocalDate date, Sale sale, List<Product> returnedProducts, String reason) {
        this.id = id;
        this.date = date;
        this.sale = sale;
        this.returnedProducts = new ArrayList<>(returnedProducts);
        this.reason = reason;
    }

    /**
     * Returns the return identifier.
     *
     * @return the identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the date of the return.
     *
     * @return the return date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the original sale of the returned products.
     *
     * @return the original sale
     */
    public Sale getSale() {
        return sale;
    }

    /**
     * Returns the products given back.
     *
     * @return a copy of the returned products
     */
    public List<Product> getReturnedProducts() {
        return new ArrayList<>(returnedProducts);
    }

    /**
     * Returns the reason of the return.
     *
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * Returns the amount refunded to the customer.
     *
     * @return the refund amount in Colombian pesos
     */
    public double getRefundAmount() {
        return refundAmount;
    }

    /**
     * Calculates the refund as the sum of the prices of the returned products
     * and stores it in the return.
     *
     * @return the calculated refund amount
     */
    public double calculateRefundAmount() {
        double total = 0;
        for (Product product : returnedProducts) {
            total += product.getPrice();
        }
        refundAmount = total;
        return refundAmount;
    }

    /**
     * Builds a receipt in Spanish with the details of the return.
     *
     * @return the formatted return receipt
     */
    public String generateReturnReceipt() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        StringBuilder receipt = new StringBuilder();
        receipt.append("========================================\n");
        receipt.append("     GAMEZONE UNICESAR - DEVOLUCION\n");
        receipt.append("========================================\n");
        receipt.append("Devolucion: ").append(id).append('\n');
        receipt.append("Fecha: ").append(date.format(format)).append('\n');
        receipt.append("Venta original: ").append(sale.getId())
                .append(" (").append(sale.getDate().format(format)).append(")\n");
        receipt.append("Cliente: ").append(sale.getCustomer().getName()).append('\n');
        receipt.append("Motivo: ").append(reason).append('\n');
        receipt.append("----------------------------------------\n");
        for (Product product : returnedProducts) {
            receipt.append(String.format("%-8s %-20s $%,12.0f%n", product.getId(), product.getTitle(), product.getPrice()));
        }
        receipt.append("----------------------------------------\n");
        receipt.append(String.format("MONTO REEMBOLSADO: $%,.0f%n", refundAmount));
        receipt.append("========================================");
        return receipt.toString();
    }
}
