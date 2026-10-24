package com.gamezone.ui;

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
            System.out.println("0. Salir");
            int option = readInt("Seleccione una opcion: ");
            switch (option) {
                case 0:
                    running = false;
                    System.out.println("Gracias por usar GameZone Unicesar. Hasta pronto.");
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
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
}
