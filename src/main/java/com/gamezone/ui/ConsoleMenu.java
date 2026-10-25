package com.gamezone.ui;

import com.gamezone.model.Accessory;
import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.Promotion;
import com.gamezone.model.Return;
import com.gamezone.model.Sale;
import com.gamezone.model.Warranty;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.PromotionService;
import com.gamezone.service.ReturnService;
import com.gamezone.service.SaleService;
import com.gamezone.service.WarrantyService;

import java.time.LocalDate;
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
    private final ConsoleInput input;
    private final ErrorPrinter errorPrinter;
    private final ProductMenu productMenu;
    private final PersonMenu personMenu;
    private final AccessoryMenu accessoryMenu;

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
        this.input = new ConsoleInput(new Scanner(System.in));
        this.errorPrinter = new ErrorPrinter();
        this.productMenu = new ProductMenu(productService, input, errorPrinter);
        this.personMenu = new PersonMenu(personService, input, errorPrinter);
        this.accessoryMenu = new AccessoryMenu(accessoryService, productService, input, errorPrinter);
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
                    productMenu.show();
                    break;
                case 2:
                    personMenu.show();
                    break;
                case 3:
                    showSaleMenu();
                    break;
                case 4:
                    accessoryMenu.show();
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

    private void printProducts() {
        productMenu.printProducts();
    }

    private void printCustomers() {
        personMenu.printCustomers();
    }

    private void printSellers() {
        personMenu.printSellers();
    }

    private void printAccessories(List<Accessory> accessories, String emptyMessage) {
        accessoryMenu.printAccessories(accessories, emptyMessage);
    }

    private String readLine(String prompt) {
        return input.readLine(prompt);
    }

    private String readText(String prompt) {
        return input.readText(prompt);
    }

    private int readInt(String prompt) {
        return input.readInt(prompt);
    }

    private double readDouble(String prompt) {
        return input.readDouble(prompt);
    }

    private void showError(RuntimeException error) {
        errorPrinter.print(error);
    }

    private LocalDate readDate(String prompt) {
        return input.readDate(prompt);
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
        } catch (RuntimeException e) {
            showError(e);
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
        } catch (RuntimeException e) {
            showError(e);
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
        } catch (RuntimeException e) {
            showError(e);
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
        } catch (RuntimeException e) {
            showError(e);
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
}
