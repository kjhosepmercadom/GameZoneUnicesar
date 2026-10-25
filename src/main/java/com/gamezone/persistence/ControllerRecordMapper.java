package com.gamezone.persistence;

import com.gamezone.model.Accessory;
import com.gamezone.model.Controller;

/**
 * CSV mapper of controllers (accessory type {@code CONTROLLER}).
 */
class ControllerRecordMapper extends AccessoryRecordMapper {

    private static final String SEPARATOR = ";";

    /**
     * Returns the discriminator of controllers.
     *
     * @return {@code "CONTROLLER"}
     */
    @Override
    public String getTypeCode() {
        return "CONTROLLER";
    }

    /**
     * Builds the columns of a controller: the common accessory columns and its own ones.
     *
     * @param value the controller to store
     * @return the columns separated by semicolons
     */
    @Override
    public String toColumns(Accessory value) {
        Controller controller = (Controller) value;
        return commonColumns(controller) + SEPARATOR + CsvFile.clean(controller.getConnectionType());
    }

    /**
     * Rebuilds a controller from the columns of a line.
     *
     * @param fields every column of the line
     * @return the controller
     */
    @Override
    public Accessory fromFields(String[] fields) {
        return new Controller(fields[1], fields[2], Double.parseDouble(fields[3]), Integer.parseInt(fields[4]),
                compatibleIds(fields), fields[6]);
    }
}
