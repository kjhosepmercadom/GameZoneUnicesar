package com.gamezone.persistence;

import com.gamezone.exceptions.PersistenceException;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Warranty;

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

    private final CsvFile file;
    private final List<WarrantyRecordMapper> mappers;

    /**
     * Creates a repository that works over the given file.
     *
     * @param filePath path of the CSV file that stores the warranties
     */
    public WarrantyRepository(String filePath) {
        this.file = new CsvFile(filePath);
        this.mappers = List.of(new BasicWarrantyRecordMapper(), new ExtendedWarrantyRecordMapper());
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
            String type = mapperOf(warranty).getTypeCode();
            lines.add(type + SEPARATOR + warranty.getId() + SEPARATOR + warranty.getProduct().getId()
                    + SEPARATOR + warranty.getSale().getId() + SEPARATOR + warranty.getStartDate());
        }
        file.writeLines(lines, "No se pudo guardar el archivo de garantias: ");
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
                throw new PersistenceException("Garantia " + fields[1] + " con referencias invalidas en " + file.getPath(), null);
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
        for (WarrantyRecordMapper mapper : mappers) {
            if (mapper.getTypeCode().equals(fields[0])) {
                return mapper.create(fields[1], product, sale, start);
            }
        }
        throw new IllegalArgumentException("Tipo de garantia desconocido: " + fields[0]);
    }

    private WarrantyRecordMapper mapperOf(Warranty warranty) {
        for (WarrantyRecordMapper mapper : mappers) {
            if (mapper.supports(warranty)) {
                return mapper;
            }
        }
        throw new IllegalArgumentException("Tipo de garantia no soportado: " + warranty.getClass().getSimpleName());
    }

    private List<String[]> readRecords() {
        List<String[]> records = new ArrayList<>();
        List<String> lines = file.readLines("No se pudo leer el archivo de garantias: ");
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] fields = line.split(SEPARATOR, -1);
            if (fields.length != 5) {
                throw new PersistenceException("Linea " + (i + 1) + " invalida en " + file.getPath() + ": " + line, null);
            }
            records.add(fields);
        }
        return records;
    }
}
