package com.gamezone.model;

import java.util.List;

/**
 * Game controller accessory. It is characterized by its connection type
 * (wireless or wired) and the consoles it is compatible with.
 */
public class Controller extends Accessory {

    private String connectionType;

    /**
     * Creates a controller with all its information.
     *
     * @param id                   unique product identifier
     * @param title                commercial title
     * @param price                unit price in Colombian pesos
     * @param stock                quantity available in inventory
     * @param compatibleConsoleIds identifiers of the compatible consoles
     * @param connectionType       connection type (wireless or wired)
     */
    public Controller(String id, String title, double price, int stock,
                      List<String> compatibleConsoleIds, String connectionType) {
        super(id, title, price, stock, compatibleConsoleIds);
        this.connectionType = connectionType;
    }

    /**
     * Returns the connection type of the controller.
     *
     * @return the connection type
     */
    public String getConnectionType() {
        return connectionType;
    }

    /**
     * Sets the connection type of the controller.
     *
     * @param connectionType the new connection type
     */
    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    /**
     * Returns the controller accessory type code.
     *
     * @return {@code "CONTROLLER"}
     */
    @Override
    public String getAccessoryType() {
        return "CONTROLLER";
    }

    /**
     * Builds the controller description including its connection type.
     *
     * @return the full controller description
     */
    @Override
    public String getDescription() {
        return "[Control] " + super.getDescription() + " | Conexion: " + connectionType;
    }
}
