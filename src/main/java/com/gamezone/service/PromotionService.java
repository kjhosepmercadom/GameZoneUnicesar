package com.gamezone.service;

import com.gamezone.model.BulkPurchaseDiscount;
import com.gamezone.model.CategoryDiscount;
import com.gamezone.model.PercentageDiscount;
import com.gamezone.model.Promotion;
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
        String category = targetCategory.trim().toUpperCase();
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
        return save(new BulkPurchaseDiscount(id.trim(), name.trim(), startDate, endDate, minimumQuantity, percentage));
    }

    private <T extends Promotion> T save(T promotion) {
        promotions.add(promotion);
        promotionRepository.saveAll(promotions);
        return promotion;
    }
}
