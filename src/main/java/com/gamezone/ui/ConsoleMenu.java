package com.gamezone.ui;

import com.gamezone.service.AccessoryService;
import com.gamezone.service.PersonService;
import com.gamezone.service.ProductService;
import com.gamezone.service.PromotionService;
import com.gamezone.service.ReturnService;
import com.gamezone.service.SaleService;
import com.gamezone.service.WarrantyService;

import java.util.Scanner;

/**
 * Main console menu of the system. It shows the main options in Spanish and
 * delegates every module to its own submenu class; the submenus talk only to
 * the service layer and never access the persistence layer directly.
 */
public class ConsoleMenu {

    private final ConsoleInput input;
    private final ProductMenu productMenu;
    private final PersonMenu personMenu;
    private final AccessoryMenu accessoryMenu;
    private final SaleMenu saleMenu;
    private final PromotionMenu promotionMenu;
    private final WarrantyMenu warrantyMenu;
    private final ReturnMenu returnMenu;

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
        this.input = new ConsoleInput(new Scanner(System.in));
        ErrorPrinter errorPrinter = new ErrorPrinter();
        this.productMenu = new ProductMenu(productService, input, errorPrinter);
        this.personMenu = new PersonMenu(personService, input, errorPrinter);
        this.accessoryMenu = new AccessoryMenu(accessoryService, productService, input, errorPrinter);
        this.saleMenu = new SaleMenu(saleService, personService, productService, accessoryService,
                personMenu, productMenu, accessoryMenu, input, errorPrinter);
        this.promotionMenu = new PromotionMenu(promotionService, input, errorPrinter);
        this.warrantyMenu = new WarrantyMenu(warrantyService, input, errorPrinter);
        this.returnMenu = new ReturnMenu(returnService, saleService, personMenu, input, errorPrinter);
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
            int option = input.readInt("Seleccione una opcion: ");
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
                    returnMenu.show();
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
}
