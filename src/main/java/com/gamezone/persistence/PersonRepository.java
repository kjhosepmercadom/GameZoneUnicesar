package com.gamezone.persistence;

import com.gamezone.model.Customer;

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

    private void writeLines(Path file, List<String> lines) {
        try {
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo guardar el archivo: " + file, e);
        }
    }

    private String clean(String value) {
        return value == null ? "" : value.replace(SEPARATOR, ",").replace("\n", " ").replace("\r", " ");
    }
}
