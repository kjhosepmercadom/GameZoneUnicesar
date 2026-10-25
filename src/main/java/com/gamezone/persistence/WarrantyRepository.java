package com.gamezone.persistence;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Warranty;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores and retrieves the warranties in a CSV file separated by semicolons.
 * The first column is a discriminator (BASIC or EXTENDED); the file keeps only
 * the identifiers of the covered product and of the sale.
 */
public class WarrantyRepository {

    private static final String SEPARATOR = ";";

    private final Path filePath;

    /**
     * Creates a repository that works over the given file.
     *
     * @param filePath path of the CSV file that stores the warranties
     */
    public WarrantyRepository(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     * Writes every warranty to the file, replacing its previous content.
     *
     * @param warranties the warranties to store
     * @throws IllegalStateException if the file cannot be written
     */
    public void saveAll(List<Warranty> warranties) {
        List<String> lines = new ArrayList<>();
        for (Warranty warranty : warranties) {
            String type = warranty instanceof ExtendedWarranty ? "EXTENDED" : "BASIC";
            lines.add(type + SEPARATOR + warranty.getId() + SEPARATOR + warranty.getProduct().getId()
                    + SEPARATOR + warranty.getSale().getId() + SEPARATOR + warranty.getStartDate());
        }
        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo guardar el archivo de garantias: " + filePath, e);
        }
    }
}
