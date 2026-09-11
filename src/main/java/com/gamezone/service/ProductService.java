package com.gamezone.service;

import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import com.gamezone.persistence.ProductPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Business rules for the product catalog: registration, listing,
 * validation, and stock management.
 *
 * @author Dair
 */
public class ProductService {

    private final ProductPersistence persistence;
    private final List<Product> products;

    /**
     * Creates a new service backed by the given persistence handler.
     *
     * @param persistence the persistence handler used to load and save products
     */
    public ProductService(ProductPersistence persistence) {
        this.persistence = Objects.requireNonNull(persistence, "Persistence cannot be null");
        this.products = new ArrayList<>(persistence.loadAll());
    }

    /**
     * Registers a new video game with domain validations.
     */
    public VideoGame registerVideoGame(String id, String name, double price, int stock,
                                       String platform, String genre) {
        validateProductData(id, name, price, stock);
        if (platform == null || platform.isBlank()) {
            throw new IllegalArgumentException("Platform cannot be empty");
        }
        if (genre == null || genre.isBlank()) {
            throw new IllegalArgumentException("Genre cannot be empty");
        }

        VideoGame videoGame = new VideoGame(id, name, price, stock, platform, genre);
        products.add(videoGame);
        persistence.saveAll(products);
        return videoGame;
    }

    /**
     * Registers a new console with domain validations.
     */
    public Console registerConsole(String id, String name, double price, int stock,
                                   String brand, String generation) {
        validateProductData(id, name, price, stock);
        if (brand == null || brand.isBlank()) {
            throw new IllegalArgumentException("Brand cannot be empty");
        }
        if (generation == null || generation.isBlank()) {
            throw new IllegalArgumentException("Generation cannot be empty");
        }

        Console console = new Console(id, name, price, stock, brand, generation);
        products.add(console);
        persistence.saveAll(products);
        return console;
    }

    private void validateProductData(String id, String name, double price, int stock) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Product ID cannot be empty");
        }
        if (findById(id).isPresent()) {
            throw new IllegalArgumentException("Product ID already exists: " + id);
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
    }

    /**
     * Returns a copy of the product catalog.
     */
    public List<Product> listAll() {
        return new ArrayList<>(products);
    }

    /**
     * Finds a product by its id.
     */
    public Optional<Product> findById(String id) {
        return products.stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst();
    }

    /**
     * Checks whether a product has enough stock.
     */
    public boolean hasEnoughStock(String id, int quantity) {
        return findById(id)
                .map(p -> p.getStock() >= quantity)
                .orElse(false);
    }

    /**
     * Deducts stock after a confirmed sale.
     */
    public void reduceStock(String id, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to reduce must be positive");
        }

        Product product = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        if (product.getStock() < quantity) {
            throw new IllegalStateException("Insufficient stock for product: " + id);
        }

        product.setStock(product.getStock() - quantity);
        persistence.saveAll(products);
    }
}