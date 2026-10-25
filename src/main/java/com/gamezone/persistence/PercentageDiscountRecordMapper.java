package com.gamezone.persistence;

import com.gamezone.model.PercentageDiscount;
import com.gamezone.model.Promotion;

import java.time.LocalDate;

/**
 * CSV mapper of percentage promotions: {@code PERCENTAGE;id;name;startDate;endDate;...}.
 */
class PercentageDiscountRecordMapper implements RecordMapper<Promotion> {

    private static final String SEPARATOR = ";";

    /**
     * Returns the discriminator of percentage promotions.
     *
     * @return {@code "PERCENTAGE"}
     */
    @Override
    public String getTypeCode() {
        return "PERCENTAGE";
    }

    /**
     * Tells whether the promotion is of this type.
     *
     * @param value the promotion to store
     * @return {@code true} for percentage promotions
     */
    @Override
    public boolean supports(Promotion value) {
        return value instanceof PercentageDiscount;
    }

    /**
     * Builds the columns of the promotion.
     *
     * @param value the promotion to store
     * @return the columns separated by semicolons
     */
    @Override
    public String toColumns(Promotion value) {
        PercentageDiscount percentage = (PercentageDiscount) value;
        return CsvFile.clean(percentage.getId()) + SEPARATOR + CsvFile.clean(percentage.getName()) + SEPARATOR
                + percentage.getStartDate() + SEPARATOR + percentage.getEndDate() + SEPARATOR + percentage.getPercentage();
    }

    /**
     * Rebuilds the promotion from the columns of a line.
     *
     * @param fields every column of the line
     * @return the promotion
     */
    @Override
    public Promotion fromFields(String[] fields) {
        return new PercentageDiscount(fields[1], fields[2], LocalDate.parse(fields[3]), LocalDate.parse(fields[4]),
                Double.parseDouble(fields[5]));
    }
}
