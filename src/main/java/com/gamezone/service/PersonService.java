package com.gamezone.service;

import com.gamezone.model.Customer;
import com.gamezone.model.Seller;
import com.gamezone.persistence.PersonRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Applies the business rules of the person module: registration of customers
 * and queries over customers and sellers. Sellers are not registered through
 * the user interface; they are loaded from the preloaded sellers file.
 */
public class PersonService {

    private final PersonRepository personRepository;
    private final List<Customer> customers;
    private final List<Seller> sellers;

    /**
     * Creates the service and loads the stored customers and sellers.
     *
     * @param personRepository repository used to persist the people
     */
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
        this.customers = personRepository.loadCustomers();
        this.sellers = personRepository.loadSellers();
    }

    /**
     * Registers a new customer.
     *
     * @param name           full name of the customer
     * @param identification identification document number
     * @param phone          contact phone number
     * @param email          email address
     * @return the registered customer
     * @throws IllegalArgumentException if any data is invalid or the identification already exists
     */
    public Customer registerCustomer(String name, String identification, String phone, String email) {
        Customer customer = new Customer(name.trim(), identification.trim(), phone.trim(), email.trim());
        customers.add(customer);
        personRepository.saveCustomers(customers);
        return customer;
    }
}
