package com.gamezone.ui;

import com.gamezone.model.Return;
import com.gamezone.model.Sale;
import com.gamezone.service.ReturnService;
import com.gamezone.service.SaleService;

import java.util.ArrayList;
import java.util.List;

/**
 * Console submenu of the return module: return registration, return queries
 * and the monthly balance of sales and returns.
 */
public class ReturnMenu {

    private final ReturnService returnService;
    private final SaleService saleService;
    private final PersonMenu personMenu;
    private final ConsoleInput input;
    private final ErrorPrinter errorPrinter;

    /**
     * Creates the submenu.
     *
     * @param returnService service of the return module
     * @param saleService   service used to show the original sale
     * @param personMenu    submenu used to print the customers
     * @param input         reader of the user input
     * @param errorPrinter  printer of the error messages
     */
    public ReturnMenu(ReturnService returnService, SaleService saleService, PersonMenu personMenu,
                      ConsoleInput input, ErrorPrinter errorPrinter) {
        this.returnService = returnService;
        this.saleService = saleService;
        this.personMenu = personMenu;
        this.input = input;
        this.errorPrinter = errorPrinter;
    }

    /**
     * Shows the submenu and executes the selected option.
     */
    public void show() {
        System.out.println();
        System.out.println("--- Gestion de devoluciones ---");
        System.out.println("1. Registrar devolucion");
        System.out.println("2. Consultar todas las devoluciones");
        System.out.println("3. Consultar devoluciones por cliente");
        System.out.println("4. Consultar devoluciones por venta");
        System.out.println("5. Consultar balance mensual");
        System.out.println("0. Volver");
        int option = input.readInt("Seleccione una opcion: ");
        try {
            switch (option) {
                case 1:
                    registerReturn();
                    break;
                case 2:
                    printReturns(returnService.viewAllReturns(), "No hay devoluciones registradas.");
                    break;
                case 3:
                    personMenu.printCustomers();
                    String customerId = input.readText("Identificacion del cliente: ");
                    printReturns(returnService.viewReturnsByCustomer(customerId),
                            "El cliente no tiene devoluciones registradas.");
                    break;
                case 4:
                    String saleId = input.readText("Codigo de la venta: ");
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
            errorPrinter.print(e);
        }
    }

    private void registerReturn() {
        System.out.println("-- Registrar devolucion --");
        String saleId = input.readText("Codigo de la venta original: ");
        Sale sale = saleService.findSaleById(saleId);
        if (sale == null) {
            System.out.println("No existe una venta con codigo " + saleId + ".");
            return;
        }
        System.out.println(sale.generateReceipt());
        List<String> productIds = new ArrayList<>();
        while (true) {
            String productId = input.readLine("Codigo del producto a devolver (Enter para terminar): ");
            if (productId.isEmpty()) {
                break;
            }
            int quantity = input.readInt("Cantidad: ");
            if (quantity <= 0) {
                System.out.println("La cantidad debe ser mayor que cero.");
                continue;
            }
            for (int i = 0; i < quantity; i++) {
                productIds.add(productId);
            }
        }
        String reason = input.readText("Motivo de la devolucion: ");
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
        int month = input.readInt("Mes (1-12): ");
        int year = input.readInt("Anio: ");
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
