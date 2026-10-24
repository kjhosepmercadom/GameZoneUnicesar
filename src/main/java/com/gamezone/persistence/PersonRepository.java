package com.gamezone.persistence;

import com.gamezone.model.Customer;
import com.gamezone.model.Seller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores and retrieves the people of the store in CSV files separated by
 * semicolons. Customers and sellers are kept in different files because the
 * sellers file is preloaded with the employees already hired by the store.
 */
public class PersonRepository {

    private static final String SEPARATOR = ";";

    private final Path customersFile;
    private final Path sellersFile;

    /**
     * Creates a repository that works over the given files.
     *
     * @param customersFile path of the CSV file that stores the customers
     * @param sellersFile   path of the CSV file that stores the sellers
     */
    public PersonRepository(String customersFile, String sellersFile) {
        this.customersFile = Paths.get(customersFile);
        this.sellersFile = Paths.get(sellersFile);
    }

    /**
     * Writes every customer to the customers file, replacing its previous content.
     *
     * @param customers the customers to store
     * @throws IllegalStateException if the file cannot be written
     */
    public void saveCustomers(List<Customer> customers) {
        List<String> lines = new ArrayList<>();
        for (Customer customer : customers) {
            lines.add(clean(customer.getIdentification()) + SEPARATOR + clean(customer.getName())
                    + SEPARATOR + clean(customer.getPhone()) + SEPARATOR + clean(customer.getEmail()));
        }
        writeLines(customersFile, lines);
    }

    /**
     * Reads every customer stored in the customers file.
     *
     * @return the stored customers, or an empty list if the file does not exist
     * @throws IllegalStateException if the file cannot be read or has an invalid line
     */
    public List<Customer> loadCustomers() {
        List<Customer> customers = new ArrayList<>();
        for (String[] fields : readRecords(customersFile, 4)) {
            customers.add(new Customer(fields[1], fields[0], fields[2], fields[3]));
        }
        return customers;
    }

    /**
     * Writes every seller to the sellers file, replacing its previous content.
     *
     * @param sellers the sellers to store
     * @throws IllegalStateException if the file cannot be written
     */
    public void saveSellers(List<Seller> sellers) {
        List<String> lines = new ArrayList<>();
        for (Seller seller : sellers) {
            lines.add(clean(seller.getIdentification()) + SEPARATOR + clean(seller.getName())
                    + SEPARATOR + clean(seller.getPhone()) + SEPARATOR + clean(seller.getEmployeeCode())
                    + SEPARATOR + clean(seller.getShift()));
        }
        writeLines(sellersFile, lines);
    }

    /**
     * Reads every seller stored in the sellers file.
     *
     * @return the stored sellers, or an empty list if the file does not exist
     * @throws IllegalStateException if the file cannot be read or has an invalid line
     */
    public List<Seller> loadSellers() {
        List<Seller> sellers = new ArrayList<>();
        for (String[] fields : readRecords(sellersFile, 5)) {
            sellers.add(new Seller(fields[1], fields[0], fields[2], fields[3], fields[4]));
        }
        return sellers;
    }

    private void writeLines(Path file, List<String> lines) {
        try {
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
            }
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo guardar el archivo: " + file, e);
        }
    }

    private List<String[]> readRecords(Path file, int expectedFields) {
        List<String[]> records = new ArrayList<>();
        if (!Files.exists(file)) {
            return records;
        }
        List<String> lines;
        try {
            lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer el archivo: " + file, e);
        }
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] fields = line.split(SEPARATOR, -1);
            if (fields.length != expectedFields) {
                throw new IllegalStateException("Linea " + (i + 1) + " invalida en " + file + ": " + line);
            }
            records.add(fields);
        }
        return records;
    }

    private String clean(String value) {
        return value == null ? "" : value.replace(SEPARATOR, ",").replace("\n", " ").replace("\r", " ");
    }
}
