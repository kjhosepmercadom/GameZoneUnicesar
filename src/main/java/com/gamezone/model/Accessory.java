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

    /**
     * Registers a console as compatible with this accessory.
     *
     * @param consoleId identifier of the compatible console
     */
    public void addCompatibleConsole(String consoleId) {
        if (consoleId != null && !isCompatibleWith(consoleId)) {
            compatibleConsoleIds.add(consoleId.trim());
        }
    }

    /**
     * Removes a console from the compatibility list.
     *
     * @param consoleId identifier of the console to remove
     */
    public void removeCompatibleConsole(String consoleId) {
        compatibleConsoleIds.removeIf(id -> id.equalsIgnoreCase(consoleId));
    }

    /**
     * Checks whether this accessory works with the given console.
     *
     * @param consoleId identifier of the console
     * @return {@code true} if the console is in the compatibility list
     */
    public boolean isCompatibleWith(String consoleId) {
        if (consoleId == null) {
            return false;
        }
        for (String id : compatibleConsoleIds) {
            if (id.equalsIgnoreCase(consoleId.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Builds the common part of the accessory description, including the
     * compatible consoles. Subclasses extend it with their specific attributes.
     *
     * @return the accessory description
     */
    @Override
    public String getDescription() {
        String compatibility = compatibleConsoleIds.isEmpty() ? "ninguna" : String.join(", ", compatibleConsoleIds);
        return String.format("%s - %s | Precio: $%,.0f | Stock: %d | Compatible con: %s",
                getId(), getTitle(), getPrice(), getStock(), compatibility);
    }
}
