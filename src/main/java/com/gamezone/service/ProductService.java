package com.gamezone.service;

import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import com.gamezone.persistence.ProductRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Applies the business rules of the product module: registration of video
 * games and consoles, inventory queries and stock updates. Every change is
 * persisted immediately through the {@link ProductRepository}.
 */
public class ProductService {

    private final ProductRepository productRepository;
    private final List<Product> products;

    /**
     * Creates the service and loads the stored products.
     *
     * @param productRepository repository used to persist the products
     */
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.products = productRepository.loadAll();
    }

    /**
     * Registers a new video game in the inventory.
     *
     * @param id        unique product identifier
     * @param title     commercial title
     * @param price     unit price in Colombian pesos
     * @param stock     initial stock
     * @param platform  platform the game was developed for
     * @param genre     genre of the game
     * @param ageRating recommended age rating
     * @return the registered video game
     * @throws IllegalArgumentException if any data is invalid or the identifier already exists
     */
    public VideoGame registerVideoGame(String id, String title, double price, int stock,
                                       String platform, String genre, String ageRating) {
        VideoGame videoGame = new VideoGame(id.trim(), title.trim(), price, stock,
                platform.trim(), genre.trim(), ageRating.trim());
        products.add(videoGame);
        productRepository.saveAll(products);
        return videoGame;
    }

    /**
     * Registers a new console in the inventory.
     *
     * @param id         unique product identifier
     * @param title      commercial title
     * @param price      unit price in Colombian pesos
     * @param stock      initial stock
     * @param brand      manufacturer brand
     * @param model      console model
     * @param generation console generation number
     * @return the registered console
     * @throws IllegalArgumentException if any data is invalid or the identifier already exists
     */
    public Console registerConsole(String id, String title, double price, int stock,
                                   String brand, String model, int generation) {
        Console console = new Console(id.trim(), title.trim(), price, stock,
                brand.trim(), model.trim(), generation);
        products.add(console);
        productRepository.saveAll(products);
        return console;
    }
}
