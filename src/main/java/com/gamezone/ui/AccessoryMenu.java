package com.gamezone.ui;

import com.gamezone.model.Accessory;
import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.service.AccessoryService;
import com.gamezone.service.ProductService;

import java.util.ArrayList;
import java.util.List;

/**
 * Console submenu of the accessory module: registration of controllers,
 * cables and memories, listings by type and by compatible console.
 */
public class AccessoryMenu {

    private final AccessoryService accessoryService;
    private final ProductService productService;
    private final ConsoleInput input;
    private final ErrorPrinter errorPrinter;

    /**
     * Creates the submenu.
     *
     * @param accessoryService service of the accessory module
     * @param productService   service used to list the consoles of the inventory
     * @param input            reader of the user input
     * @param errorPrinter     printer of the error messages
     */
    public AccessoryMenu(AccessoryService accessoryService, ProductService productService,
                         ConsoleInput input, ErrorPrinter errorPrinter) {
        this.accessoryService = accessoryService;
        this.productService = productService;
        this.input = input;
        this.errorPrinter = errorPrinter;
    }

    /**
     * Shows the submenu and executes the selected option.
     */
    public void show() {
        System.out.println();
        System.out.println("--- Gestion de accesorios ---");
        System.out.println("1. Registrar control");
        System.out.println("2. Registrar cable");
        System.out.println("3. Registrar memoria");
        System.out.println("4. Listar todos los accesorios");
        System.out.println("5. Listar accesorios por tipo");
        System.out.println("6. Consultar accesorios compatibles con una consola");
        System.out.println("0. Volver");
        int option = input.readInt("Seleccione una opcion: ");
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
                    String consoleId = input.readText("Codigo de la consola: ");
                    printAccessories(accessoryService.findAccessoriesCompatibleWith(consoleId),
                            "No hay accesorios compatibles con la consola " + consoleId + ".");
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
     * Prints a list of accessories, or a message when it is empty.
     *
     * @param accessories  the accessories to print
     * @param emptyMessage message shown when the list is empty
     */
    public void printAccessories(List<Accessory> accessories, String emptyMessage) {
        System.out.println("-- Accesorios --");
        if (accessories.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }
        for (Accessory accessory : accessories) {
            System.out.println(accessory.getDescription());
        }
    }

    private void registerController() {
        System.out.println("-- Registrar control --");
        String id = input.readText("Codigo: ");
        String title = input.readText("Titulo: ");
        double price = input.readDouble("Precio: ");
        int stock = input.readInt("Cantidad en inventario: ");
        List<String> consoles = readCompatibleConsoles();
        String connection = input.readText("Tipo de conexion (Inalambrico/Alambrico): ");
        accessoryService.registerController(id, title, price, stock, consoles, connection);
        System.out.println("Control registrado correctamente.");
    }

    private void registerCable() {
        System.out.println("-- Registrar cable --");
        String id = input.readText("Codigo: ");
        String title = input.readText("Titulo: ");
        double price = input.readDouble("Precio: ");
        int stock = input.readInt("Cantidad en inventario: ");
        List<String> consoles = readCompatibleConsoles();
        double length = input.readDouble("Longitud en metros: ");
        String connector = input.readText("Tipo de conector (HDMI, USB, Optico...): ");
        accessoryService.registerCable(id, title, price, stock, consoles, length, connector);
        System.out.println("Cable registrado correctamente.");
    }

    private void registerMemory() {
        System.out.println("-- Registrar memoria --");
        String id = input.readText("Codigo: ");
        String title = input.readText("Titulo: ");
        double price = input.readDouble("Precio: ");
        int stock = input.readInt("Cantidad en inventario: ");
        List<String> consoles = readCompatibleConsoles();
        int capacity = input.readInt("Capacidad en GB: ");
        String memoryType = input.readText("Tipo de memoria (SD, microSD, Tarjeta interna): ");
        accessoryService.registerMemory(id, title, price, stock, consoles, capacity, memoryType);
        System.out.println("Memoria registrada correctamente.");
    }

    private List<String> readCompatibleConsoles() {
        printConsoles();
        String line = input.readLine("Codigos de consolas compatibles separados por coma (Enter si ninguna): ");
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

    private String readAccessoryType() {
        int option = input.readInt("Tipo (1=Control, 2=Cable, 3=Memoria): ");
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
}
