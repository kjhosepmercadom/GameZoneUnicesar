package com.gamezone.persistence;

import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles reading and writing the product catalog to a plain-text
 * file. Each line represents one product, using ';' as a field
 * separator and the first field as a type discriminator
 * (VIDEOGAME or CONSOLE).
 *
 * File format:
 * VIDEOGAME;id;name;price;stock;platform;genre
 * CONSOLE;id;name;price;stock;brand;generation
 *
 * @author Dair
 */
public class ProductPersistence {

    private final String filePath;

    public ProductPersistence(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads every product stored in the file. If the file does not
     * exist yet, an empty list is returned instead of failing, so the
     * application can run on a clean environment.
     */
    public List<Product> loadAll() {
        List<Product> products = new ArrayList<>();
        Path path = Path.of(filePath);

        if (!Files.exists(path)) {
            return products;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                Product product = parseLine(line);
                if (product != null) {
                    products.add(product);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading product file: " + filePath, e);
        }

        return products;
    }

    /**
     * Overwrites the file with the full list of products (simplest
     * strategy that keeps the file consistent with in-memory state).
     */
    public void saveAll(List<Product> products) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Product product : products) {
                writer.println(toLine(product));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing product file: " + filePath, e);
        }
    }

    private Product parseLine(String line) {
        String[] fields = line.split(";");
        String type = fields[0];
        String id = fields[1];
        String name = fields[2];
        double price = Double.parseDouble(fields[3]);
        int stock = Integer.parseInt(fields[4]);

        switch (type) {
            case "VIDEOGAME":
                String platform = fields[5];
                String genre = fields[6];
                return new VideoGame(id, name, price, stock, platform, genre);
            case "CONSOLE":
                String brand = fields[5];
                String generation = fields[6];
                return new Console(id, name, price, stock, brand, generation);
            default:
                return null;
        }
    }

    private String toLine(Product product) {
        if (product instanceof VideoGame) {
            VideoGame videoGame = (VideoGame) product;
            return String.join(";", "VIDEOGAME", videoGame.getId(), videoGame.getName(),
                    String.valueOf(videoGame.getPrice()), String.valueOf(videoGame.getStock()),
                    videoGame.getPlatform(), videoGame.getGenre());
        }
        if (product instanceof Console) {
            Console console = (Console) product;
            return String.join(";", "CONSOLE", console.getId(), console.getName(),
                    String.valueOf(console.getPrice()), String.valueOf(console.getStock()),
                    console.getBrand(), console.getGeneration());
        }
        throw new IllegalArgumentException("Unsupported product type: " + product.getClass());
    }
}
