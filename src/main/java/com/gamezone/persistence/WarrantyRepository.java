package com.gamezone.persistence;

import com.gamezone.model.BasicWarranty;
import com.gamezone.model.ExtendedWarranty;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Warranty;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;

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
    private final SaleService saleService;
    private final ProductService productService;

    /**
     * Creates a repository that works over the given file. The services are
     * used to resolve the sale and product references while loading.
     *
     * @param filePath       path of the CSV file that stores the warranties
     * @param saleService    service used to find the sales by identifier
     * @param productService service used to find the products by identifier
     */
    public WarrantyRepository(String filePath, SaleService saleService, ProductService productService) {
        this.filePath = Paths.get(filePath);
        this.saleService = saleService;
        this.productService = productService;
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

    /**
     * Reads every warranty stored in the file, resolving the sale and the
     * product through the services.
     *
     * @return the stored warranties, or an empty list if the file does not exist
     * @throws IllegalStateException if the file cannot be read or has an invalid line
     */
    public List<Warranty> loadAll() {
        List<Warranty> warranties = new ArrayList<>();
        for (String[] fields : readRecords()) {
            Sale sale = saleService.findSaleById(fields[3]);
            Product product = productService.findById(fields[2]);
            if (sale == null || product == null) {
                throw new IllegalStateException("Garantia " + fields[1] + " con referencias invalidas en " + filePath);
            }
            warranties.add(create(fields, product, sale));
        }
        return warranties;
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
            throw new IllegalStateException("No se pudo leer el archivo de garantias: " + filePath, e);
        }
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] fields = line.split(SEPARATOR, -1);
            if (fields.length != 5) {
                throw new IllegalStateException("Linea " + (i + 1) + " invalida en " + filePath + ": " + line);
            }
            records.add(fields);
        }
        return records;
    }
}
