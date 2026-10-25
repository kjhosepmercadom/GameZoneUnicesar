package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Cable;
import com.gamezone.model.Controller;
import com.gamezone.model.Memory;
import com.gamezone.persistence.AccessoryRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Applies the business rules of the accessory module: registration of
 * controllers, cables and memories, inventory queries, compatibility queries
 * and stock updates. Every change is persisted through the {@link AccessoryRepository}.
 */
public class AccessoryService {

    private final AccessoryRepository accessoryRepository;
    private final List<Accessory> accessories;

    /**
     * Creates the service and loads the stored accessories.
     *
     * @param accessoryRepository repository used to persist the accessories
     */
    public AccessoryService(AccessoryRepository accessoryRepository) {
        this.accessoryRepository = accessoryRepository;
        this.accessories = accessoryRepository.loadAll();
    }

    /**
     * Registers a new controller.
     *
     * @param id                   unique identifier
     * @param title                commercial title
     * @param price                unit price
     * @param stock                initial stock
     * @param compatibleConsoleIds identifiers of the compatible consoles
     * @param connectionType       connection type (wireless or wired)
     * @return the registered controller
     * @throws IllegalArgumentException if any data is invalid or the identifier already exists
     */
    public Controller registerController(String id, String title, double price, int stock,
                                         List<String> compatibleConsoleIds, String connectionType) {
        Controller controller = new Controller(id.trim(), title.trim(), price, stock,
                cleanIds(compatibleConsoleIds), connectionType.trim());
        return save(controller);
    }

    /**
     * Registers a new cable.
     *
     * @param id                   unique identifier
     * @param title                commercial title
     * @param price                unit price
     * @param stock                initial stock
     * @param compatibleConsoleIds identifiers of the compatible consoles
     * @param lengthInMeters       cable length in meters
     * @param connectorType        connector type
     * @return the registered cable
     * @throws IllegalArgumentException if any data is invalid or the identifier already exists
     */
    public Cable registerCable(String id, String title, double price, int stock,
                               List<String> compatibleConsoleIds, double lengthInMeters, String connectorType) {
        Cable cable = new Cable(id.trim(), title.trim(), price, stock,
                cleanIds(compatibleConsoleIds), lengthInMeters, connectorType.trim());
        return save(cable);
    }

    /**
     * Registers a new memory.
     *
     * @param id                   unique identifier
     * @param title                commercial title
     * @param price                unit price
     * @param stock                initial stock
     * @param compatibleConsoleIds identifiers of the compatible consoles
     * @param capacityInGb         storage capacity in gigabytes
     * @param memoryType           memory type
     * @return the registered memory
     * @throws IllegalArgumentException if any data is invalid or the identifier already exists
     */
    public Memory registerMemory(String id, String title, double price, int stock,
                                 List<String> compatibleConsoleIds, int capacityInGb, String memoryType) {
        Memory memory = new Memory(id.trim(), title.trim(), price, stock,
                cleanIds(compatibleConsoleIds), capacityInGb, memoryType.trim());
        return save(memory);
    }

    private <T extends Accessory> T save(T accessory) {
        accessories.add(accessory);
        accessoryRepository.saveAll(accessories);
        return accessory;
    }

    private List<String> cleanIds(List<String> ids) {
        List<String> result = new ArrayList<>();
        if (ids != null) {
            for (String id : ids) {
                if (id != null && !id.trim().isEmpty()) {
                    result.add(id.trim().toUpperCase());
                }
            }
        }
        return result;
    }
}
