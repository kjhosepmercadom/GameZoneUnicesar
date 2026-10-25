package com.gamezone.persistence;

import com.gamezone.model.BulkPurchaseDiscount;
import com.gamezone.model.CategoryDiscount;
import com.gamezone.model.PercentageDiscount;
import com.gamezone.model.Promotion;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores and retrieves the promotions of the store in a CSV file separated by
 * semicolons. The first column is a discriminator (PERCENTAGE, CATEGORY or BULK)
 * that tells which concrete promotion type the line represents.
 */
public class PromotionRepository {

    private static final String SEPARATOR = ";";

    private final Path filePath;

    /**
     * Creates a repository that works over the given file.
     *
     * @param filePath path of the CSV file that stores the promotions
     */
    public PromotionRepository(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     * Writes every promotion to the file, replacing its previous content.
     *
     * @param promotions the promotions to store
     * @throws IllegalStateException if the file cannot be written
     */
    public void saveAll(List<Promotion> promotions) {
        List<String> lines = new ArrayList<>();
        for (Promotion promotion : promotions) {
            lines.add(toLine(promotion));
        }
        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo guardar el archivo de promociones: " + filePath, e);
        }
    }

    private String toLine(Promotion promotion) {
        String common = clean(promotion.getId()) + SEPARATOR + clean(promotion.getName()) + SEPARATOR
                + promotion.getStartDate() + SEPARATOR + promotion.getEndDate();
        if (promotion instanceof PercentageDiscount) {
            return "PERCENTAGE" + SEPARATOR + common + SEPARATOR + ((PercentageDiscount) promotion).getPercentage();
        }
        if (promotion instanceof CategoryDiscount) {
            CategoryDiscount category = (CategoryDiscount) promotion;
            return "CATEGORY" + SEPARATOR + common + SEPARATOR + category.getPercentage()
                    + SEPARATOR + category.getTargetCategory();
        }
        if (promotion instanceof BulkPurchaseDiscount) {
            BulkPurchaseDiscount bulk = (BulkPurchaseDiscount) promotion;
            return "BULK" + SEPARATOR + common + SEPARATOR + bulk.getMinimumQuantity()
                    + SEPARATOR + bulk.getPercentage();
        }
        throw new IllegalArgumentException("Tipo de promocion no soportado: " + promotion.getClass().getSimpleName());
    }

    private String clean(String value) {
        return value == null ? "" : value.replace(SEPARATOR, ",").replace("\n", " ").replace("\r", " ");
    }
}
