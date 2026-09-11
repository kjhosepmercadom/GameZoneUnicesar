package com.gamezone.model;

/**
 * Represents a console product, identified by its brand and generation.
 *
 * @author Dair
 */
public class Console extends Product {

    private String brand;
    private String generation;

    /**
     * Creates a new console.
     *
     * @param id         unique identifier of the product
     * @param name       display name of the product
     * @param price      unit price, must not be negative
     * @param stock      available quantity, must not be negative
     * @param brand      manufacturer brand of the console
     * @param generation hardware generation of the console
     */
    public Console(String id, String name, double price, int stock,
                   String brand, String generation) {
        super(id, name, price, stock);
        this.brand = brand;
        this.generation = generation;
    }

    /**
     * Returns the manufacturer brand of the console.
     *
     * @return the brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the manufacturer brand of the console.
     *
     * @param brand the new brand
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Returns the hardware generation of the console.
     *
     * @return the generation
     */
    public String getGeneration() {
        return generation;
    }

    /**
     * Sets the hardware generation of the console.
     *
     * @param generation the new generation
     */
    public void setGeneration(String generation) {
        this.generation =