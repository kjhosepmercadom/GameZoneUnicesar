package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Product;
import com.gamezone.model.Return;
import com.gamezone.model.Sale;
import com.gamezone.persistence.ReturnRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Applies the business rules of the return module: a return must reference
 * an existing sale, be registered within thirty days of the sale and contain
 * only products that belong to that sale. Returned units go back to the
 * inventory. The service also builds the monthly balance of sales and returns.
 */
public class ReturnService {

    private final ReturnRepository returnRepository;
    private final SaleService saleService;
    private final ProductService productService;
    private final AccessoryService accessoryService;
    private final List<Return> returns;

    /**
     * Creates the service and loads the stored returns.
     *
     * @param returnRepository repository used to persist the returns
     * @param saleService      service used to find the original sales
     * @param productService   service used to restore the stock of returned products
     * @param accessoryService service used to restore the stock of returned accessories
     */
    public ReturnService(ReturnRepository returnRepository, SaleService saleService,
                         ProductService productService, AccessoryService accessoryService) {
        this.returnRepository = returnRepository;
        this.saleService = saleService;
        this.productService = productService;
        this.accessoryService = accessoryService;
        this.returns = returnRepository.loadAll(saleService.listAllSales());
    }

    /**
     * Registers a return of products of a previous sale. The sale must exist,
     * be within the thirty day return period and contain the returned products;
     * the returned units go back to the inventory.
     *
     * @param saleId     identifier of the original sale
     * @param productIds identifiers of the returned products, one entry per unit
     * @param reason     reason of the return
     * @return the registered return
     * @throws IllegalArgumentException if any rule is violated
     */
    public Return registerReturn(String saleId, List<String> productIds, String reason) {
        Sale sale = saleService.findSaleById(saleId);
        if (sale == null) {
            throw new IllegalArgumentException("No existe una venta con codigo " + saleId + ".");
        }
        if (!sale.canBeReturned()) {
            throw new IllegalArgumentException("La venta " + sale.getId()
                    + " supero el plazo de 30 dias para devoluciones.");
        }
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("La devolucion debe incluir al menos un producto.");
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo motivo es obligatorio.");
        }
        List<Product> returned = new ArrayList<>();
        Map<String, Integer> quantities = new LinkedHashMap<>();
        for (String productId : productIds) {
            Product product = findInSale(sale, productId);
            if (product == null) {
                throw new IllegalArgumentException("El producto " + productId + " no pertenece a la venta "
                        + sale.getId() + ".");
            }
            returned.add(product);
            quantities.merge(product.getId(), 1, Integer::sum);
        }
        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            int available = countInSale(sale, entry.getKey()) - countReturned(sale, entry.getKey());
            if (entry.getValue() > available) {
                throw new IllegalArgumentException("Solo se pueden devolver " + available + " unidades de "
                        + entry.getKey() + " en la venta " + sale.getId() + ".");
            }
        }

        Return productReturn = new Return(nextReturnId(), LocalDate.now(), sale, returned, reason.trim());
        productReturn.calculateRefundAmount();
        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            if (findInSale(sale, entry.getKey()) instanceof Accessory) {
                accessoryService.restoreStock(entry.getKey(), entry.getValue());
            } else {
                productService.restoreStock(entry.getKey(), entry.getValue());
            }
        }
        returns.add(productReturn);
        returnRepository.saveAll(returns);
        return productReturn;
    }

    /**
     * Returns every registered return.
     *
     * @return a copy of the return list
     */
    public List<Return> viewAllReturns() {
        return new ArrayList<>(returns);
    }

    /**
     * Returns the returns whose original sale belongs to a customer.
     *
     * @param customerId identification of the customer
     * @return the returns of the customer, possibly empty
     */
    public List<Return> viewReturnsByCustomer(String customerId) {
        List<Return> result = new ArrayList<>();
        for (Return productReturn : returns) {
            if (productReturn.getSale().getCustomer().getIdentification().equals(customerId.trim())) {
                result.add(productReturn);
            }
        }
        return result;
    }

    /**
     * Returns the returns associated with a sale.
     *
     * @param saleId identifier of the sale
     * @return the returns of the sale, possibly empty
     */
    public List<Return> viewReturnsBySale(String saleId) {
        List<Return> result = new ArrayList<>();
        for (Return productReturn : returns) {
            if (productReturn.getSale().getId().equalsIgnoreCase(saleId.trim())) {
                result.add(productReturn);
            }
        }
        return result;
    }

    /**
     * Calculates the total sold in a month, using the final total of each
     * sale (with discounts and extended warranties).
     *
     * @param month month number, from 1 to 12
     * @param year  four digit year
     * @return the sales total in Colombian pesos
     */
    public double calculateMonthlySales(int month, int year) {
        double salesTotal = 0;
        for (Sale sale : saleService.listAllSales()) {
            if (isInMonth(sale.getDate(), month, year)) {
                salesTotal += sale.calculateFinalTotal();
            }
        }
        return salesTotal;
    }

    /**
     * Calculates the net balance of a month: the total of the sales minus the
     * total refunded in the returns of the same month.
     *
     * @param month month number, from 1 to 12
     * @param year  four digit year
     * @return the net balance in Colombian pesos
     */
    public double generateMonthlyBalance(int month, int year) {
        double returnsTotal = 0;
        for (Return productReturn : returns) {
            if (isInMonth(productReturn.getDate(), month, year)) {
                returnsTotal += productReturn.getRefundAmount();
            }
        }
        return calculateMonthlySales(month, year) - returnsTotal;
    }

    private boolean isInMonth(LocalDate date, int month, int year) {
        return date.getMonthValue() == month && date.getYear() == year;
    }

    private Product findInSale(Sale sale, String productId) {
        for (Product product : sale.getProducts()) {
            if (product.getId().equalsIgnoreCase(productId.trim())) {
                return product;
            }
        }
        return null;
    }

    private int countInSale(Sale sale, String productId) {
        int count = 0;
        for (Product product : sale.getProducts()) {
            if (product.getId().equals(productId)) {
                count++;
            }
        }
        return count;
    }

    private int countReturned(Sale sale, String productId) {
        int count = 0;
        for (Return productReturn : returns) {
            if (productReturn.getSale().getId().equals(sale.getId())) {
                for (Product product : productReturn.getReturnedProducts()) {
                    if (product.getId().equals(productId)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private String nextReturnId() {
        int max = 0;
        for (Return productReturn : returns) {
            try {
                max = Math.max(max, Integer.parseInt(productReturn.getId().substring(1)));
            } catch (RuntimeException e) {
                // identifiers that do not follow the R0000 pattern are ignored
            }
        }
        return String.format("R%04d", max + 1);
    }
}
