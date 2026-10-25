package com.gamezone.service;

import com.gamezone.model.Return;
import com.gamezone.model.Sale;

import java.time.LocalDate;
import java.util.List;

/**
 * Calculates the monthly totals of the balance report. It has a single
 * responsibility, the report arithmetic, separated from the registration of
 * returns, so the report can change without touching the return rules.
 */
public class MonthlyBalanceCalculator {

    /**
     * Adds the final total of the sales made in a month.
     *
     * @param sales the sales to consider
     * @param month month number, from 1 to 12
     * @param year  four digit year
     * @return the sales total in Colombian pesos
     */
    public double calculateSales(List<Sale> sales, int month, int year) {
        double salesTotal = 0;
        for (Sale sale : sales) {
            if (isInMonth(sale.getDate(), month, year)) {
                salesTotal += sale.calculateFinalTotal();
            }
        }
        return salesTotal;
    }

    /**
     * Adds the amount refunded in the returns registered in a month.
     *
     * @param returns the returns to consider
     * @param month   month number, from 1 to 12
     * @param year    four digit year
     * @return the returns total in Colombian pesos
     */
    public double calculateReturns(List<Return> returns, int month, int year) {
        double returnsTotal = 0;
        for (Return productReturn : returns) {
            if (isInMonth(productReturn.getDate(), month, year)) {
                returnsTotal += productReturn.getRefundAmount();
            }
        }
        return returnsTotal;
    }

    private boolean isInMonth(LocalDate date, int month, int year) {
        return date.getMonthValue() == month && date.getYear() == year;
    }
}
