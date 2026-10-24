package com.gamezone.persistence;

import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores and retrieves the products of the store in a CSV file separated by
 * semicolons. The first column of every line is a discriminator that tells
 * which concrete product type the line represents.
 */
public class ProductRepository {

    private static final String SEPARATOR = ";";
    private static final String VIDEO_GAME_TYPE = "VIDEOGAME";
    private static final String CONSOLE_TYPE = "CONSOLE";

    private final Path filePath;

    /**
     * Creates a repository that works over the given file.
     *
     * @param filePath path of the CSV file that stores the products
     */
    public ProductRepository(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     * Writes every product to the file, replacing its previous content.
     *
     * @param products the products to store
     * @throws IllegalStateException if the file cannot be written
     */
    public void saveAll(List<Product> products) {
        List<String> lines = new ArrayList<>();
        for (Product product : products) {
            lines.add(toLine(product));
        }
        try {
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo guardar el archivo de productos: " + filePath, e);
        }
    }

    private String toLine(Product product) {
        String common = clean(product.getId()) + SEPARATOR + clean(product.getTitle()) + SEPARATOR
                + product.getPrice() + SEPARATOR + product.getStock();
        if (product instanceof VideoGame) {
            VideoGame videoGame = (VideoGame) product;
            return VIDEO_GAME_TYPE + SEPARATOR + common + SEPARATOR + clean(videoGame.getPlatform())
                    + SEPARATOR + clean(videoGame.getGenre()) + SEPARATOR + clean(videoGame.getAgeRating());
        }
        if (product instanceof Console) {
            Console console = (Console) product;
            return CONSOLE_TYPE + SEPARATOR + common + SEPARATOR + clean(console.getBrand())
                    + SEPARATOR + clean(console.getModel()) + SEPARATOR + console.getGeneration();
        }
        throw new IllegalArgumentException("Tipo de producto no soportado: " + product.getClass().getSimpleName());
    }

    private String clean(String value) {
        return value == null ? "" : value.replace(SEPARATOR, ",").replace("\n", " ").replace("\r", " ");
    }
}
