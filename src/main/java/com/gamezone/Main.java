package com.gamezone;

import com.gamezone.persistence.PersonRepository;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;
import com.gamezone.ui.ConsoleMenu;

/**
 * Entry point of GameZone Unicesar. It builds every layer of the system,
 * injects the dependencies (repositories into services, services into the
 * user interface), loads the stored data and starts the console menu.
 */
public class Main {

    private static final String DATA_DIRECTORY = "data/";

    /**
     * Starts the application.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            ProductRepository productRepository = new ProductRepository(DATA_DIRECTORY + "products.csv");
            PersonRepository personRepository = new PersonRepository(DATA_DIRECTORY + "customers.csv",
                    DATA_DIRECTORY + "sellers.csv");
            SaleRepository saleRepository = new SaleRepository(DATA_DIRECTORY + "sales.csv");

            ProductService productService = new ProductService(productRepository);
            PersonService personService = new PersonService(personRepository);
            SaleService saleService = new SaleService(saleRepository, productService, personService);

            ConsoleMenu consoleMenu = new ConsoleMenu(productService, personService, saleService);
            consoleMenu.start();
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
