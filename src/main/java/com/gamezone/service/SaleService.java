package com.gamezone.service;

import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;
import com.gamezone.persistence.SaleRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Applies the business rules of the sales module. A sale can only be
 * registered when it contains at least one product and the inventory covers
 * every requested quantity; once registered, the sold quantities are deducted
 * from the inventory automatically.
 */
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductService productService;
    private final PersonService personService;
    private final AccessoryService accessoryService;
    private final List<Sale> sales;

    /**
     * Creates the service and loads the stored sales, resolving their
     * references through the product, accessory and person services.
     *
     * @param saleRepository   repository used to persist the sales
     * @param productService   service that manages the product inventory
     * @param personService    service that manages customers and sellers
     * @param accessoryService service that manages the accessory inventory
     */
    public SaleService(SaleRepository saleRepository, ProductService productService, PersonService personService,
                       AccessoryService accessoryService) {
        this.saleRepository = saleRepository;
        this.productService = productService;
        this.personService = personService;
        this.accessoryService = accessoryService;
        this.sales = saleRepository.loadAll(personService.listCustomers(), personService.listSellers(), allItems());
    }

    /**
     * Registers a new sale. The rules applied are: the sale must contain at
     * least one product, the customer and the seller must exist, every product
     * must exist and have enough stock. If all rules pass, the stock of every
     * product is deducted and the sale is stored.
     *
     * @param customerId       identification of the customer
     * @param sellerCode       employee code of the seller
     * @param productIds       identifiers of the products bought, one entry per unit
     * @return the registered sale
     * @throws IllegalArgumentException if any business rule is violated
     */
    public Sale registerSale(String customerId, String sellerCode, List<String> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("La venta debe contener al menos un producto.");
        }
        Customer customer = personService.findCustomerById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("No existe un cliente con identificacion " + customerId + ".");
        }
        Seller seller = personService.findSellerByEmployeeCode(sellerCode);
        if (seller == null) {
            throw new IllegalArgumentException("No existe un vendedor con codigo " + sellerCode + ".");
        }

        List<Product> products = new ArrayList<>();
        Map<String, Integer> quantities = new LinkedHashMap<>();
        for (String productId : productIds) {
            Product product = productService.findById(productId);
            if (product == null) {
                throw new IllegalArgumentException("No existe un producto con codigo " + productId + ".");
            }
            products.add(product);
            quantities.merge(product.getId(), 1, Integer::sum);
        }
        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            if (!productService.hasEnoughStock(entry.getKey(), entry.getValue())) {
                Product product = productService.findById(entry.getKey());
                throw new IllegalArgumentException("Stock insuficiente para " + product.getTitle()
                        + " (disponible: " + product.getStock() + ", solicitado: " + entry.getValue() + ").");
            }
        }

        Sale sale = new Sale(nextSaleId(), LocalDate.now(), customer, seller, products);
        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            productService.updateStock(entry.getKey(), entry.getValue());
        }
        sales.add(sale);
        saleRepository.saveAll(sales);
        return sale;
    }

    /**
     * Returns the complete history of registered sales.
     *
     * @return a copy of the sales list
     */
    public List<Sale> listAllSales() {
        return new ArrayList<>(sales);
    }

    /**
     * Returns the purchase history of a specific customer.
     *
     * @param customerId identification of the customer
     * @return the sales made by the customer, possibly empty
     */
    public List<Sale> findSalesByCustomer(String customerId) {
        List<Sale> result = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getCustomer().getIdentification().equals(customerId.trim())) {
                result.add(sale);
            }
        }
        return result;
    }

    /**
     * Returns the sales attended by a specific seller.
     *
     * @param sellerCode employee code of the seller
     * @return the sales attended by the seller, possibly empty
     */
    public List<Sale> findSalesBySeller(String sellerCode) {
        List<Sale> result = new ArrayList<>();
        for (Sale sale : sales) {
            if (sale.getSeller().getEmployeeCode().equalsIgnoreCase(sellerCode.trim())) {
                result.add(sale);
            }
        }
        return result;
    }

    private List<Product> allItems() {
        List<Product> items = new ArrayList<>(productService.listAllProducts());
        items.addAll(accessoryService.listAllAccessories());
        return items;
    }

    private String nextSaleId() {
        int max = 0;
        for (Sale sale : sales) {
            try {
                max = Math.max(max, Integer.parseInt(sale.getId().substring(1)));
            } catch (RuntimeException e) {
                // identifiers that do not follow the S0000 pattern are ignored
            }
        }
        return String.format("S%04d", max + 1);
    }
}
