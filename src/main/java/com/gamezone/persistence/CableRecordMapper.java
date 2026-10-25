package com.gamezone.persistence;

import com.gamezone.model.Accessory;
import com.gamezone.model.Cable;

/**
 * CSV mapper of cables (accessory type {@code CABLE}).
 */
class CableRecordMapper extends AccessoryRecordMapper {

    private static final String SEPARATOR = ";";

    /**
     * Returns the discriminator of cables.
     *
     * @return {@code "CABLE"}
     */
    @Override
    public String getTypeCode() {
        return "CABLE";
    }

    /**
     * Builds the columns of a cable: the common accessory columns and its own ones.
     *
     * @param value the cable to store
     * @return the columns separated by semicolons
     */
    @Override
    public String toColumns(Accessory value) {
        Cable cable = (Cable) value;
        return commonColumns(cable) + SEPARATOR + cable.getLengthInMeters() + SEPARATOR + CsvFile.clean(cable.getConnectorType());
    }

    /**
     * Rebuilds a cable from the columns of a line.
     *
     * @param fields every column of the line
     * @return the cable
     */
    @Override
    public Accessory fromFields(String[] fields) {
        return new Cable(fields[1], fields[2], Double.parseDouble(fields[3]), Integer.parseInt(fields[4]),
                compatibleIds(fields), Double.parseDouble(fields[6]), fields[7]);
    }
}
