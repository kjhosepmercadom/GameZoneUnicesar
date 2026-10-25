package com.gamezone.persistence;

import com.gamezone.model.Product;
import com.gamezone.model.Return;
import com.gamezone.model.Sale;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores and retrieves the returns in a CSV file separated by semicolons. Each
 * line keeps the identifier of the original sale and the identifiers of the
 * returned products separated by commas, one per returned unit.
 */
public class ReturnRepository {

    private static final String SEPARATOR = ";";
    private static final String LIST_SEPARATOR = ",";

    private final Path filePath;

    /**
     * Creates a repository that works over the given file.
     *
     * @param filePath path of the CSV file that stores the returns
     */
    public ReturnRepository(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     * Writes every return to the file, replacing its previous content.
     *
     * @param returns the returns to store
     * @throws IllegalStateException if the file cannot be written
     */
    public void saveAll(List<Return> returns) {
        List<String> lines = new ArrayList<>();
        for (Return productReturn : returns) {
            List<String> productIds = new ArrayList<>();
            for (Product product : productReturn.getReturnedProducts()) {
                productIds.add(product.getId());
            }
            lines.add(productReturn.getId() + SEPARATOR + productReturn.getDate() + SEPARATOR
                    + productReturn.getSale().getId() + SEPARATOR + String.join(LIST_SEPARATOR, productIds)
                    + SEPARATOR + clean(productReturn.getReason()) + SEPARATOR + productReturn.getRefundAmount()
            );
        }
        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo guardar el archivo de devoluciones: " + filePath, e);
        }
    }

    private String clean(String value) {
        return value == null ? "" : value.replace(SEPARATOR, ",").replace("\n", " ").replace("\r", " ");
    }
}
