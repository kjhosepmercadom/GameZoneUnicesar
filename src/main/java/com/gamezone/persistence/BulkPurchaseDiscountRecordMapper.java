package com.gamezone.persistence;

import com.gamezone.model.BulkPurchaseDiscount;
import com.gamezone.model.Promotion;

import java.time.LocalDate;

/**
 * CSV mapper of bulk purchase promotions: {@code BULK;id;name;startDate;endDate;...}.
 */
class BulkPurchaseDiscountRecordMapper implements RecordMapper<Promotion> {

    private static final String SEPARATOR = ";";

    /**
     * Returns the discriminator of bulk purchase promotions.
     *
     * @return {@code "BULK"}
     */
    @Override
    public String getTypeCode() {
        return "BULK";
    }

    /**
     * Tells whether the promotion is of this type.
     *
     * @param value the promotion to store
     * @return {@code true} for bulk purchase promotions
     */
    @Override
    public boolean supports(Promotion value) {
        return value instanceof BulkPurchaseDiscount;
    }

    /**
     * Builds the columns of the promotion.
     *
     * @param value the promotion to store
     * @return the columns separated by semicolons
     */
    @Override
    public String toColumns(Promotion value) {
        BulkPurchaseDiscount bulk = (BulkPurchaseDiscount) value;
        return CsvFile.clean(bulk.getId()) + SEPARATOR + CsvFile.clean(bulk.getName()) + SEPARATOR
                + bulk.getStartDate() + SEPARATOR + bulk.getEndDate() + SEPARATOR + bulk.getMinimumQuantity() + SEPARATOR + bulk.getPercentage();
    }

    /**
     * Rebuilds the promotion from the columns of a line.
     *
     * @param fields every column of the line
     * @return the promotion
     */
    @Override
    public Promotion fromFields(String[] fields) {
        return new BulkPurchaseDiscount(fields[1], fields[2], LocalDate.parse(fields[3]), LocalDate.parse(fields[4]),
                Integer.parseInt(fields[5]), Double.parseDouble(fields[6]));
    }
}
