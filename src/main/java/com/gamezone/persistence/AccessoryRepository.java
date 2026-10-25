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
