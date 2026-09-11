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

    public ProductService(ProductPersistence persistence) {
        this.persistence = persistence;
        this.products = new ArrayList<>(persistence.loadAll());
    }

    public VideoGame registerVideoGame(String id, String name, double price, int stock,
                                       String platform, String genre) {
        VideoGame videoGame = new VideoGame(id, name, price, stock, platform, genre);
        products.add(videoGame);
        persistence.saveAll(products);
        return videoGame;
    }

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
     */
    public List<Product> listAll() {
        return new ArrayList<>(products);
    }

    public Optional<Product> findById(String id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    /**
     * Checks whether a product has enough stock for the requested
     * quantity, used by the Sale module before confirming a sale.
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
