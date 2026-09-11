package com.gamezone.persistence;

import com.gamezone.model.Client;
import com.gamezone.model.Vendor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles file-based persistence for clients and vendors.
 */
public class PersonRepository {

    private static final String CLIENTS_FILE = "data/clients.txt";
    private static final String VENDORS_FILE = "data/vendors.txt";

    /**
     * Loads all clients stored in the clients file.
     *
     * @return the list of clients, or an empty list if the file does not exist
     */
    public List<Client> loadClients() {
        List<Client> clients = new ArrayList<>();
        File file = new File(CLIENTS_FILE);
        if (!file.exists()) {
            return clients;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                clients.add(new Client(parts[0], parts[1], parts[2], parts[3]));
            }
        } catch (IOException e) {
            System.out.println("Error loading clients: " + e.getMessage());
        }
        return clients;
    }

    /**
     * Saves the given list of clients, overwriting the clients file.
     *
     * @param clients the list of clients to persist
     */
    public void saveClients(List<Client> clients) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CLIENTS_FILE))) {
            for (Client c : clients) {
                writer.write(c.getId() + ";" + c.getName() + ";" + c.getPhone() + ";" + c.getEmail());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving clients: " + e.getMessage());
        }
    }

    /**
     * Loads all vendors stored in the vendors file.
     *
     * @return the list of vendors, or an empty list if the file does not exist
     */
    public List<Vendor> loadVendors() {
        List<Vendor> vendors = new ArrayList<>();
        File file = new File(VENDORS_FILE);
        if (!file.exists()) {
            return vendors;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                vendors.add(new Vendor(parts[0], parts[1], parts[2], parts[3], parts[4]));
            }
        } catch (IOException e) {
            System.out.println("Error loading vendors: " + e.getMessage());
        }
        return vendors;
    }

    /**
     * Saves the given list of vendors, overwriting the vendors file.
     *
     * @param vendors the list of vendors to persist
     */
    public void saveVendors(List<Vendor> vendors) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(VENDORS_FILE))) {
            for (Vendor v : vendors) {
                writer.write(v.getId() + ";" + v.getName() + ";" + v.getPhone() + ";"
                        + v.getEmployeeCode() + ";" + v.getShift());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving vendors: " + e.getMessage());
        }
    }
}