package com.gamezone.model;

/**
 * Video game console sold by the store. A console is characterized by its
 * brand, its model and the generation it belongs to.
 */
public class Console extends Product {

    private String brand;
    private String model;
    private int generation;

    /**
     * Creates a console with all its information.
     *
     * @param id         unique product identifier
     * @param title      commercial title of the console
     * @param price      unit price in Colombian pesos
     * @param stock      quantity available in inventory
     * @param brand      manufacturer brand
     * @param model      console model
     * @param generation console generation number
     */
    public Console(String id, String title, double price, int stock,
                   String brand, String model, int generation) {
        super(id, title, price, stock);
        this.brand = brand;
        this.model = model;
        this.generation = generation;
    }

    /**
     * Returns the manufacturer brand.
     *
     * @return the brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the manufacturer brand.
     *
     * @param brand the new brand
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Returns the console model.
     *
     * @return the model
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the console model.
     *
     * @param model the new model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Returns the console generation number.
     *
     * @return the generation
     */
    public int getGeneration() {
        return generation;
    }

    /**
     * Sets the console generation number.
     *
     * @param generation the new generation
     */
    public void setGeneration(int generation) {
        this.generation = generation;
    }

    /**
     * Builds the console description including brand, model and generation.
     *
     * @return the full console description
     */
    @Override
    public String getDescription() {
        return String.format("[Consola] %s - %s | Marca: %s | Modelo: %s | Generacion: %d"
                        + " | Precio: $%,.0f | Stock: %d",
                getId(), getTitle(), brand, model, generation, getPrice(), getStock());
    }
}
