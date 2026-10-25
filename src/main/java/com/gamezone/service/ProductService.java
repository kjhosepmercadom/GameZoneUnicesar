package com.gamezone.service;

import com.gamezone.exceptions.BusinessRuleException;
import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import com.gamezone.persistence.ProductRepository;
import com.gamezone.validation.ProductValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * Applies the business rules of the product module: registration of video
 * games and consoles, inventory queries and stock updates. Every change is
 * persisted immediately through the {@link ProductRepository}.
 */
public class ProductService implements InventoryService {

    private final ProductRepository productRepository;
    private final List<Product> products;

    /**
     * Creates the service and loads the stored products.
     *
     * @param productRepository repository used to persist the products
     */
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.products = productRepository.loadAll();
    }

    /**
     * Registers a new video game in the inventory.
     *
     * @param id        unique product identifier
     * @param title     commercial title
     * @param price     unit price in Colombian pesos
     * @param stock     initial stock
     * @param platform  platform the game was developed for
     * @param genre     genre of the game
     * @param ageRating recommended age rating
     * @return the registered video game
     * @throws InvalidDataException  if any data is invalid
     * @throws BusinessRuleException if the identifier already exists
     */
    public VideoGame registerVideoGame(String id, String title, double price, int stock,
                                       String platform, String genre, String ageRating) {
        ProductValidator.validateProductData(id, title, price, stock);
        ensureUniqueId(id);
        ProductValidator.requireText(platform, "plataforma");
        ProductValidator.requireText(genre, "genero");
        ProductValidator.requireText(ageRating, "clasificacion de edad");
        VideoGame videoGame = new VideoGame(id.trim(), title.trim(), price, stock,
                platform.trim(), genre.trim(), ageRating.trim());
        products.add(videoGame);
        productRepository.saveAll(products);
        return videoGame;
    }

    /**
     * Registers a new console in the inventory.
     *
     * @param id         unique product identifier
     * @param title      commercial title
     * @param price      unit price in Colombian pesos
     * @param stock      initial stock
     * @param brand      manufacturer brand
     * @param model      console model
     * @param generation console generation number
     * @return the registered console
     * @throws InvalidDataException  if any data is invalid
     * @throws BusinessRuleException if the identifier already exists
     */
    public Console registerConsole(String id, String title, double price, int stock,
                                   String brand, String model, int generation) {
        ProductValidator.validateProductData(id, title, price, stock);
        ensureUniqueId(id);
        ProductValidator.requireText(brand, "marca");
        ProductValidator.requireText(model, "modelo");
        if (generation <= 0) {
            throw new InvalidDataException("generacion", "debe ser mayor que cero.");
        }
        Console console = new Console(id.trim(), title.trim(), price, stock,
                brand.trim(), model.trim(), generation);
        products.add(console);
        productRepository.saveAll(products);
        return console;
    }

    /**
     * Returns every product registered in the inventory.
     *
     * @return a copy of the product list
     */
    public List<Product> listAllProducts() {
        return new ArrayList<>(products);
    }

    /**
     * Finds a product by its identifier, ignoring letter case.
     *
     * @param id the product identifier
     * @return the product found, or {@code null} if it does not exist
     */
    @Override
    public Product findById(String id) {
        if (id == null) {
            return null;
        }
        for (Product product : products) {
            if (product.getId().equalsIgnoreCase(id.trim())) {
                return product;
            }
        }
        return null;
    }

    /**
     * Checks whether a product has enough stock for the requested quantity.
     *
     * @param productId the product identifier
     * @param quantity  the quantity requested
     * @return {@code true} if the product exists and its stock covers the quantity
     */
    public boolean hasEnoughStock(String productId, int quantity) {
        Product product = findById(productId);
        return product != null && product.hasEnoughStock(quantity);
    }

    /**
     * Deducts a sold quantity from the stock of a product and persists the change.
     *
     * @param productId the product identifier
     * @param quantity  the quantity sold
     * @throws com.gamezone.exceptions.ResourceNotFoundException if the product does not exist
     * @throws BusinessRuleException                             if the stock is insufficient
     */
    @Override
    public void updateStock(String productId, int quantity) {
        Product product = findById(productId);
        ProductValidator.validateProductExists(product, productId);
        ProductValidator.validateStockAvailability(product, quantity);
        product.decreaseStock(quantity);
        productRepository.saveAll(products);
    }

    /**
     * Adds returned units back to the stock of a product and persists the change.
     *
     * @param productId the product identifier
     * @param quantity  the quantity returned, must be positive
     * @throws com.gamezone.exceptions.ResourceNotFoundException if the product does not exist
     * @throws InvalidDataException                              if the quantity is not positive
     */
    @Override
    public void restoreStock(String productId, int quantity) {
        Product product = findById(productId);
        ProductValidator.validateProductExists(product, productId);
        if (quantity <= 0) {
            throw new InvalidDataException("cantidad a restaurar", "debe ser mayor que cero.");
        }
        product.increaseStock(quantity);
        productRepository.saveAll(products);
    }

    private void ensureUniqueId(String id) {
        if (findById(id) != null) {
            throw new BusinessRuleException("Ya existe un producto con codigo " + id.trim() + ".");
        }
    }
}
