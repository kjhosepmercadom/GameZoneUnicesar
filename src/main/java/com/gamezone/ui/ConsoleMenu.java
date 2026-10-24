package com.gamezone.ui;

import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Seller;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;

import java.util.List;
import java.util.Scanner;

/**
 * Console user interface of the system. It shows the menus in Spanish, reads
 * the user input and delegates every operation to the service layer; it never
 * accesses the persistence layer directly.
 */
public class ConsoleMenu {

    private final ProductService productService;
    private final PersonService personService;
    private final SaleService saleService;
    private final Scanner scanner;

    /**
     * Creates the menu with the services it uses.
     *
     * @param productService service of the product module
     * @param personService  service of the person module
     * @param saleService    service of the sales module
     */
    public ConsoleMenu(ProductService productService, PersonService personService, SaleService saleService) {
        this.productService = productService;
        this.personService = personService;
        this.saleService = saleService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Shows the main menu and processes the user options until the user exits.
     */
    public void start() {
        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("===== GAMEZONE UNICESAR =====");
            System.out.println("1. Gestion de productos");
            System.out.println("2. Gestion de personas");
            System.out.println("0. Salir");
            int option = readInt("Seleccione una opcion: ");
            switch (option) {
                case 1:
                    showProductMenu();
                    break;
                case 2:
                    showPersonMenu();
                    break;
                case 0:
                    running = false;
                    System.out.println("Gracias por usar GameZone Unicesar. Hasta pronto.");
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        }
    }

    private void showProductMenu() {
        System.out.println();
        System.out.println("--- Gestion de productos ---");
        System.out.println("1. Registrar videojuego");
        System.out.println("2. Registrar consola");
        System.out.println("3. Listar inventario de productos");
        System.out.println("0. Volver");
        int option = readInt("Seleccione una opcion: ");
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
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void registerVideoGame() {
        System.out.println("-- Registrar videojuego --");
        String id = readText("Codigo: ");
        String title = readText("Titulo: ");
        double price = readDouble("Precio: ");
        int stock = readInt("Cantidad en inventario: ");
        String platform = readText("Plataforma: ");
        String genre = readText("Genero: ");
        String ageRating = readText("Clasificacion de edad: ");
        productService.registerVideoGame(id, title, price, stock, platform, genre, ageRating);
        System.out.println("Videojuego registrado correctamente.");
    }

    private void registerConsole() {
        System.out.println("-- Registrar consola --");
        String id = readText("Codigo: ");
        String title = readText("Titulo: ");
        double price = readDouble("Precio: ");
        int stock = readInt("Cantidad en inventario: ");
        String brand = readText("Marca: ");
        String model = readText("Modelo: ");
        int generation = readInt("Generacion: ");
        productService.registerConsole(id, title, price, stock, brand, model, generation);
        System.out.println("Consola registrada correctamente.");
    }

    private void printProducts() {
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

    private void showPersonMenu() {
        System.out.println();
        System.out.println("--- Gestion de personas ---");
        System.out.println("1. Registrar cliente");
        System.out.println("2. Listar clientes");
        System.out.println("3. Listar vendedores");
        System.out.println("0. Volver");
        int option = readInt("Seleccione una opcion: ");
        try {
            switch (option) {
                case 1:
                    registerCustomer();
                    break;
                case 2:
                    printCustomers();
                    break;
                case 3:
                    printSellers();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void registerCustomer() {
        System.out.println("-- Registrar cliente --");
        String name = readText("Nombre completo: ");
        String identification = readText("Identificacion: ");
        String phone = readText("Telefono: ");
        String email = readText("Correo electronico: ");
        personService.registerCustomer(name, identification, phone, email);
        System.out.println("Cliente registrado correctamente.");
    }

    private void printCustomers() {
        List<Customer> customers = personService.listCustomers();
        System.out.println("-- Clientes registrados --");
        if (customers.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        for (Customer customer : customers) {
            System.out.println(customer.getDescription());
        }
    }

    private void printSellers() {
        List<Seller> sellers = personService.listSellers();
        System.out.println("-- Vendedores --");
        if (sellers.isEmpty()) {
            System.out.println("No hay vendedores registrados.");
            return;
        }
        for (Seller seller : sellers) {
            System.out.println(seller.getDescription());
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            throw new IllegalStateException("No hay mas datos de entrada.");
        }
        return scanner.nextLine().trim();
    }

    private String readText(String prompt) {
        while (true) {
            String value = readLine(prompt);
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Este campo es obligatorio.");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            String value = readLine(prompt);
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero entero.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            String value = readLine(prompt).replace("$", "").replace(" ", "");
            if (value.matches("\\d{1,3}(\\.\\d{3})+")) {
                value = value.replace(".", "");
            }
            value = value.replace(",", ".");
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero valido.");
            }
        }
    }
}
