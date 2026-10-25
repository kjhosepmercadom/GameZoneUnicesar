package com.gamezone.ui;

import com.gamezone.model.Accessory;
import com.gamezone.model.Console;
import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Promotion;
import com.gamezone.model.Return;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;
import com.gamezone.model.Warranty;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.PromotionService;
import com.gamezone.service.ReturnService;
import com.gamezone.service.SaleService;
import com.gamezone.service.WarrantyService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
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
    private final AccessoryService accessoryService;
    private final PromotionService promotionService;
    private final WarrantyService warrantyService;
    private final ReturnService returnService;
    private final Scanner scanner;

    /**
     * Creates the menu with the services it uses.
     *
     * @param productService   service of the product module
     * @param personService    service of the person module
     * @param saleService      service of the sales module
     * @param accessoryService service of the accessory module
     * @param promotionService service of the promotion module
     * @param warrantyService  service of the warranty module
     * @param returnService    service of the return module
     */
    public ConsoleMenu(ProductService productService, PersonService personService, SaleService saleService,
                       AccessoryService accessoryService, PromotionService promotionService,
                       WarrantyService warrantyService, ReturnService returnService) {
        this.productService = productService;
        this.personService = personService;
        this.saleService = saleService;
        this.accessoryService = accessoryService;
        this.promotionService = promotionService;
        this.warrantyService = warrantyService;
        this.returnService = returnService;
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
            System.out.println("3. Gestion de ventas");
            System.out.println("4. Gestion de accesorios");
            System.out.println("5. Gestion de promociones");
            System.out.println("6. Gestion de garantias");
            System.out.println("7. Gestion de devoluciones");
            System.out.println("0. Salir");
            int option = readInt("Seleccione una opcion: ");
            switch (option) {
                case 1:
                    showProductMenu();
                    break;
                case 2:
                    showPersonMenu();
                    break;
                case 3:
                    showSaleMenu();
                    break;
                case 4:
                    showAccessoryMenu();
                    break;
                case 5:
                    showPromotionMenu();
                    break;
                case 6:
                    showWarrantyMenu();
                    break;
                case 7:
                    showReturnMenu();
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

    private void showSaleMenu() {
        System.out.println();
        System.out.println("--- Gestion de ventas ---");
        System.out.println("1. Registrar venta");
        System.out.println("2. Historial completo de ventas");
        System.out.println("3. Historial de compras de un cliente");
        System.out.println("4. Ventas atendidas por un vendedor");
        System.out.println("5. Consultar detalle de una venta");
        System.out.println("0. Volver");
        int option = readInt("Seleccione una opcion: ");
        try {
            switch (option) {
                case 1:
                    registerSale();
                    break;
                case 2:
                    printSales(saleService.listAllSales(), "No hay ventas registradas.");
                    break;
                case 3:
                    printCustomers();
                    String customerId = readText("Identificacion del cliente: ");
                    if (personService.findCustomerById(customerId) == null) {
                        System.out.println("No existe un cliente con identificacion " + customerId + ".");
                    } else {
                        printSales(saleService.findSalesByCustomer(customerId), "El cliente no tiene compras registradas.");
                    }
                    break;
                case 4:
                    printSellers();
                    String sellerCode = readText("Codigo del vendedor: ");
                    if (personService.findSellerByEmployeeCode(sellerCode) == null) {
                        System.out.println("No existe un vendedor con codigo " + sellerCode + ".");
                    } else {
                        printSales(saleService.findSalesBySeller(sellerCode), "El vendedor no tiene ventas registradas.");
                    }
                    break;
                case 5:
                    String saleId = readText("Codigo de la venta: ");
                    Sale sale = saleService.findSaleById(saleId);
                    if (sale == null) {
                        System.out.println("No existe una venta con codigo " + saleId + ".");
                    } else {
                        System.out.println(sale.generateReceipt());
                    }
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

    private void registerSale() {
        System.out.println("-- Registrar venta --");
        if (personService.listCustomers().isEmpty()) {
            System.out.println("Primero debe registrar al menos un cliente.");
            return;
        }
        printCustomers();
        String customerId = readText("Identificacion del cliente: ");
        printSellers();
        String sellerCode = readText("Codigo del vendedor: ");
        List<String> itemIds = readSaleItems();
        List<String> extendedWarrantyIds = askExtendedWarranties(itemIds);
        Sale sale = saleService.registerSale(customerId, sellerCode, itemIds, extendedWarrantyIds);
        System.out.println("Venta registrada correctamente.");
        System.out.println(sale.generateReceipt());
    }

    private List<String> readSaleItems() {
        printProducts();
        printAccessories(accessoryService.listAllAccessories(), "No hay accesorios registrados.");
        List<String> itemIds = new ArrayList<>();
        while (true) {
            String itemId = readLine("Codigo del producto o accesorio (Enter para terminar): ");
            if (itemId.isEmpty()) {
                return itemIds;
            }
            if (productService.findById(itemId) == null && accessoryService.findById(itemId) == null) {
                System.out.println("No existe un producto o accesorio con codigo " + itemId + ".");
                continue;
            }
            int quantity = readInt("Cantidad: ");
            if (quantity <= 0) {
                System.out.println("La cantidad debe ser mayor que cero.");
                continue;
            }
            for (int i = 0; i < quantity; i++) {
                itemIds.add(itemId);
            }
        }
    }

    private List<String> askExtendedWarranties(List<String> itemIds) {
        List<String> extendedWarrantyIds = new ArrayList<>();
        for (String itemId : new LinkedHashSet<>(itemIds)) {
            Product product = productService.findById(itemId);
            if (product instanceof Console && !extendedWarrantyIds.contains(product.getId())) {
                String answer = readLine("Desea garantia extendida (10% del precio) para "
                        + product.getTitle() + "? (s/n): ");
                if (answer.equalsIgnoreCase("s")) {
                    extendedWarrantyIds.add(product.getId());
                }
            }
        }
        return extendedWarrantyIds;
    }

    private void printSales(List<Sale> sales, String emptyMessage) {
        if (sales.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }
        for (Sale sale : sales) {
            System.out.println(sale.generateReceipt());
        }
    }

    private void showAccessoryMenu() {
        System.out.println();
        System.out.println("--- Gestion de accesorios ---");
        System.out.println("1. Registrar control");
        System.out.println("2. Registrar cable");
        System.out.println("3. Registrar memoria");
        System.out.println("4. Listar todos los accesorios");
        System.out.println("5. Listar accesorios por tipo");
        System.out.println("6. Consultar accesorios compatibles con una consola");
        System.out.println("0. Volver");
        int option = readInt("Seleccione una opcion: ");
        try {
            switch (option) {
                case 1:
                    registerController();
                    break;
                case 2:
                    registerCable();
                    break;
                case 3:
                    registerMemory();
                    break;
                case 4:
                    printAccessories(accessoryService.listAllAccessories(), "No hay accesorios registrados.");
                    break;
                case 5:
                    String type = readAccessoryType();
                    if (type != null) {
                        printAccessories(accessoryService.listAccessoriesByType(type), "No hay accesorios de ese tipo.");
                    }
                    break;
                case 6:
                    printConsoles();
                    String consoleId = readText("Codigo de la consola: ");
                    printAccessories(accessoryService.findAccessoriesCompatibleWith(consoleId),
                            "No hay accesorios compatibles con la consola " + consoleId + ".");
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

    private void registerController() {
        System.out.println("-- Registrar control --");
        String id = readText("Codigo: ");
        String title = readText("Titulo: ");
        double price = readDouble("Precio: ");
        int stock = readInt("Cantidad en inventario: ");
        List<String> consoles = readCompatibleConsoles();
        String connection = readText("Tipo de conexion (Inalambrico/Alambrico): ");
        accessoryService.registerController(id, title, price, stock, consoles, connection);
        System.out.println("Control registrado correctamente.");
    }

    private void registerCable() {
        System.out.println("-- Registrar cable --");
        String id = readText("Codigo: ");
        String title = readText("Titulo: ");
        double price = readDouble("Precio: ");
        int stock = readInt("Cantidad en inventario: ");
        List<String> consoles = readCompatibleConsoles();
        double length = readDouble("Longitud en metros: ");
        String connector = readText("Tipo de conector (HDMI, USB, Optico...): ");
        accessoryService.registerCable(id, title, price, stock, consoles, length, connector);
        System.out.println("Cable registrado correctamente.");
    }

    private void registerMemory() {
        System.out.println("-- Registrar memoria --");
        String id = readText("Codigo: ");
        String title = readText("Titulo: ");
        double price = readDouble("Precio: ");
        int stock = readInt("Cantidad en inventario: ");
        List<String> consoles = readCompatibleConsoles();
        int capacity = readInt("Capacidad en GB: ");
        String memoryType = readText("Tipo de memoria (SD, microSD, Tarjeta interna): ");
        accessoryService.registerMemory(id, title, price, stock, consoles, capacity, memoryType);
        System.out.println("Memoria registrada correctamente.");
    }

    private List<String> readCompatibleConsoles() {
        printConsoles();
        String line = readLine("Codigos de consolas compatibles separados por coma (Enter si ninguna): ");
        List<String> ids = new ArrayList<>();
        for (String id : line.split(",")) {
            if (!id.trim().isEmpty()) {
                ids.add(id.trim());
            }
        }
        return ids;
    }

    private void printConsoles() {
        System.out.println("-- Consolas del inventario --");
        boolean found = false;
        for (Product product : productService.listAllProducts()) {
            if (product instanceof Console) {
                System.out.println(product.getDescription());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No hay consolas registradas.");
        }
    }

    private void printAccessories(List<Accessory> accessories, String emptyMessage) {
        System.out.println("-- Accesorios --");
        if (accessories.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }
        for (Accessory accessory : accessories) {
            System.out.println(accessory.getDescription());
        }
    }

    private String readAccessoryType() {
        int option = readInt("Tipo (1=Control, 2=Cable, 3=Memoria): ");
        switch (option) {
            case 1:
                return "CONTROLLER";
            case 2:
                return "CABLE";
            case 3:
                return "MEMORY";
            default:
                System.out.println("Tipo no valido.");
                return null;
        }
    }

    private void showPromotionMenu() {
        System.out.println();
        System.out.println("--- Gestion de promociones ---");
        System.out.println("1. Registrar promocion por porcentaje");
        System.out.println("2. Registrar promocion por categoria");
        System.out.println("3. Registrar promocion por volumen de compra");
        System.out.println("4. Listar todas las promociones");
        System.out.println("5. Listar promociones vigentes");
        System.out.println("0. Volver");
        int option = readInt("Seleccione una opcion: ");
        try {
            switch (option) {
                case 1:
                    registerPercentageDiscount();
                    break;
                case 2:
                    registerCategoryDiscount();
                    break;
                case 3:
                    registerBulkPurchaseDiscount();
                    break;
                case 4:
                    printPromotions(promotionService.listAllPromotions(), "No hay promociones registradas.");
                    break;
                case 5:
                    printPromotions(promotionService.listActivePromotions(), "No hay promociones vigentes hoy.");
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

    private void registerPercentageDiscount() {
        System.out.println("-- Registrar promocion por porcentaje --");
        String id = readText("Codigo: ");
        String name = readText("Nombre: ");
        LocalDate start = readDate("Fecha de inicio (AAAA-MM-DD): ");
        LocalDate end = readDate("Fecha de fin (AAAA-MM-DD): ");
        double percentage = readDouble("Porcentaje de descuento: ");
        promotionService.registerPercentageDiscount(id, name, start, end, percentage);
        System.out.println("Promocion registrada correctamente.");
    }

    private void registerCategoryDiscount() {
        System.out.println("-- Registrar promocion por categoria --");
        String id = readText("Codigo: ");
        String name = readText("Nombre: ");
        LocalDate start = readDate("Fecha de inicio (AAAA-MM-DD): ");
        LocalDate end = readDate("Fecha de fin (AAAA-MM-DD): ");
        double percentage = readDouble("Porcentaje de descuento: ");
        int option = readInt("Categoria (1=Videojuegos, 2=Consolas, 3=Accesorios): ");
        String category = option == 1 ? "VIDEOGAME" : option == 2 ? "CONSOLE" : option == 3 ? "ACCESSORY" : "";
        promotionService.registerCategoryDiscount(id, name, start, end, percentage, category);
        System.out.println("Promocion registrada correctamente.");
    }

    private void registerBulkPurchaseDiscount() {
        System.out.println("-- Registrar promocion por volumen de compra --");
        String id = readText("Codigo: ");
        String name = readText("Nombre: ");
        LocalDate start = readDate("Fecha de inicio (AAAA-MM-DD): ");
        LocalDate end = readDate("Fecha de fin (AAAA-MM-DD): ");
        int minimum = readInt("Cantidad minima de productos: ");
        double percentage = readDouble("Porcentaje de descuento: ");
        promotionService.registerBulkPurchaseDiscount(id, name, start, end, minimum, percentage);
        System.out.println("Promocion registrada correctamente.");
    }

    private void printPromotions(List<Promotion> promotions, String emptyMessage) {
        System.out.println("-- Promociones --");
        if (promotions.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }
        for (Promotion promotion : promotions) {
            System.out.println(promotion.getDescription());
        }
    }

    private LocalDate readDate(String prompt) {
        while (true) {
            String value = readLine(prompt);
            try {
                return LocalDate.parse(value);
            } catch (DateTimeParseException e) {
                System.out.println("Debe ingresar una fecha valida con el formato AAAA-MM-DD.");
            }
        }
    }

    private void showWarrantyMenu() {
        System.out.println();
        System.out.println("--- Gestion de garantias ---");
        System.out.println("1. Consultar la garantia de un producto en una venta");
        System.out.println("2. Listar todas las garantias");
        System.out.println("3. Listar garantias vigentes");
        System.out.println("4. Listar garantias proximas a vencer");
        System.out.println("0. Volver");
        int option = readInt("Seleccione una opcion: ");
        try {
            switch (option) {
                case 1:
                    String saleId = readText("Codigo de la venta: ");
                    String productId = readText("Codigo del producto: ");
                    Warranty warranty = warrantyService.findWarrantyByProduct(productId, saleId);
                    if (warranty == null) {
                        System.out.println("No existe una garantia para el producto " + productId
                                + " en la venta " + saleId + ".");
                    } else {
                        System.out.println(warranty.generateWarrantyCertificate());
                    }
                    break;
                case 2:
                    printWarranties(warrantyService.listAllWarranties(), "No hay garantias registradas.");
                    break;
                case 3:
                    printWarranties(warrantyService.listActiveWarranties(), "No hay garantias vigentes.");
                    break;
                case 4:
                    int days = readInt("Dias de anticipacion: ");
                    printWarranties(warrantyService.listWarrantiesExpiringSoon(days),
                            "No hay garantias que venzan en los proximos " + days + " dias.");
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

    private void printWarranties(List<Warranty> warranties, String emptyMessage) {
        if (warranties.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }
        for (Warranty warranty : warranties) {
            System.out.println(warranty.generateWarrantyCertificate());
        }
    }

    private void showReturnMenu() {
        System.out.println();
        System.out.println("--- Gestion de devoluciones ---");
        System.out.println("1. Registrar devolucion");
        System.out.println("2. Consultar todas las devoluciones");
        System.out.println("3. Consultar devoluciones por cliente");
        System.out.println("4. Consultar devoluciones por venta");
        System.out.println("5. Consultar balance mensual");
        System.out.println("0. Volver");
        int option = readInt("Seleccione una opcion: ");
        try {
            switch (option) {
                case 1:
                    registerReturn();
                    break;
                case 2:
                    printReturns(returnService.viewAllReturns(), "No hay devoluciones registradas.");
                    break;
                case 3:
                    printCustomers();
                    String customerId = readText("Identificacion del cliente: ");
                    printReturns(returnService.viewReturnsByCustomer(customerId),
                            "El cliente no tiene devoluciones registradas.");
                    break;
                case 4:
                    String saleId = readText("Codigo de la venta: ");
                    printReturns(returnService.viewReturnsBySale(saleId), "La venta no tiene devoluciones registradas.");
                    break;
                case 5:
                    showMonthlyBalance();
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

    private void registerReturn() {
        System.out.println("-- Registrar devolucion --");
        String saleId = readText("Codigo de la venta original: ");
        Sale sale = saleService.findSaleById(saleId);
        if (sale == null) {
            System.out.println("No existe una venta con codigo " + saleId + ".");
            return;
        }
        System.out.println(sale.generateReceipt());
        List<String> productIds = new ArrayList<>();
        while (true) {
            String productId = readLine("Codigo del producto a devolver (Enter para terminar): ");
            if (productId.isEmpty()) {
                break;
            }
            int quantity = readInt("Cantidad: ");
            if (quantity <= 0) {
                System.out.println("La cantidad debe ser mayor que cero.");
                continue;
            }
            for (int i = 0; i < quantity; i++) {
                productIds.add(productId);
            }
        }
        String reason = readText("Motivo de la devolucion: ");
        Return productReturn = returnService.registerReturn(sale.getId(), productIds, reason);
        System.out.println("Devolucion registrada correctamente.");
        System.out.println(productReturn.generateReturnReceipt());
    }

    private void printReturns(List<Return> returns, String emptyMessage) {
        if (returns.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }
        for (Return productReturn : returns) {
            System.out.println(productReturn.generateReturnReceipt());
        }
    }

    private void showMonthlyBalance() {
        int month = readInt("Mes (1-12): ");
        int year = readInt("Anio: ");
        if (month < 1 || month > 12) {
            System.out.println("El mes debe estar entre 1 y 12.");
            return;
        }
        System.out.println("-- Balance mensual " + month + "/" + year + " --");
        System.out.println(String.format("Total de ventas:       $%,.0f", returnService.calculateMonthlySales(month, year)));
        System.out.println(String.format("Total de devoluciones: $%,.0f", returnService.calculateMonthlyReturns(month, year)));
        System.out.println(String.format("Balance neto:          $%,.0f", returnService.generateMonthlyBalance(month, year)));
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
