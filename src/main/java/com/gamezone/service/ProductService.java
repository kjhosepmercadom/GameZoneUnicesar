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
 * validation, stock management, and search operations.
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
     *
     * @param id       unique identifier of the product
     * @param name     display name of the product
     * @param price    unit price, must not be negative
     * @param stock    available quantity, must not be negative
     * @param platform target gaming platform
     * @param genre    genre of the game
     * @return the registered VideoGame instance
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
     *
     * @param id         unique identifier of the product
     * @param name       display name of the product
     * @param price      unit price, must not be negative
     * @param stock      available quantity, must not be negative
     * @param brand      hardware manufacturer brand
     * @param generation console generation
     * @return the registered Console instance
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
     * Returns a copy of the current product catalog.
     *
     * @return defensive copy of product list
     */
    public List<Product> listAll() {
        return new ArrayList<>(products);
    }

    /**
     * Finds a product by its id.
     *
     * @param id the product ID to search for
     * @return Optional containing the product if found
     */
    public Optional<Product> findById(String id) {
        return products.stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst();
    }

    /**
     * Checks whether a product has enough stock.
     *
     * @param id       the product ID
     * @param quantity required stock quantity
     * @return true if enough stock is available
     */
    public boolean hasEnoughStock(String id, int quantity) {
        return findById(id)
                .map(p -> p.getStock() >= quantity)
                .orElse(false);
    }

    /**
     * Deducts stock after a confirmed sale.
     *
     * @param id       the product ID
     * @param quantity quantity to deduct
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

    /**
     * Filters products matching a given name keyword (case-insensitive).
     *
     * @param keyword the substring to search for in product names
     * @return list of matching products
     */
    public List<Product> searchByName(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return listAll();
        }
        String lowerKeyword = keyword.toLowerCase();
        return products.stream()
                .filter(p -> p.getName().toLowerCase().contains(lowerKeyword))
                .toList();
    }

    /**
     * Filters products within a specific price range.
     *
     * @param minPrice minimum price threshold
     * @param maxPrice maximum price threshold
     * @return list of products within the range
     */
    public List<Product> filterByPriceRange(double minPrice, double maxPrice) {
        if (minPrice < 0 || maxPrice < minPrice) {
            throw new IllegalArgumentException("Invalid price range parameters");
        }
        return products.stream()
                .filter(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
                .toList();
    }

    /**
     * Retrieves all video games from the catalog.
     *
     * @return list of VideoGame instances
     */
    public List<VideoGame> listVideoGames() {
        return products.stream()
                .filter(p -> p instanceof VideoGame)
                .map(p -> (VideoGame) p)
                .toList();
    }

    /**
     * Retrieves all consoles from the catalog.
     *
     * @return list of Console instances
     */
    public List<Console> listConsoles() {
        return products.stream()
                .filter(p -> p instanceof Console)
                .map(p -> (Console) p)
                .toList();
    }
}