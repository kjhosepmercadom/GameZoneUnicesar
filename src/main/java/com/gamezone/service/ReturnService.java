package com.gamezone.service;

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
    private final List<Return> returns;

    /**
     * Creates the service and loads the stored returns.
     *
     * @param returnRepository repository used to persist the returns
     * @param saleService      service used to find the original sales
     * @param productService   service used to restore the stock of returned products
     */
    public ReturnService(ReturnRepository returnRepository, SaleService saleService, ProductService productService) {
        this.returnRepository = returnRepository;
        this.saleService = saleService;
        this.productService = productService;
        this.returns = returnRepository.loadAll(saleService.listAllSales(), productService.listAllProducts());
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

        Return productReturn = new Return(nextReturnId(), LocalDate.now(), sale, returned, reason.trim());
        productReturn.calculateRefundAmount();
        for (Map.Entry<String, Integer> entry : quantities.entrySet()) {
            productService.restoreStock(entry.getKey(), entry.getValue());
        }
        returns.add(productReturn);
        returnRepository.saveAll(returns);
        return productReturn;
    }

    private Product findInSale(Sale sale, String productId) {
        for (Product product : sale.getProducts()) {
            if (product.getId().equalsIgnoreCase(productId.trim())) {
                return product;
            }
        }
        return null;
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
