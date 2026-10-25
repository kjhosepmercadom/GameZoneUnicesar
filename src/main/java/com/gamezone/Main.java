package com.gamezone;

import com.gamezone.persistence.AccessoryRepository;
import com.gamezone.persistence.PersonRepository;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.persistence.PromotionRepository;
import com.gamezone.persistence.SaleRepository;
import com.gamezone.persistence.WarrantyRepository;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.PromotionService;
import com.gamezone.service.SaleService;
import com.gamezone.service.WarrantyService;
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
            AccessoryRepository accessoryRepository = new AccessoryRepository(DATA_DIRECTORY + "accessories.csv");
            PromotionRepository promotionRepository = new PromotionRepository(DATA_DIRECTORY + "promotions.csv");

            ProductService productService = new ProductService(productRepository);
            PersonService personService = new PersonService(personRepository);
            AccessoryService accessoryService = new AccessoryService(accessoryRepository);
            PromotionService promotionService = new PromotionService(promotionRepository);
            SaleService saleService = new SaleService(saleRepository, productService, personService,
                    accessoryService, promotionService);
            // The warranty repository needs the sale service to load its data, so the
            // warranty service cannot be injected into SaleService through its constructor.
            WarrantyRepository warrantyRepository = new WarrantyRepository(DATA_DIRECTORY + "warranties.csv",
                    saleService, productService);
            WarrantyService warrantyService = new WarrantyService(warrantyRepository);
            saleService.setWarrantyService(warrantyService);

            ConsoleMenu consoleMenu = new ConsoleMenu(productService, personService, saleService, accessoryService,
                    promotionService, warrantyService);
            consoleMenu.start();
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
