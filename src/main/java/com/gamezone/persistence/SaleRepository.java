package com.gamezone.persistence;

import com.gamezone.exceptions.PersistenceException;
import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
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
                    + String.join(PRODUCT_SEPARATOR, productIds)
                    + SEPARATOR + clean(sale.getAppliedPromotionName()) + SEPARATOR + sale.getDiscountAmount()
                    + SEPARATOR + sale.getExtendedWarrantyCost()
            );
        }
        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new PersistenceException("No se pudo guardar el archivo de ventas: " + filePath, e);
        }
    }

    /**
     * Reads every sale stored in the file, resolving its references against
     * the customers, sellers and products already loaded by the system.
     *
     * @param customers the known customers
     * @param sellers   the known sellers
     * @param products  the known products
     * @return the stored sales, or an empty list if the file does not exist
     * @throws IllegalStateException if the file cannot be read, has an invalid line
     *                               or references an unknown entity
     */
    public List<Sale> loadAll(List<Customer> customers, List<Seller> sellers, List<Product> products) {
        List<Sale> sales = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return sales;
        }
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new PersistenceException("No se pudo leer el archivo de ventas: " + filePath, e);
        }
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            try {
                sales.add(fromLine(line, customers, sellers, products));
            } catch (RuntimeException e) {
                throw new PersistenceException("Linea " + (i + 1) + " invalida en " + filePath + ": "
                        + e.getMessage(), e);
            }
        }
        return sales;
    }

    private Sale fromLine(String line, List<Customer> customers, List<Seller> sellers, List<Product> products) {
        String[] fields = line.split(SEPARATOR, -1);
        Customer customer = findCustomer(customers, fields[2]);
        Seller seller = findSeller(sellers, fields[3]);
        List<Product> saleProducts = new ArrayList<>();
        for (String productId : fields[4].split(PRODUCT_SEPARATOR)) {
            saleProducts.add(findProduct(products, productId));
        }
        Sale sale = new Sale(fields[0], LocalDate.parse(fields[1]), customer, seller, saleProducts);
        if (fields.length > 6) {
            sale.setAppliedPromotionName(fields[5].isEmpty() ? null : fields[5]);
            sale.setDiscountAmount(Double.parseDouble(fields[6]));
        }
        if (fields.length > 7) {
            sale.setExtendedWarrantyCost(Double.parseDouble(fields[7]));
        }
        return sale;
    }

    private String clean(String value) {
        return value == null ? "" : value.replace(SEPARATOR, ",").replace("\n", " ").replace("\r", " ");
    }

    private Customer findCustomer(List<Customer> customers, String identification) {
        for (Customer customer : customers) {
            if (customer.getIdentification().equals(identification)) {
                return customer;
            }
        }
        throw new IllegalArgumentException("cliente desconocido " + identification);
    }

    private Seller findSeller(List<Seller> sellers, String employeeCode) {
        for (Seller seller : sellers) {
            if (seller.getEmployeeCode().equals(employeeCode)) {
                return seller;
            }
        }
        throw new IllegalArgumentException("vendedor desconocido " + employeeCode);
    }

    private Product findProduct(List<Product> products, String productId) {
        for (Product product : products) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        throw new IllegalArgumentException("producto desconocido " + productId);
    }
}
