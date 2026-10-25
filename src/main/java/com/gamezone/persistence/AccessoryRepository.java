package com.gamezone.persistence;

import com.gamezone.model.Accessory;
import com.gamezone.model.Cable;
import com.gamezone.model.Controller;
import com.gamezone.model.Memory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Stores and retrieves the accessories of the store in a CSV file separated by
 * semicolons. The first column is a discriminator (CONTROLLER, CABLE or MEMORY)
 * and the sixth column keeps the compatible console identifiers separated by commas.
 */
public class AccessoryRepository {

    private static final String SEPARATOR = ";";
    private static final String LIST_SEPARATOR = ",";

    private final Path filePath;

    /**
     * Creates a repository that works over the given file.
     *
     * @param filePath path of the CSV file that stores the accessories
     */
    public AccessoryRepository(String filePath) {
        this.filePath = Paths.get(filePath);
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
        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo guardar el archivo de accesorios: " + filePath, e);
        }
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
        if (!Files.exists(filePath)) {
            return accessories;
        }
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer el archivo de accesorios: " + filePath, e);
        }
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            try {
                accessories.add(fromLine(line));
            } catch (RuntimeException e) {
                throw new IllegalStateException("Linea " + (i + 1) + " invalida en " + filePath + ": " + line, e);
            }
        }
        return accessories;
    }

    private Accessory fromLine(String line) {
        String[] fields = line.split(SEPARATOR, -1);
        String type = fields[0];
        String id = fields[1];
        String title = fields[2];
        double price = Double.parseDouble(fields[3]);
        int stock = Integer.parseInt(fields[4]);
        List<String> compatible = fields[5].isEmpty()
                ? new ArrayList<>() : new ArrayList<>(Arrays.asList(fields[5].split(LIST_SEPARATOR)));
        switch (type) {
            case "CONTROLLER":
                return new Controller(id, title, price, stock, compatible, fields[6]);
            case "CABLE":
                return new Cable(id, title, price, stock, compatible, Double.parseDouble(fields[6]), fields[7]);
            case "MEMORY":
                return new Memory(id, title, price, stock, compatible, Integer.parseInt(fields[6]), fields[7]);
            default:
                throw new IllegalArgumentException("Tipo de accesorio desconocido: " + type);
        }
    }

    private String toLine(Accessory accessory) {
        String common = accessory.getAccessoryType() + SEPARATOR + clean(accessory.getId()) + SEPARATOR
                + clean(accessory.getTitle()) + SEPARATOR + accessory.getPrice() + SEPARATOR
                + accessory.getStock() + SEPARATOR
                + String.join(LIST_SEPARATOR, accessory.getCompatibleConsoleIds());
        if (accessory instanceof Controller) {
            return common + SEPARATOR + clean(((Controller) accessory).getConnectionType());
        }
        if (accessory instanceof Cable) {
            Cable cable = (Cable) accessory;
            return common + SEPARATOR + cable.getLengthInMeters() + SEPARATOR + clean(cable.getConnectorType());
        }
        if (accessory instanceof Memory) {
            Memory memory = (Memory) accessory;
            return common + SEPARATOR + memory.getCapacityInGb() + SEPARATOR + clean(memory.getMemoryType());
        }
        throw new IllegalArgumentException("Tipo de accesorio no soportado: " + accessory.getClass().getSimpleName());
    }

    private String clean(String value) {
        return value == null ? "" : value.replace(SEPARATOR, ",").replace("\n", " ").replace("\r", " ");
    }
}
