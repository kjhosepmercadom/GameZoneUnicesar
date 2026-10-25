package com.gamezone.persistence;

import com.gamezone.model.Product;
import com.gamezone.model.Return;
import com.gamezone.model.Sale;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
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

    /**
     * Reads every return stored in the file, resolving the original sale and
     * the returned products against the given lists.
     *
     * @param sales    the sales already loaded by the system
     * @param products the products of the inventory
     * @return the stored returns, or an empty list if the file does not exist
     * @throws IllegalStateException if the file cannot be read, has an invalid line
     *                               or references an unknown sale or product
     */
    public List<Return> loadAll(List<Sale> sales, List<Product> products) {
        List<Return> returns = new ArrayList<>();
        for (String[] fields : readRecords()) {
            Sale sale = findSale(sales, fields[2]);
            List<Product> returned = new ArrayList<>();
            for (String productId : fields[3].split(LIST_SEPARATOR)) {
                returned.add(findProduct(products, productId));
            }
            returns.add(create(fields, sale, returned));
        }
        return returns;
    }

    private Return create(String[] fields, Sale sale, List<Product> returned) {
        Return productReturn = new Return(fields[0], LocalDate.parse(fields[1]), sale, returned, fields[4]);
        productReturn.calculateRefundAmount();
        return productReturn;
    }

    private List<String[]> readRecords() {
        List<String[]> records = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return records;
        }
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer el archivo de devoluciones: " + filePath, e);
        }
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (!line.isEmpty()) {
                records.add(line.split(SEPARATOR, -1));
            }
        }
        return records;
    }

    private Sale findSale(List<Sale> sales, String saleId) {
        for (Sale sale : sales) {
            if (sale.getId().equals(saleId)) {
                return sale;
            }
        }
        throw new IllegalStateException("Devolucion con venta desconocida " + saleId + " en " + filePath);
    }

    private Product findProduct(List<Product> products, String productId) {
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        throw new IllegalStateException("Devolucion con producto desconocido " + productId + " en " + filePath);
    }

    private String clean(String value) {
        return value == null ? "" : value.replace(SEPARATOR, ",").replace("\n", " ").replace("\r", " ");
    }
}
