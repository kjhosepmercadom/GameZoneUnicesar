package com.gamezone.persistence;

import com.gamezone.model.Product;
import com.gamezone.model.Sale;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores and retrieves the sales of the store in a CSV file separated by
 * semicolons. Each line keeps references to the customer (identification),
 * the seller (employee code) and the products (identifiers separated by
 * commas, one per unit sold).
 */
public class SaleRepository {

    private static final String SEPARATOR = ";";
    private static final String PRODUCT_SEPARATOR = ",";

    private final Path filePath;

    /**
     * Creates a repository that works over the given file.
     *
     * @param filePath path of the CSV file that stores the sales
     */
    public SaleRepository(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     * Writes every sale to the file, replacing its previous content.
     *
     * @param sales the sales to store
     * @throws IllegalStateException if the file cannot be written
     */
    public void saveAll(List<Sale> sales) {
        List<String> lines = new ArrayList<>();
        for (Sale sale : sales) {
            List<String> productIds = new ArrayList<>();
            for (Product product : sale.getProducts()) {
                productIds.add(product.getId());
            }
            lines.add(sale.getId() + SEPARATOR + sale.getDate() + SEPARATOR
                    + sale.getCustomer().getIdentification() + SEPARATOR
                    + sale.getSeller().getEmployeeCode() + SEPARATOR
                    + String.join(PRODUCT_SEPARATOR, productIds));
        }
        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo guardar el archivo de ventas: " + filePath, e);
        }
    }
}
