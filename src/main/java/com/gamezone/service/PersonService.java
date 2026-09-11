package com.gamezone.service;

import com.gamezone.model.Client;
import com.gamezone.model.Vendor;
import com.gamezone.persistence.PersonRepository;

import java.util.List;

/**
 * Provides business logic for managing clients and vendors.
 */
public class PersonService {

    private final PersonRepository repository;
    private List<Client> clients;
    private List<Vendor> vendors;

    /**
     * Creates the service and loads existing persons from storage.
     *
     * @param repository the repository used for persistence
     */
    public PersonService(PersonRepository repository) {
        this.repository = repository;
        this.clients = repository.loadClients();
        this.vendors = repository.loadVendors();
    }

    /**
     * Registers a new client and persists the updated list.
     *
     * @param client the client to register
     */
    public void registerClient(Client client) {
        clients.add(client);
        repository.saveClients(clients);
    }

    /**
     * Returns all registered clients.
     *
     * @return the list of clients
     */
    public List<Client> listClients() {
        return clients;
    }

    /**
     * Returns all registered vendors.
     *
     * @return the list of vendors
     */
    public List<Vendor> listVendors() {
        return vendors;
    }

    /**
     * Finds a client by id.
     *
     * @param id the client id
     * @return the matching client, or null if not found
     */
    public Client findClientById(String id) {
        for (Client c : clients) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Finds a vendor by id.
     *
     * @param id the vendor id
     * @return the matching vendor, or null if not found
     */
    public Vendor findVendorById(String id) {
        for (Vendor v : vendors) {
            if (v.getId().equals(id)) {
                return v;
            }
        }
        return null;
    }
}