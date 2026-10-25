package com.gamezone.persistence;

import com.gamezone.model.Accessory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Base CSV mapper of the accessories. It writes and reads the columns shared
 * by every accessory ({@code id;title;price;stock;compatibleIds}); each
 * subclass adds the columns of its accessory type.
 */
abstract class AccessoryRecordMapper implements RecordMapper<Accessory> {

    private static final String SEPARATOR = ";";
    private static final String LIST_SEPARATOR = ",";

    /**
     * Uses the accessory type code as discriminator.
     *
     * @param value the accessory to store
     * @return {@code true} if the accessory type matches the type code of this mapper
     */
    @Override
    public boolean supports(Accessory value) {
        return getTypeCode().equals(value.getAccessoryType());
    }

    /**
     * Builds the columns shared by every accessory.
     *
     * @param accessory the accessory to store
     * @return the common columns separated by semicolons
     */
    protected String commonColumns(Accessory accessory) {
        return CsvFile.clean(accessory.getId()) + SEPARATOR + CsvFile.clean(accessory.getTitle()) + SEPARATOR
                + accessory.getPrice() + SEPARATOR + accessory.getStock() + SEPARATOR
                + String.join(LIST_SEPARATOR, accessory.getCompatibleConsoleIds());
    }

    /**
     * Reads the compatible console identifiers of a line.
     *
     * @param fields every column of the line
     * @return the compatible console identifiers, possibly empty
     */
    protected List<String> compatibleIds(String[] fields) {
        return fields[5].isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(fields[5].split(LIST_SEPARATOR)));
    }
}
