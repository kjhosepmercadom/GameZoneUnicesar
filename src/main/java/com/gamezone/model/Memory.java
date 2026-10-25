package com.gamezone.model;

import java.util.List;

/**
 * Memory accessory. It is characterized by its storage capacity in gigabytes
 * and its memory type (SD, microSD, internal card).
 */
public class Memory extends Accessory {

    private int capacityInGb;
    private String memoryType;

    /**
     * Creates a memory with all its information.
     *
     * @param id                   unique product identifier
     * @param title                commercial title
     * @param price                unit price in Colombian pesos
     * @param stock                quantity available in inventory
     * @param compatibleConsoleIds identifiers of the compatible consoles
     * @param capacityInGb         storage capacity in gigabytes
     * @param memoryType           memory type
     */
    public Memory(String id, String title, double price, int stock,
                  List<String> compatibleConsoleIds, int capacityInGb, String memoryType) {
        super(id, title, price, stock, compatibleConsoleIds);
        this.capacityInGb = capacityInGb;
        this.memoryType = memoryType;
    }

    /**
     * Returns the storage capacity in gigabytes.
     *
     * @return the capacity in gigabytes
     */
    public int getCapacityInGb() {
        return capacityInGb;
    }

    /**
     * Sets the storage capacity in gigabytes.
     *
     * @param capacityInGb the new capacity in gigabytes
     */
    public void setCapacityInGb(int capacityInGb) {
        this.capacityInGb = capacityInGb;
    }

    /**
     * Returns the memory type.
     *
     * @return the memory type
     */
    public String getMemoryType() {
        return memoryType;
    }

    /**
     * Sets the memory type.
     *
     * @param memoryType the new memory type
     */
    public void setMemoryType(String memoryType) {
        this.memoryType = memoryType;
    }

    /**
     * Returns the memory accessory type code.
     *
     * @return {@code "MEMORY"}
     */
    @Override
    public String getAccessoryType() {
        return "MEMORY";
    }

    /**
     * Builds the memory description including capacity and type.
     *
     * @return the full memory description
     */
    @Override
    public String getDescription() {
        return String.format("[Memoria] %s | Capacidad: %d GB | Tipo: %s",
                super.getDescription(), capacityInGb, memoryType);
    }
}
