package com.gamezone.persistence;

import com.gamezone.model.Product;
import com.gamezone.exception.ProductException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductPersistence {
    private final String filePath;

    public ProductPersistence(String filePath) {
        this.filePath = filePath;
    }

    public void saveAll(List<Product> products) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(products);
        } catch (IOException e) {
            throw new ProductException("Error saving products to file: " + filePath, e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Product> loadAll() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<Product>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new ProductException("Error loading products from file: " + filePath, e);
        }
    }
}