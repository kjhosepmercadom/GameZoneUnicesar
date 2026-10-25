package com.gamezone.ui;

import com.gamezone.model.Promotion;
import com.gamezone.service.PromotionService;

import java.time.LocalDate;
import java.util.List;

/**
 * Console submenu of the promotion module: registration of the three
 * promotion types and the promotion listings.
 */
public class PromotionMenu {

    private final PromotionService promotionService;
    private final ConsoleInput input;
    private final ErrorPrinter errorPrinter;

    /**
     * Creates the submenu.
     *
     * @param promotionService service of the promotion module
     * @param input            reader of the user input
     * @param errorPrinter     printer of the error messages
     */
    public PromotionMenu(PromotionService promotionService, ConsoleInput input, ErrorPrinter errorPrinter) {
        this.promotionService = promotionService;
        this.input = input;
        this.errorPrinter = errorPrinter;
    }

    /**
     * Shows the submenu and executes the selected option.
     */
    public void show() {
        System.out.println();
        System.out.println("--- Gestion de promociones ---");
        System.out.println("1. Registrar promocion por porcentaje");
        System.out.println("2. Registrar promocion por categoria");
        System.out.println("3. Registrar promocion por volumen de compra");
        System.out.println("4. Listar todas las promociones");
        System.out.println("5. Listar promociones vigentes");
        System.out.println("0. Volver");
        int option = input.readInt("Seleccione una opcion: ");
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
            errorPrinter.print(e);
        }
    }

    private void registerPercentageDiscount() {
        System.out.println("-- Registrar promocion por porcentaje --");
        String id = input.readText("Codigo: ");
        String name = input.readText("Nombre: ");
        LocalDate start = input.readDate("Fecha de inicio (AAAA-MM-DD): ");
        LocalDate end = input.readDate("Fecha de fin (AAAA-MM-DD): ");
        double percentage = input.readDouble("Porcentaje de descuento: ");
        promotionService.registerPercentageDiscount(id, name, start, end, percentage);
        System.out.println("Promocion registrada correctamente.");
    }

    private void registerCategoryDiscount() {
        System.out.println("-- Registrar promocion por categoria --");
        String id = input.readText("Codigo: ");
        String name = input.readText("Nombre: ");
        LocalDate start = input.readDate("Fecha de inicio (AAAA-MM-DD): ");
        LocalDate end = input.readDate("Fecha de fin (AAAA-MM-DD): ");
        double percentage = input.readDouble("Porcentaje de descuento: ");
        int option = input.readInt("Categoria (1=Videojuegos, 2=Consolas, 3=Accesorios): ");
        String category = option == 1 ? "VIDEOGAME" : option == 2 ? "CONSOLE" : option == 3 ? "ACCESSORY" : "";
        promotionService.registerCategoryDiscount(id, name, start, end, percentage, category);
        System.out.println("Promocion registrada correctamente.");
    }

    private void registerBulkPurchaseDiscount() {
        System.out.println("-- Registrar promocion por volumen de compra --");
        String id = input.readText("Codigo: ");
        String name = input.readText("Nombre: ");
        LocalDate start = input.readDate("Fecha de inicio (AAAA-MM-DD): ");
        LocalDate end = input.readDate("Fecha de fin (AAAA-MM-DD): ");
        int minimum = input.readInt("Cantidad minima de productos: ");
        double percentage = input.readDouble("Porcentaje de descuento: ");
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
}
