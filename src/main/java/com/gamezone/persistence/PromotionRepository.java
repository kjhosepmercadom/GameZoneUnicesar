package com.gamezone.persistence;

import com.gamezone.exceptions.PersistenceException;
import com.gamezone.model.Promotion;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores and retrieves the promotions of the store in a CSV file separated by
 * semicolons. The first column is a discriminator (PERCENTAGE, CATEGORY or BULK)
 * that tells which concrete promotion type the line represents.
 */
public class PromotionRepository {

    private static final String SEPARATOR = ";";

    private final CsvFile file;
    private final List<RecordMapper<Promotion>> mappers;

    /**
     * Creates a repository that works over the given file.
     *
     * @param filePath path of the CSV file that stores the promotions
     */
    public PromotionRepository(String filePath) {
        this.file = new CsvFile(filePath);
        this.mappers = List.of(new PercentageDiscountRecordMapper(), new CategoryDiscountRecordMapper(), new BulkPurchaseDiscountRecordMapper());
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
        file.writeLines(lines, "No se pudo guardar el archivo de promociones: ");
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
        List<String> lines = file.readLines("No se pudo leer el archivo de promociones: ");
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            try {
                promotions.add(fromLine(line));
            } catch (RuntimeException e) {
                throw new PersistenceException("Linea " + (i + 1) + " invalida en " + file.getPath() + ": " + line, e);
            }
        }
        return promotions;
    }

    private Promotion fromLine(String line) {
        String[] fields = line.split(SEPARATOR, -1);
        String type = fields[0];
        for (RecordMapper<Promotion> mapper : mappers) {
            if (mapper.getTypeCode().equals(type)) {
                return mapper.fromFields(fields);
            }
        }
        throw new IllegalArgumentException("Tipo de promocion desconocido: " + type);
    }

    private String toLine(Promotion promotion) {
        for (RecordMapper<Promotion> mapper : mappers) {
            if (mapper.supports(promotion)) {
                return mapper.getTypeCode() + SEPARATOR + mapper.toColumns(promotion);
            }
        }
        throw new IllegalArgumentException("Tipo de promocion no soportado: " + promotion.getClass().getSimpleName());
    }
}
