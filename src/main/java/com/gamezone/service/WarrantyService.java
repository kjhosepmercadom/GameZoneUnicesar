package com.gamezone.service;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Warranty;
import com.gamezone.persistence.WarrantyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Applies the business rules of the warranty module: assignment of basic and
 * extended warranties, validity queries and warranties about to expire.
 */
public class WarrantyService {

    private final WarrantyRepository warrantyRepository;
    private final List<Warranty> warranties;

    /**
     * Creates the service. The stored warranties are loaded with
     * {@link #loadWarranties(List)} once the sales they reference are available.
     *
     * @param warrantyRepository repository used to persist the warranties
     */
    public WarrantyService(WarrantyRepository warrantyRepository) {
        this.warrantyRepository = warrantyRepository;
        this.warranties = new ArrayList<>();
    }

    /**
     * Loads the stored warranties, resolving their references against the
     * given sales. Any warranty previously kept in memory is replaced.
     *
     * @param sales the sales already loaded by the system
     */
    public void loadWarranties(List<Sale> sales) {
        warranties.clear();
        warranties.addAll(warrantyRepository.loadAll(sales));
    }

    /**
     * Creates and stores the automatic basic warranty of a product.
     *
     * @param product   product covered by the warranty
     * @param sale      sale in which the product was bought
     * @param startDate first day of coverage
     * @return the created basic warranty
     */
    public BasicWarranty assignBasicWarranty(Product product, Sale sale, LocalDate startDate) {
        BasicWarranty warranty = new BasicWarranty(nextWarrantyId(), product, sale, startDate);
        warranties.add(warranty);
        warrantyRepository.saveAll(warranties);
        return warranty;
    }

    /**
     * Creates and stores an extended warranty requested by the customer.
     *
     * @param product   product covered by the warranty
     * @param sale      sale in which the product was bought
     * @param startDate first day of coverage
     * @return the created extended warranty
     */
    public ExtendedWarranty assignExtendedWarranty(Product product, Sale sale, LocalDate startDate) {
        ExtendedWarranty warranty = new ExtendedWarranty(nextWarrantyId(), product, sale, startDate);
        warranties.add(warranty);
        warrantyRepository.saveAll(warranties);
        return warranty;
    }

    /**
     * Finds the warranty of a product within a specific sale. When the product
     * has several warranties in the sale, the extended one is preferred.
     *
     * @param productId identifier of the product
     * @param saleId    identifier of the sale
     * @return the warranty found, or {@code null} if it does not exist
     */
    public Warranty findWarrantyByProduct(String productId, String saleId) {
        Warranty found = null;
        for (Warranty warranty : warranties) {
            if (warranty.getProduct().getId().equalsIgnoreCase(productId.trim())
                    && warranty.getSale().getId().equalsIgnoreCase(saleId.trim())) {
                if (warranty instanceof ExtendedWarranty) {
                    return warranty;
                }
                if (found == null) {
                    found = warranty;
                }
            }
        }
        return found;
    }

    /**
     * Returns every registered warranty.
     *
     * @return a copy of the warranty list
     */
    public List<Warranty> listAllWarranties() {
        return new ArrayList<>(warranties);
    }

    /**
     * Returns the warranties that are valid on the current date.
     *
     * @return the active warranties, possibly empty
     */
    public List<Warranty> listActiveWarranties() {
        LocalDate today = LocalDate.now();
        List<Warranty> active = new ArrayList<>();
        for (Warranty warranty : warranties) {
            if (warranty.isActive(today)) {
                active.add(warranty);
            }
        }
        return active;
    }

    /**
     * Returns the active warranties that end within the next days.
     *
     * @param daysAhead number of days to look ahead, zero or positive
     * @return the warranties about to expire, possibly empty
     * @throws IllegalArgumentException if the number of days is negative
     */
    public List<Warranty> listWarrantiesExpiringSoon(int daysAhead) {
        if (daysAhead < 0) {
            throw new IllegalArgumentException("Los dias de anticipacion no pueden ser negativos.");
        }
        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusDays(daysAhead);
        List<Warranty> expiring = new ArrayList<>();
        for (Warranty warranty : warranties) {
            if (warranty.isActive(today) && !warranty.getEndDate().isAfter(limit)) {
                expiring.add(warranty);
            }
        }
        return expiring;
    }

    private String nextWarrantyId() {
        int max = 0;
        for (Warranty warranty : warranties) {
            try {
                max = Math.max(max, Integer.parseInt(warranty.getId().substring(1)));
            } catch (RuntimeException e) {
                // identifiers that do not follow the W0000 pattern are ignored
            }
        }
        return String.format("W%04d", max + 1);
    }
}
