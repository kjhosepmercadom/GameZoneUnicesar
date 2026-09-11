package com.gamezone.service;

import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import com.gamezone.persistence.ProductPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Business rules for the product catalog: registration, listing,
 * and stock validation/update. This is the only layer allowed to
 * talk to ProductPersistence — the UI must go through here.
 *
 * @author Dair
 */
public class ProductService {

    private final ProductPersistence persistence;
    private final List<Product> products;

    /**
     * Creates a new service backed by the given persistence handler,
     * loading the existing catalog immediately.
     *
     * @param persistence the persistence handler used to load and save products
     */
    public ProductService(ProductPersistence persistence) {
        this.persistence = persistence;
        this.products = new ArrayList<>(persistence.loadAll());
    }

    /**
     * Registers a new video game and persists the updated catalog.
     *
     * @param id       unique identifier of the product
     * @param name     display name of the product
     * @param price    unit price, must not be negative
     * @param stock    available quantity, must not be negative
     * @param platform platform the game was developed for
     * @param genre    genre of the game
     * @return the newly registered video game
     */
    public VideoGame registerVideoGame(String id, String name, double price, int stock,
                                       String platform, String genre) {
        VideoGame videoGame = new VideoGame(id, name, price, stock, platform, genre);
        products.add(videoGame);
        persistence.saveAll(products);
        return videoGame;
    }

    /**
     * Registers a new console and persists the updated catalog.
     *
     * @param id         unique identifier of the product
     * @param name       display name of the product
     * @param price      unit price, must not be negative
     * @param stock      available quantity, must not be negative
     * @param brand      manufacturer brand of the console
     * @param generation hardware generation of the console
     * @return the newly registered console
     */
    public Console registerConsole(String id, String name, double price, int stock,
                                   String brand, String generation) {
        Console console = new Console(id, name, price, stock, brand, generation);
        products.add(console);
        persistence.saveAll(products);
        return console;
    }

    /**
     * Returns a defensive copy so callers cannot mutate the internal
     * catalog directly.
     *
     * @return a copy of the current product catalog
     */
    public List<Product> listAll() {
        return new ArrayList<>(products);
    }

    /**
     * Finds a product by its id.
     *
     * @param id the id to search for
     * @return an Optional containing the product if found, empty otherwise
     */
    public Optional<Product> findById(String id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    /**
     * Checks whether a product has enough stock for the requested
     * quantity, used by the Sale module before confirming a sale.
     *
     * @param id       the product id to check
     * @param quantity the quantity requested
     * @return true if the product exists and has enough stock
     */
    public boolean hasEnoughStock(String id, int quantity) {
        return findById(id)
                .map(p -> p.getStock() >= quantity)
                .orElse(false);
    }

    /**
     * Deducts stock after a confirmed sale. Throws if the product
     * doesn't exist or there isn't enough stock, so the sale module
     * never ends up with a negative inventory.
     *
     * @param id       the product id whose stock will be reduced
     * @param quantity the quantity to deduct
     */
    public void reduceStock(String id, int quantity) {
        Product product = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        if (product.getStock() < quantity) {
            throw new IllegalStateException("Insufficient stock for product: " + id);
        }

        product.setStock(product.getStock() - quantity);
        persistence.saveAll(products);
    }
}