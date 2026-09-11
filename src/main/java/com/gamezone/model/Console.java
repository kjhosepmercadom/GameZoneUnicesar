package com.gamezone.model;

/**
 * Represents a console product, identified by its brand and generation.
 *
 * @author Dair
 */
public class Console extends Product {

    private String brand;
    private String generation;

    public Console(String id, String name, double price, int stock,
                   String brand, String generation) {
        super(id, name, price, stock);
        this.brand = brand;
        this.generation = generation;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    @Override
    public String getDescription() {
        return getName() + " (Console - " + brand + ", " + generation + ")";
    }
}