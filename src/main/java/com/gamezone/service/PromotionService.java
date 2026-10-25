package com.gamezone.service;

import com.gamezone.model.BulkPurchaseDiscount;
import com.gamezone.model.CategoryDiscount;
import com.gamezone.model.PercentageDiscount;
import com.gamezone.model.Promotion;
import com.gamezone.model.Sale;
import com.gamezone.persistence.PromotionRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Applies the business rules of the promotion module: registration of the
 * three promotion types, validity queries and selection of the promotion that
 * grants the biggest discount to a sale.
 */
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final List<Promotion> promotions;

    /**
     * Creates the service and loads the stored promotions.
     *
     * @param promotionRepository repository used to persist the promotions
     */
    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
        this.promotions = promotionRepository.loadAll();
    }

    /**
     * Registers a promotion that discounts a percentage of the sale subtotal.
     *
     * @param id         unique identifier
     * @param name       commercial name
     * @param startDate  first valid day
     * @param endDate    last valid day
     * @param percentage discount percentage, greater than 0 and up to 100
     * @return the registered promotion
     * @throws IllegalArgumentException if any data is invalid or the identifier already exists
     */
    public PercentageDiscount registerPercentageDiscount(String id, String name, LocalDate startDate,
                                                         LocalDate endDate, double percentage) {
        validateCommonData(id, name, startDate, endDate, percentage);
        return save(new PercentageDiscount(id.trim(), name.trim(), startDate, endDate, percentage));
    }

    /**
     * Registers a promotion that discounts a percentage only of the products
     * of a target category.
     *
     * @param id             unique identifier
     * @param name           commercial name
     * @param startDate      first valid day
     * @param endDate        last valid day
     * @param percentage     discount percentage, greater than 0 and up to 100
     * @param targetCategory VIDEOGAME or CONSOLE
     * @return the registered promotion
     * @throws IllegalArgumentException if any data is invalid or the identifier already exists
     */
    public CategoryDiscount registerCategoryDiscount(String id, String name, LocalDate startDate,
                                                     LocalDate endDate, double percentage, String targetCategory) {
        validateCommonData(id, name, startDate, endDate, percentage);
        String category = targetCategory == null ? "" : targetCategory.trim().toUpperCase();
        if (!category.equals("VIDEOGAME") && !category.equals("CONSOLE")) {
            throw new IllegalArgumentException("La categoria debe ser VIDEOGAME o CONSOLE.");
        }
        return save(new CategoryDiscount(id.trim(), name.trim(), startDate, endDate, percentage, category));
    }

    /**
     * Registers a promotion that discounts a percentage of the subtotal when the
     * sale includes a minimum number of products.
     *
     * @param id              unique identifier
     * @param name            commercial name
     * @param startDate       first valid day
     * @param endDate         last valid day
     * @param minimumQuantity minimum number of products, at least 1
     * @param percentage      discount percentage, greater than 0 and up to 100
     * @return the registered promotion
     * @throws IllegalArgumentException if any data is invalid or the identifier already exists
     */
    public BulkPurchaseDiscount registerBulkPurchaseDiscount(String id, String name, LocalDate startDate,
                                                             LocalDate endDate, int minimumQuantity,
                                                             double percentage) {
        validateCommonData(id, name, startDate, endDate, percentage);
        if (minimumQuantity < 1) {
            throw new IllegalArgumentException("La cantidad minima debe ser al menos 1.");
        }
        return save(new BulkPurchaseDiscount(id.trim(), name.trim(), startDate, endDate, minimumQuantity, percentage));
    }

    /**
     * Returns every registered promotion, active or not.
     *
     * @return a copy of the promotion list
     */
    public List<Promotion> listAllPromotions() {
        return new ArrayList<>(promotions);
    }

    /**
     * Returns the promotions that are valid on the current date.
     *
     * @return the active promotions, possibly empty
     */
    public List<Promotion> listActivePromotions() {
        return listActivePromotions(LocalDate.now());
    }

    /**
     * Finds a promotion by its identifier, ignoring letter case.
     *
     * @param id the promotion identifier
     * @return the promotion found, or {@code null} if it does not exist
     */
    public Promotion findById(String id) {
        if (id == null) {
            return null;
        }
        for (Promotion promotion : promotions) {
            if (promotion.getId().equalsIgnoreCase(id.trim())) {
                return promotion;
            }
        }
        return null;
    }

    /**
     * Among the promotions valid on the date of the sale, returns the one that
     * grants the biggest discount. Promotions are not cumulative.
     *
     * @param sale the sale to evaluate
     * @return the best promotion, or {@code null} if none applies or the best discount is zero
     */
    public Promotion findBestPromotionFor(Sale sale) {
        Promotion best = null;
        double bestDiscount = 0;
        for (Promotion promotion : listActivePromotions(sale.getDate())) {
            double discount = promotion.calculateDiscount(sale);
            if (discount > bestDiscount) {
                best = promotion;
                bestDiscount = discount;
            }
        }
        return best;
    }

    private List<Promotion> listActivePromotions(LocalDate date) {
        List<Promotion> active = new ArrayList<>();
        for (Promotion promotion : promotions) {
            if (promotion.isActive(date)) {
                active.add(promotion);
            }
        }
        return active;
    }

    private <T extends Promotion> T save(T promotion) {
        promotions.add(promotion);
        promotionRepository.saveAll(promotions);
        return promotion;
    }

    private void validateCommonData(String id, String name, LocalDate startDate, LocalDate endDate,
                                    double percentage) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo codigo es obligatorio.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo nombre es obligatorio.");
        }
        if (findById(id) != null) {
            throw new IllegalArgumentException("Ya existe una promocion con codigo " + id.trim() + ".");
        }
        if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }
        if (percentage <= 0 || percentage > 100) {
            throw new IllegalArgumentException("El porcentaje debe ser mayor que 0 y como maximo 100.");
        }
    }
}
