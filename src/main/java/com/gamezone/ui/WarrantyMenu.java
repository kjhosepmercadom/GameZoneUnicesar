package com.gamezone.ui;

import com.gamezone.model.Warranty;
import com.gamezone.service.WarrantyService;

import java.util.List;

/**
 * Console submenu of the warranty module: warranty of a product in a sale,
 * and the listings of all, active and soon to expire warranties.
 */
public class WarrantyMenu {

    private final WarrantyService warrantyService;
    private final ConsoleInput input;
    private final ErrorPrinter errorPrinter;

    /**
     * Creates the submenu.
     *
     * @param warrantyService service of the warranty module
     * @param input           reader of the user input
     * @param errorPrinter    printer of the error messages
     */
    public WarrantyMenu(WarrantyService warrantyService, ConsoleInput input, ErrorPrinter errorPrinter) {
        this.warrantyService = warrantyService;
        this.input = input;
        this.errorPrinter = errorPrinter;
    }

    /**
     * Shows the submenu and executes the selected option.
     */
    public void show() {
        System.out.println();
        System.out.println("--- Gestion de garantias ---");
        System.out.println("1. Consultar la garantia de un producto en una venta");
        System.out.println("2. Listar todas las garantias");
        System.out.println("3. Listar garantias vigentes");
        System.out.println("4. Listar garantias proximas a vencer");
        System.out.println("0. Volver");
        int option = input.readInt("Seleccione una opcion: ");
        try {
            switch (option) {
                case 1:
                    String saleId = input.readText("Codigo de la venta: ");
                    String productId = input.readText("Codigo del producto: ");
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
                    int days = input.readInt("Dias de anticipacion: ");
                    printWarranties(warrantyService.listWarrantiesExpiringSoon(days),
                            "No hay garantias que venzan en los proximos " + days + " dias.");
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

    private void printWarranties(List<Warranty> warranties, String emptyMessage) {
        if (warranties.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }
        for (Warranty warranty : warranties) {
            System.out.println(warranty.generateWarrantyCertificate());
        }
    }
}
