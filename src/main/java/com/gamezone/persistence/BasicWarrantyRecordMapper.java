package com.gamezone.persistence;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Warranty;

import java.time.LocalDate;

/**
 * CSV mapper of basic warranties (discriminator {@code BASIC}).
 */
class BasicWarrantyRecordMapper implements WarrantyRecordMapper {

    /**
     * Returns the discriminator of basic warranties.
     *
     * @return {@code "BASIC"}
     */
    @Override
    public String getTypeCode() {
        return "BASIC";
    }

    /**
     * Tells whether the warranty is of this type.
     *
     * @param warranty the warranty to store
     * @return {@code true} for basic warranties
     */
    @Override
    public boolean supports(Warranty warranty) {
        return warranty instanceof BasicWarranty;
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
        return new BasicWarranty(id, product, sale, startDate);
    }
}
