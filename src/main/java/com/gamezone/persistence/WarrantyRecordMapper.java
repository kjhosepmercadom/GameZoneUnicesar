package com.gamezone.persistence;

import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Warranty;

import java.time.LocalDate;

/**
 * Converts one concrete warranty type to its CSV discriminator and back. The
 * warranty repository keeps one mapper per type, so a new warranty type is
 * supported by adding a mapper instead of modifying the repository.
 */
interface WarrantyRecordMapper {

    /**
     * Returns the discriminator written in the first column of the line.
     *
     * @return the type code of the warranty type
     */
    String getTypeCode();

    /**
     * Tells whether this mapper handles the given warranty.
     *
     * @param warranty the warranty to store
     * @return {@code true} if the warranty is of the type of this mapper
     */
    boolean supports(Warranty warranty);

    /**
     * Creates a warranty of the type of this mapper.
     *
     * @param id        warranty identifier
     * @param product   covered product, already resolved
     * @param sale      sale of the product, already resolved
     * @param startDate first day of coverage
     * @return the warranty
     */
    Warranty create(String id, Product product, Sale sale, LocalDate startDate);
}
