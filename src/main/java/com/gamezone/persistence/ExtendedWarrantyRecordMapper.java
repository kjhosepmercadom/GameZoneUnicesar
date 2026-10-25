package com.gamezone.persistence;

import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Warranty;

import java.time.LocalDate;

/**
 * CSV mapper of extended warranties (discriminator {@code EXTENDED}).
 */
class ExtendedWarrantyRecordMapper implements WarrantyRecordMapper {

    /**
     * Returns the discriminator of extended warranties.
     *
     * @return {@code "EXTENDED"}
     */
    @Override
    public String getTypeCode() {
        return "EXTENDED";
    }

    /**
     * Tells whether the warranty is of this type.
     *
     * @param warranty the warranty to store
     * @return {@code true} for extended warranties
     */
    @Override
    public boolean supports(Warranty warranty) {
        return warranty instanceof ExtendedWarranty;
    }

    /**
     * Creates a warranty of this type.
     *
     * @param id        warranty identifier
     * @param product   covered product
     * @param sale      sale of the product
     * @param startDate first day of coverage
     * @return the warranty
     */
    @Override
    public Warranty create(String id, Product product, Sale sale, LocalDate startDate) {
        return new ExtendedWarranty(id, product, sale, startDate);
    }
}
