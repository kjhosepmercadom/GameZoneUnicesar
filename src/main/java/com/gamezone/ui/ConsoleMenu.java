package com.gamezone.ui;

import com.gamezone.model.Return;
import com.gamezone.model.Sale;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.PromotionService;
import com.gamezone.service.ReturnService;
import com.gamezone.service.SaleService;
import com.gamezone.service.WarrantyService;

import java.util.ArrayList;
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
    private final SaleMenu saleMenu;
    private final PromotionMenu promotionMenu;
    private final WarrantyMenu warrantyMenu;

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
        this.saleMenu = new SaleMenu(saleService, personService, productService, accessoryService,
                personMenu, productMenu, accessoryMenu, input, errorPrinter);
        this.promotionMenu = new PromotionMenu(promotionService, input, errorPrinter);
        this.warrantyMenu = new WarrantyMenu(warrantyService, input, errorPrinter);
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
                    saleMenu.show();
                    break;
                case 4:
                    accessoryMenu.show();
                    break;
                case 5:
                    promotionMenu.show();
                    break;
                case 6:
                    warrantyMenu.show();
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

    private void printCustomers() {
        personMenu.printCustomers();
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
