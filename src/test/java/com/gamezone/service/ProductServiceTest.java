package com.gamezone.service;

import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import com.gamezone.persistence.ProductPersistence;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Unit test suite to verify ProductService business rules independently.
 *
 * @author Dair
 */
public class ProductServiceTest {

    public static void main(String[] args) throws IOException {
        // Prepare temporary test persistence file
        File tempFile = File.createTempFile("test_products", ".txt");
        tempFile.deleteOnExit();

        ProductPersistence persistence = new ProductPersistence(tempFile.getAbsolutePath());
        ProductService service = new ProductService(persistence);

        System.out.println("--- Running ProductService Unit Tests ---");

        // Test 1: Register VideoGame
        VideoGame game = service.registerVideoGame("VG999", "Elden Ring", 59.99, 10, "PS5", "RPG");
        if (!"VG999".equals(game.getId())) {
            throw new IllegalStateException("Test 1 Failed: Game ID mismatch");
        }
        System.out.println("[OK] Test 1 Passed: Register VideoGame");

        // Test 2: Register Console
        Console console = service.registerConsole("CON999", "Xbox Series X", 499.99, 4, "Microsoft", "9th Gen");
        if (!"CON999".equals(console.getId())) {
            throw new IllegalStateException("Test 2 Failed: Console ID mismatch");
        }
        System.out.println("[OK] Test 2 Passed: Register Console");

        // Test 3: Duplicate ID Validation
        try {
            service.registerVideoGame("VG999", "Duplicate Game", 19.99, 5, "PC", "Action");
            System.err.println("[FAIL] Test 3 Failed: Should have thrown exception for duplicate ID");
        } catch (IllegalArgumentException e) {
            System.out.println("[OK] Test 3 Passed: Prevented duplicate product ID");
        }

        // Test 4: Stock Reduction
        service.reduceStock("VG999", 3);
        Product updatedGame = service.findById("VG999").orElseThrow();
        if (updatedGame.getStock() != 7) {
            throw new IllegalStateException("Test 4 Failed: Stock reduction incorrect");
        }
        System.out.println("[OK] Test 4 Passed: Stock reduction working correctly");

        // Test 5: Search functionality
        List<Product> searchResult = service.searchByName("Elden");
        if (searchResult.size() != 1) {
            throw new IllegalStateException("Test 5 Failed: Search results count mismatch");
        }
        System.out.println("[OK] Test 5 Passed: Search by name");

        System.out.println("--- All Tests Passed Successfully! ---");
    }
}