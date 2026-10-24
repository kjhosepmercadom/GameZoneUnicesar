package com.gamezone.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for the video game accessories sold by the store.
 * An accessory is a sellable product, so it reuses the common information of
 * {@link Product}, and adds the list of console identifiers it is compatible with.
 */
public abstract class Accessory extends Product {

    private List<String> compatibleConsoleIds;

    /**
     * Creates an accessory with its common information.
     *
     * @param id                   unique product identifier
     * @param title                commercial title
     * @param price                unit price in Colombian pesos
     * @param stock                quantity available in inventory
     * @param compatibleConsoleIds identifiers of the consoles the accessory works with
     */
    public Accessory(String id, String title, double price, int stock, List<String> compatibleConsoleIds) {
        super(id, title, price, stock);
        this.compatibleConsoleIds = compatibleConsoleIds == null
                ? new ArrayList<>() : new ArrayList<>(compatibleConsoleIds);
    }

    /**
     * Returns the identifiers of the compatible consoles.
     *
     * @return a copy of the compatible console identifiers
     */
    public List<String> getCompatibleConsoleIds() {
        return new ArrayList<>(compatibleConsoleIds);
    }

    /**
     * Replaces the identifiers of the compatible consoles.
     *
     * @param compatibleConsoleIds the new compatible console identifiers
     */
    public void setCompatibleConsoleIds(List<String> compatibleConsoleIds) {
        this.compatibleConsoleIds = compatibleConsoleIds == null
                ? new ArrayList<>() : new ArrayList<>(compatibleConsoleIds);
    }

    /**
     * Returns the type of accessory, used to classify and filter accessories.
     *
     * @return the accessory type code (CONTROLLER, CABLE or MEMORY)
     */
    public abstract String getAccessoryType();
}
