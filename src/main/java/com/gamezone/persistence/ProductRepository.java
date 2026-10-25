package com.gamezone.persistence;

import com.gamezone.exceptions.PersistenceException;
import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;

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

    private final CsvFile file;

    /**
     * Creates a repository that works over the given file.
     *
     * @param filePath path of the CSV file that stores the products
     */
    public ProductRepository(String filePath) {
        this.file = new CsvFile(filePath);
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
        file.writeLines(lines, "No se pudo guardar el archivo de productos: ");
    }

    /**
     * Reads every product stored in the file. The discriminator column is used
     * to rebuild the right concrete type.
     *
     * @return the stored products, or an empty list if the file does not exist
     * @throws IllegalStateException if the file cannot be read or has an invalid line
     */
    public List<Product> loadAll() {
        List<Product> products = new ArrayList<>();
        List<String> lines = file.readLines("No se pudo leer el archivo de productos: ");
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            try {
                products.add(fromLine(line));
            } catch (RuntimeException e) {
                throw new PersistenceException("Linea " + (i + 1) + " invalida en " + file.getPath() + ": " + line, e);
            }
        }
        return products;
    }

    private Product fromLine(String line) {
        String[] fields = line.split(SEPARATOR, -1);
        String type = fields[0];
        String id = fields[1];
        String title = fields[2];
        double price = Double.parseDouble(fields[3]);
        int stock = Integer.parseInt(fields[4]);
        if (VIDEO_GAME_TYPE.equals(type)) {
            return new VideoGame(id, title, price, stock, fields[5], fields[6], fields[7]);
        }
        if (CONSOLE_TYPE.equals(type)) {
            return new Console(id, title, price, stock, fields[5], fields[6], Integer.parseInt(fields[7]));
        }
        throw new IllegalArgumentException("Tipo de producto desconocido: " + type);
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
        return CsvFile.clean(value);
    }
}
