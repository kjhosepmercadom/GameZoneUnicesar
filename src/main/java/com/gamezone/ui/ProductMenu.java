package com.gamezone.ui;

import com.gamezone.model.Product;
import com.gamezone.service.ProductService;

import java.util.List;

/**
 * Console submenu of the product module: registration of video games and
 * consoles and the product inventory listing.
 */
public class ProductMenu {

    private final ProductService productService;
    private final ConsoleInput input;
    private final ErrorPrinter errorPrinter;

    /**
     * Creates the submenu.
     *
     * @param productService service of the product module
     * @param input          reader of the user input
     * @param errorPrinter   printer of the error messages
     */
    public ProductMenu(ProductService productService, ConsoleInput input, ErrorPrinter errorPrinter) {
        this.productService = productService;
        this.input = input;
        this.errorPrinter = errorPrinter;
    }

    /**
     * Shows the submenu and executes the selected option.
     */
    public void show() {
        System.out.println();
        System.out.println("--- Gestion de productos ---");
        System.out.println("1. Registrar videojuego");
        System.out.println("2. Registrar consola");
        System.out.println("3. Listar inventario de productos");
        System.out.println("0. Volver");
        int option = input.readInt("Seleccione una opcion: ");
        try {
            switch (option) {
                case 1:
                    registerVideoGame();
                    break;
                case 2:
                    registerConsole();
                    break;
                case 3:
                    printProducts();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } catch (RuntimeException e) {
            errorPrinter.print(e);
        }
    }

    /**
     * Prints the product inventory.
     */
    public void printProducts() {
        List<Product> products = productService.listAllProducts();
        System.out.println("-- Inventario de productos --");
        if (products.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }
        for (Product product : products) {
            System.out.println(product.getDescription());
        }
    }

    private void registerVideoGame() {
        System.out.println("-- Registrar videojuego --");
        String id = input.readText("Codigo: ");
        String title = input.readText("Titulo: ");
        double price = input.readDouble("Precio: ");
        int stock = input.readInt("Cantidad en inventario: ");
        String platform = input.readText("Plataforma: ");
        String genre = input.readText("Genero: ");
        String ageRating = input.readText("Clasificacion de edad: ");
        productService.registerVideoGame(id, title, price, stock, platform, genre, ageRating);
        System.out.println("Videojuego registrado correctamente.");
    }

    private void registerConsole() {
        System.out.println("-- Registrar consola --");
        String id = input.readText("Codigo: ");
        String title = input.readText("Titulo: ");
        double price = input.readDouble("Precio: ");
        int stock = input.readInt("Cantidad en inventario: ");
        String brand = input.readText("Marca: ");
        String model = input.readText("Modelo: ");
        int generation = input.readInt("Generacion: ");
        productService.registerConsole(id, title, price, stock, brand, model, generation);
        System.out.println("Consola registrada correctamente.");
    }
}
