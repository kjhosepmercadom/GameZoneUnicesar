package com.gamezone.model;

import java.util.List;

/**
 * Cable accessory. It is characterized by its length in meters and its
 * connector type (HDMI, USB, optical, among others).
 */
public class Cable extends Accessory {

    private double lengthInMeters;
    private String connectorType;

    /**
     * Creates a cable with all its information.
     *
     * @param id                   unique product identifier
     * @param title                commercial title
     * @param price                unit price in Colombian pesos
     * @param stock                quantity available in inventory
     * @param compatibleConsoleIds identifiers of the compatible consoles
     * @param lengthInMeters       cable length in meters
     * @param connectorType        connector type
     */
    public Cable(String id, String title, double price, int stock,
                 List<String> compatibleConsoleIds, double lengthInMeters, String connectorType) {
        super(id, title, price, stock, compatibleConsoleIds);
        this.lengthInMeters = lengthInMeters;
        this.connectorType = connectorType;
    }

    /**
     * Returns the cable length in meters.
     *
     * @return the length in meters
     */
    public double getLengthInMeters() {
        return lengthInMeters;
    }

    /**
     * Sets the cable length in meters.
     *
     * @param lengthInMeters the new length in meters
     */
    public void setLengthInMeters(double lengthInMeters) {
        this.lengthInMeters = lengthInMeters;
    }

    /**
     * Returns the connector type of the cable.
     *
     * @return the connector type
     */
    public String getConnectorType() {
        return connectorType;
    }

    /**
     * Sets the connector type of the cable.
     *
     * @param connectorType the new connector type
     */
    public void setConnectorType(String connectorType) {
        this.connectorType = connectorType;
    }

    /**
     * Returns the cable accessory type code.
     *
     * @return {@code "CABLE"}
     */
    @Override
    public String getAccessoryType() {
        return "CABLE";
    }

    /**
     * Builds the cable description including length and connector type.
     *
     * @return the full cable description
     */
    @Override
    public String getDescription() {
        return String.format("[Cable] %s | Longitud: %.1f m | Conector: %s",
                super.getDescription(), lengthInMeters, connectorType);
    }
}
