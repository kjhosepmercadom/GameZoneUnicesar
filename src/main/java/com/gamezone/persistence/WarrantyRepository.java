package com.gamezone.persistence;

import com.gamezone.exceptions.PersistenceException;
import com.gamezone.model.BasicWarranty;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Warranty;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
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
            throw new PersistenceException("No se pudo guardar el archivo de garantias: " + filePath, e);
        }
    }

    /**
     * Reads every warranty stored in the file, resolving the sale and the
     * covered product from the identifiers against the given sales.
     *
     * @param sales the sales already loaded by the system
     * @return the stored warranties, or an empty list if the file does not exist
     * @throws IllegalStateException if the file cannot be read, has an invalid line
     *                               or references an unknown sale or product
     */
    public List<Warranty> loadAll(List<Sale> sales) {
        List<Warranty> warranties = new ArrayList<>();
        for (String[] fields : readRecords()) {
            Sale sale = findSale(sales, fields[3]);
            Product product = sale == null ? null : findProduct(sale, fields[2]);
            if (sale == null || product == null) {
                throw new PersistenceException("Garantia " + fields[1] + " con referencias invalidas en " + filePath, null);
            }
            warranties.add(create(fields, product, sale));
        }
        return warranties;
    }

    private Sale findSale(List<Sale> sales, String saleId) {
        for (Sale sale : sales) {
            if (sale.getId().equals(saleId)) {
                return sale;
            }
        }
        return null;
    }

    private Product findProduct(Sale sale, String productId) {
        for (Product product : sale.getProducts()) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    private Warranty create(String[] fields, Product product, Sale sale) {
        LocalDate start = LocalDate.parse(fields[4]);
        if ("EXTENDED".equals(fields[0])) {
            return new ExtendedWarranty(fields[1], product, sale, start);
        }
        return new BasicWarranty(fields[1], product, sale, start);
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
            throw new PersistenceException("No se pudo leer el archivo de garantias: " + filePath, e);
        }
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] fields = line.split(SEPARATOR, -1);
            if (fields.length != 5) {
                throw new PersistenceException("Linea " + (i + 1) + " invalida en " + filePath + ": " + line, null);
            }
            records.add(fields);
        }
        return records;
    }
}
