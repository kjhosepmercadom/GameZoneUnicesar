package com.gamezone.ui;

import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.SaleService;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Console submenu of the sales module: sale registration with products,
 * accessories and extended warranties, and the sales history queries.
 */
public class SaleMenu {

    private final SaleService saleService;
    private final PersonService personService;
    private final ProductService productService;
    private final AccessoryService accessoryService;
    private final PersonMenu personMenu;
    private final ProductMenu productMenu;
    private final AccessoryMenu accessoryMenu;
    private final ConsoleInput input;
    private final ErrorPrinter errorPrinter;

    /**
     * Creates the submenu.
     *
     * @param saleService      service of the sales module
     * @param personService    service used to validate customers and sellers
     * @param productService   service used to find the products to sell
     * @param accessoryService service used to find the accessories to sell
     * @param personMenu       submenu used to print customers and sellers
     * @param productMenu      submenu used to print the product inventory
     * @param accessoryMenu    submenu used to print the accessories
     * @param input            reader of the user input
     * @param errorPrinter     printer of the error messages
     */
    public SaleMenu(SaleService saleService, PersonService personService, ProductService productService,
                    AccessoryService accessoryService, PersonMenu personMenu, ProductMenu productMenu,
                    AccessoryMenu accessoryMenu, ConsoleInput input, ErrorPrinter errorPrinter) {
        this.saleService = saleService;
        this.personService = personService;
        this.productService = productService;
        this.accessoryService = accessoryService;
        this.personMenu = personMenu;
        this.productMenu = productMenu;
        this.accessoryMenu = accessoryMenu;
        this.input = input;
        this.errorPrinter = errorPrinter;
    }

    /**
     * Shows the submenu and executes the selected option.
     */
    public void show() {
        System.out.println();
        System.out.println("--- Gestion de ventas ---");
        System.out.println("1. Registrar venta");
        System.out.println("2. Historial completo de ventas");
        System.out.println("3. Historial de compras de un cliente");
        System.out.println("4. Ventas atendidas por un vendedor");
        System.out.println("5. Consultar detalle de una venta");
        System.out.println("0. Volver");
        int option = input.readInt("Seleccione una opcion: ");
        try {
            switch (option) {
                case 1:
                    registerSale();
                    break;
                case 2:
                    printSales(saleService.listAllSales(), "No hay ventas registradas.");
                    break;
                case 3:
                    personMenu.printCustomers();
                    String customerId = input.readText("Identificacion del cliente: ");
                    if (personService.findCustomerById(customerId) == null) {
                        System.out.println("No existe un cliente con identificacion " + customerId + ".");
                    } else {
                        printSales(saleService.findSalesByCustomer(customerId), "El cliente no tiene compras registradas.");
                    }
                    break;
                case 4:
                    personMenu.printSellers();
                    String sellerCode = input.readText("Codigo del vendedor: ");
                    if (personService.findSellerByEmployeeCode(sellerCode) == null) {
                        System.out.println("No existe un vendedor con codigo " + sellerCode + ".");
                    } else {
                        printSales(saleService.findSalesBySeller(sellerCode), "El vendedor no tiene ventas registradas.");
                    }
                    break;
                case 5:
                    String saleId = input.readText("Codigo de la venta: ");
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
            errorPrinter.print(e);
        }
    }

    private void registerSale() {
        System.out.println("-- Registrar venta --");
        if (personService.listCustomers().isEmpty()) {
            System.out.println("Primero debe registrar al menos un cliente.");
            return;
        }
        personMenu.printCustomers();
        String customerId = input.readText("Identificacion del cliente: ");
        personMenu.printSellers();
        String sellerCode = input.readText("Codigo del vendedor: ");
        List<String> itemIds = readSaleItems();
        List<String> extendedWarrantyIds = askExtendedWarranties(itemIds);
        Sale sale = saleService.registerSale(customerId, sellerCode, itemIds, extendedWarrantyIds);
        System.out.println("Venta registrada correctamente.");
        System.out.println(sale.generateReceipt());
    }

    private List<String> readSaleItems() {
        productMenu.printProducts();
        accessoryMenu.printAccessories(accessoryService.listAllAccessories(), "No hay accesorios registrados.");
        List<String> itemIds = new ArrayList<>();
        while (true) {
            String itemId = input.readLine("Codigo del producto o accesorio (Enter para terminar): ");
            if (itemId.isEmpty()) {
                return itemIds;
            }
            if (productService.findById(itemId) == null && accessoryService.findById(itemId) == null) {
                System.out.println("No existe un producto o accesorio con codigo " + itemId + ".");
                continue;
            }
            int quantity = input.readInt("Cantidad: ");
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
                String answer = input.readLine("Desea garantia extendida (10% del precio) para "
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
}
