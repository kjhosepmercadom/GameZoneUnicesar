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
import java.time.LocalDate;
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

    /**
     * Reads every promotion stored in the file, using the discriminator column
     * to rebuild the right concrete type.
     *
     * @return the stored promotions, or an empty list if the file does not exist
     * @throws IllegalStateException if the file cannot be read or has an invalid line
     */
    public List<Promotion> loadAll() {
        List<Promotion> promotions = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return promotions;
        }
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer el archivo de promociones: " + filePath, e);
        }
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            try {
                promotions.add(fromLine(line));
            } catch (RuntimeException e) {
                throw new IllegalStateException("Linea " + (i + 1) + " invalida en " + filePath + ": " + line, e);
            }
        }
        return promotions;
    }

    private Promotion fromLine(String line) {
        String[] fields = line.split(SEPARATOR, -1);
        String type = fields[0];
        String id = fields[1];
        String name = fields[2];
        LocalDate start = LocalDate.parse(fields[3]);
        LocalDate end = LocalDate.parse(fields[4]);
        switch (type) {
            case "PERCENTAGE":
                return new PercentageDiscount(id, name, start, end, Double.parseDouble(fields[5]));
            case "CATEGORY":
                return new CategoryDiscount(id, name, start, end, Double.parseDouble(fields[5]), fields[6]);
            case "BULK":
                return new BulkPurchaseDiscount(id, name, start, end, Integer.parseInt(fields[5]),
                        Double.parseDouble(fields[6]));
            default:
                throw new IllegalArgumentException("Tipo de promocion desconocido: " + type);
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
