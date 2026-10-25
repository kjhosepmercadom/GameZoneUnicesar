package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Customer;
import com.gamezone.model.Product;
import com.gamezone.model.Promotion;
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
    private final PromotionService promotionService;
    private final List<Sale> sales;

    /**
     * Creates the service and loads the stored sales, resolving their
     * references through the product, accessory and person services.
     *
     * @param saleRepository   repository used to persist the sales
     * @param productService   service that manages the product inventory
     * @param personService    service that manages customers and sellers
     * @param accessoryService service that manages the accessory inventory
     * @param promotionService service that selects the best promotion of each sale
     */
    public SaleService(SaleRepository saleRepository, ProductService productService, PersonService personService,
                       AccessoryService accessoryService, PromotionService promotionService) {
        this.saleRepository = saleRepository;
        this.productService = productService;
        this.personService = personService;
        this.accessoryService = accessoryService;
        this.promotionService = promotionService;
        this.sales = saleRepository.loadAll(personService.listCustomers(), personService.listSellers(), allItems());
    }

    /**
     * Registers a new sale of products and accessories. The rules applied are:
     * the sale must contain at least one item, the customer and the seller must
     * exist, every item must exist and have enough stock. The best active
     * promotion is applied, the stock of every item is deducted through the
     * service of its type and the sale is stored.
     *
     * @param customerId identification of the customer
     * @param sellerCode employee code of the seller
     * @param itemIds    identifiers of the products or accessories bought, one entry per unit
     * @return the registered sale
     * @throws IllegalArgumentException if any business rule is violated
     */
    public Sale registerSale(String customerId, String sellerCode, List<String> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) {
            throw new IllegalArgumentException("La venta debe contener al menos un producto.");
        }
        Customer customer = findCustomer(customerId);
        Seller seller = findSeller(sellerCode);
        List<Product> items = resolveItems(itemIds);
        Map<String, Integer> quantities = countQuantities(items);
        validateStock(items, quantities);

        Sale sale = new Sale(nextSaleId(), LocalDate.now(), customer, seller, items);
        Promotion promotion = promotionService.findBestPromotionFor(sale);
        if (promotion != null) {
            sale.setAppliedPromotionName(promotion.getName());
            sale.setDiscountAmount(promotion.calculateDiscount(sale));
        }
        updateInventory(items, quantities);
        sales.add(sale);
        saleRepository.saveAll(sales);
        return sale;
    }

    /**
     * Finds a sale by its identifier, ignoring letter case.
     *
     * @param saleId the sale identifier
     * @return the sale found, or {@code null} if it does not exist
     */
    public Sale findSaleById(String saleId) {
        if (saleId == null) {
            return null;
        }
        for (Sale sale : sales) {
            if (sale.getId().equalsIgnoreCase(saleId.trim())) {
                return sale;
            }
        }
        return null;
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

    private Customer findCustomer(String customerId) {
        Customer customer = personService.findCustomerById(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("No existe un cliente con identificacion " + customerId + ".");
        }
        return customer;
    }

    private Seller findSeller(String sellerCode) {
        Seller seller = personService.findSellerByEmployeeCode(sellerCode);
        if (seller == null) {
            throw new IllegalArgumentException("No existe un vendedor con codigo " + sellerCode + ".");
        }
        return seller;
    }

    private List<Product> resolveItems(List<String> itemIds) {
        List<Product> items = new ArrayList<>();
        for (String itemId : itemIds) {
            Product item = productService.findById(itemId);
            if (item == null) {
                item = accessoryService.findById(itemId);
            }
            if (item == null) {
                throw new IllegalArgumentException("No existe un producto con codigo " + itemId + ".");
            }
            items.add(item);
        }
        return items;
    }

    private Map<String, Integer> countQuantities(List<Product> items) {
        Map<String, Integer> quantities = new LinkedHashMap<>();
        for (Product item : items) {
            quantities.merge(item.getId(), 1, Integer::sum);
        }
        return quantities;
    }

    private void validateStock(List<Product> items, Map<String, Integer> quantities) {
        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            Product item = findItem(items, entry.getKey());
            if (!item.hasEnoughStock(entry.getValue())) {
                throw new IllegalArgumentException("Stock insuficiente para " + item.getTitle()
                        + " (disponible: " + item.getStock() + ", solicitado: " + entry.getValue() + ").");
            }
        }
    }

    private void updateInventory(List<Product> items, Map<String, Integer> quantities) {
        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            if (findItem(items, entry.getKey()) instanceof Accessory) {
                accessoryService.updateStock(entry.getKey(), entry.getValue());
            } else {
                productService.updateStock(entry.getKey(), entry.getValue());
            }
        }
    }

    private Product findItem(List<Product> items, String itemId) {
        for (Product item : items) {
            if (item.getId().equals(itemId)) {
                return item;
            }
        }
        return null;
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
