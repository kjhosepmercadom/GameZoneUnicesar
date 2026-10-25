package com.gamezone.persistence;

import com.gamezone.exceptions.PersistenceException;
import com.gamezone.model.Accessory;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores and retrieves the accessories of the store in a CSV file separated by
 * semicolons. The first column is a discriminator (CONTROLLER, CABLE or MEMORY)
 * and the sixth column keeps the compatible console identifiers separated by commas.
 */
public class AccessoryRepository {

    private static final String SEPARATOR = ";";

    private final CsvFile file;
    private final List<RecordMapper<Accessory>> mappers;

    /**
     * Creates a repository that works over the given file.
     *
     * @param filePath path of the CSV file that stores the accessories
     */
    public AccessoryRepository(String filePath) {
        this.file = new CsvFile(filePath);
        this.mappers = List.of(new ControllerRecordMapper(), new CableRecordMapper(), new MemoryRecordMapper());
    }

    /**
     * Writes every accessory to the file, replacing its previous content.
     *
     * @param accessories the accessories to store
     * @throws IllegalStateException if the file cannot be written
     */
    public void saveAll(List<Accessory> accessories) {
        List<String> lines = new ArrayList<>();
        for (Accessory accessory : accessories) {
            lines.add(toLine(accessory));
        }
        file.writeLines(lines, "No se pudo guardar el archivo de accesorios: ");
    }

    /**
     * Reads every accessory stored in the file, using the discriminator column
     * to rebuild the right concrete type.
     *
     * @return the stored accessories, or an empty list if the file does not exist
     * @throws IllegalStateException if the file cannot be read or has an invalid line
     */
    public List<Accessory> loadAll() {
        List<Accessory> accessories = new ArrayList<>();
        List<String> lines = file.readLines("No se pudo leer el archivo de accesorios: ");
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            try {
                accessories.add(fromLine(line));
            } catch (RuntimeException e) {
                throw new PersistenceException("Linea " + (i + 1) + " invalida en " + file.getPath() + ": " + line, e);
            }
        }
        return accessories;
    }

    private Accessory fromLine(String line) {
        String[] fields = line.split(SEPARATOR, -1);
        String type = fields[0];
        for (RecordMapper<Accessory> mapper : mappers) {
            if (mapper.getTypeCode().equals(type)) {
                return mapper.fromFields(fields);
            }
        }
        throw new IllegalArgumentException("Tipo de accesorio desconocido: " + type);
    }

    private String toLine(Accessory accessory) {
        for (RecordMapper<Accessory> mapper : mappers) {
            if (mapper.supports(accessory)) {
                return mapper.getTypeCode() + SEPARATOR + mapper.toColumns(accessory);
            }
        }
        throw new IllegalArgumentException("Tipo de accesorio no soportado: " + accessory.getClass().getSimpleName());
    }
}
