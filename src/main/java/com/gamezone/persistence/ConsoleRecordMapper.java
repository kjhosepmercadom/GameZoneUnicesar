package com.gamezone.persistence;

import com.gamezone.model.Console;
import com.gamezone.model.Product;

/**
 * CSV mapper of consoles: {@code CONSOLE;id;title;price;stock;brand;model;generation}.
 */
class ConsoleRecordMapper implements RecordMapper<Product> {

    private static final String SEPARATOR = ";";

    /**
     * Returns the discriminator of consoles.
     *
     * @return {@code "CONSOLE"}
     */
    @Override
    public String getTypeCode() {
        return "CONSOLE";
    }

    /**
     * Tells whether the product is a console.
     *
     * @param value the product to store
     * @return {@code true} for consoles
     */
    @Override
    public boolean supports(Product value) {
        return value instanceof Console;
    }

    /**
     * Builds the columns of a console.
     *
     * @param value the console to store
     * @return the columns separated by semicolons
     */
    @Override
    public String toColumns(Product value) {
        Console console = (Console) value;
        return CsvFile.clean(console.getId()) + SEPARATOR + CsvFile.clean(console.getTitle()) + SEPARATOR
                + console.getPrice() + SEPARATOR + console.getStock() + SEPARATOR
                + CsvFile.clean(console.getBrand()) + SEPARATOR + CsvFile.clean(console.getModel())
                + SEPARATOR + console.getGeneration();
    }

    /**
     * Rebuilds a console from the columns of a line.
     *
     * @param fields every column of the line
     * @return the console
     */
    @Override
    public Product fromFields(String[] fields) {
        return new Console(fields[1], fields[2], Double.parseDouble(fields[3]), Integer.parseInt(fields[4]),
                fields[5], fields[6], Integer.parseInt(fields[7]));
    }
}
