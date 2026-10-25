package com.gamezone.ui;

import com.gamezone.model.Customer;
import com.gamezone.model.Seller;
import com.gamezone.service.PersonService;

import java.util.List;

/**
 * Console submenu of the person module: customer registration and the
 * customer and seller listings.
 */
public class PersonMenu {

    private final PersonService personService;
    private final ConsoleInput input;
    private final ErrorPrinter errorPrinter;

    /**
     * Creates the submenu.
     *
     * @param personService service of the person module
     * @param input         reader of the user input
     * @param errorPrinter  printer of the error messages
     */
    public PersonMenu(PersonService personService, ConsoleInput input, ErrorPrinter errorPrinter) {
        this.personService = personService;
        this.input = input;
        this.errorPrinter = errorPrinter;
    }

    /**
     * Shows the submenu and executes the selected option.
     */
    public void show() {
        System.out.println();
        System.out.println("--- Gestion de personas ---");
        System.out.println("1. Registrar cliente");
        System.out.println("2. Listar clientes");
        System.out.println("3. Listar vendedores");
        System.out.println("0. Volver");
        int option = input.readInt("Seleccione una opcion: ");
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
        } catch (RuntimeException e) {
            errorPrinter.print(e);
        }
    }

    /**
     * Prints the registered customers.
     */
    public void printCustomers() {
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

    /**
     * Prints the sellers of the store.
     */
    public void printSellers() {
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

    private void registerCustomer() {
        System.out.println("-- Registrar cliente --");
        String name = input.readText("Nombre completo: ");
        String identification = input.readText("Identificacion: ");
        String phone = input.readText("Telefono: ");
        String email = input.readText("Correo electronico: ");
        personService.registerCustomer(name, identification, phone, email);
        System.out.println("Cliente registrado correctamente.");
    }
}
