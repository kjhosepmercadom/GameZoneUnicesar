package com.gamezone.service;

import com.gamezone.model.Console;
import com.gamezone.model.Product;
import com.gamezone.model.VideoGame;
import com.gamezone.persistence.ProductPersistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Business rules for the product catalog: registration, listing,
 * and stock validation/update. This is the only layer allowed to
 * talk to ProductPersistence — the UI must go through here.
 *
 * @author Dair
 */
public class ProductService {

    private final ProductPersistence persistence;
    private final List<Product> products;

    /**
     * Creates a new service backed by the given persistence handler,
     * loading the existing catalog immediately.
     *
     * @param persistence the persistence handler used to load and save products
     */
    public ProductService(ProductPersistence persistence) {
        this.persistence = persistence;
        this.products = new ArrayList<>(persistence.loadAll());
    }