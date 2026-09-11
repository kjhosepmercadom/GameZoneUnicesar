package com.gamezone.service;

import com.gamezone.exception.ProductException;
import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import com.gamezone.persistence.ProductPersistence;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Service layer to manage Product business operations and validation rules.
 *
 * @author Dair
 */
public class ProductService {

    private final ProductPersistence persistence;

    public ProductService(ProductPersistence persistence) {
        this.persistence = persistence;
    }

    public List<Product> getAllProducts() throws IOException {
        return persistence.findAll();
    }

    public Optional<Product> findById(String id) throws IOException {
        if (id == null || id.trim().isEmpty()) {
            throw new ProductException("Product ID cannot be null or empty.");
        }
        return persistence.findById(id);
    }

    public VideoGame registerVideoGame(String id, String name, double price, int stock, String platform, String genre) throws IOException {
        validateCommonFields(id, name, price, stock);
        if (platform == null || platform.trim().isEmpty()) {
            throw new ProductException("Platform cannot be empty.");
        }
        if (genre == null || genre.trim().isEmpty()) {
            throw new ProductException("Genre cannot be empty.");
        }

        VideoGame game = new VideoGame(id, name, price, stock, platform, genre);
        persistence.save(game);
        return game;
    }

    public Console registerConsole(String id, String name, double price, int stock, String brand, String generation) throws IOException {
        validateCommonFields(id, name, price, stock);
        if (brand == null || brand.trim().isEmpty()) {
            throw new ProductException("Brand cannot be empty.");
        }
        if (generation == null || generation.trim().isEmpty()) {
            throw new ProductException("Generation cannot be empty.");
        }

        Console console = new Console(id, name, price, stock, brand, generation);
        persistence.save(console);
        return console;
    }

    public void reduceStock(String id, int quantity) throws IOException {
        if (quantity <= 0) {
            throw new ProductException("Quantity to reduce must be greater than zero.");
        }

        Product product = findById(id)
                .orElseThrow(() -> new ProductException("Product not found with ID: " + id));

        if (product.getStock() < quantity) {
            throw new ProductException("Insufficient stock for product ID: " + id);
        }

        product.setStock(product.getStock() - quantity);
        persistence.update(product);
    }

    public List<Product> searchByName(String query) throws IOException {
        if (query == null || query.trim().isEmpty()) {
            return getAllProducts();
        }
        return persistence.searchByName(query);
    }

    private void validateCommonFields(String id, String name, double price, int stock) throws IOException {
        if (id == null || id.trim().isEmpty()) {
            throw new ProductException("Product ID cannot be empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new ProductException("Product name cannot be empty.");
        }
        if (price < 0) {
            throw new ProductException("Product price cannot be negative.");
        }
        if (stock < 0) {
            throw new ProductException("Product stock cannot be negative.");
        }
        if (persistence.existsById(id)) {
            throw new ProductException("Product ID already exists: " + id);
        }
    }
}