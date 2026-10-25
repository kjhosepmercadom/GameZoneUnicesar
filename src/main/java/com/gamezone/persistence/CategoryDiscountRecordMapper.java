package com.gamezone.persistence;

import com.gamezone.model.CategoryDiscount;
import com.gamezone.model.Promotion;

import java.time.LocalDate;

/**
 * CSV mapper of category promotions: {@code CATEGORY;id;name;startDate;endDate;...}.
 */
class CategoryDiscountRecordMapper implements RecordMapper<Promotion> {

    private static final String SEPARATOR = ";";

    /**
     * Returns the discriminator of category promotions.
     *
     * @return {@code "CATEGORY"}
     */
    @Override
    public String getTypeCode() {
        return "CATEGORY";
    }

    /**
     * Tells whether the promotion is of this type.
     *
     * @param value the promotion to store
     * @return {@code true} for category promotions
     */
    @Override
    public boolean supports(Promotion value) {
        return value instanceof CategoryDiscount;
    }

    /**
     * Builds the columns of the promotion.
     *
     * @param value the promotion to store
     * @return the columns separated by semicolons
     */
    @Override
    public String toColumns(Promotion value) {
        CategoryDiscount category = (CategoryDiscount) value;
        return CsvFile.clean(category.getId()) + SEPARATOR + CsvFile.clean(category.getName()) + SEPARATOR
                + category.getStartDate() + SEPARATOR + category.getEndDate() + SEPARATOR + category.getPercentage() + SEPARATOR + category.getTargetCategory();
    }

    /**
     * Rebuilds the promotion from the columns of a line.
     *
     * @param fields every column of the line
     * @return the promotion
     */
    @Override
    public Promotion fromFields(String[] fields) {
        return new CategoryDiscount(fields[1], fields[2], LocalDate.parse(fields[3]), LocalDate.parse(fields[4]),
                Double.parseDouble(fields[5]), fields[6]);
    }
}
