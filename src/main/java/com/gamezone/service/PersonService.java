package com.gamezone.service;

import com.gamezone.exceptions.BusinessRuleException;
import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.model.Customer;
import com.gamezone.model.Seller;
import com.gamezone.persistence.PersonRepository;
import com.gamezone.validation.PersonValidator;

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
     * @throws InvalidDataException  if any data is invalid
     * @throws BusinessRuleException if the identification already exists
     */
    public Customer registerCustomer(String name, String identification, String phone, String email) {
        PersonValidator.validatePersonData(identification, name, email, phone);
        PersonValidator.validateUniquePerson(customers, identification);
        Customer customer = new Customer(name.trim(), identification.trim(), phone.trim(), email.trim());
        customers.add(customer);
        personRepository.saveCustomers(customers);
        return customer;
    }

    /**
     * Returns every registered customer.
     *
     * @return a copy of the customer list
     */
    public List<Customer> listCustomers() {
        return new ArrayList<>(customers);
    }

    /**
     * Returns every seller of the store.
     *
     * @return a copy of the seller list
     */
    public List<Seller> listSellers() {
        return new ArrayList<>(sellers);
    }

    /**
     * Finds a customer by its identification number.
     *
     * @param identification the identification number
     * @return the customer found, or {@code null} if it does not exist
     */
    public Customer findCustomerById(String identification) {
        if (identification == null) {
            return null;
        }
        for (Customer customer : customers) {
            if (customer.getIdentification().equals(identification.trim())) {
                return customer;
            }
        }
        return null;
    }

    /**
     * Finds a seller by its employee code, ignoring letter case.
     *
     * @param employeeCode the employee code
     * @return the seller found, or {@code null} if it does not exist
     */
    public Seller findSellerByEmployeeCode(String employeeCode) {
        if (employeeCode == null) {
            return null;
        }
        for (Seller seller : sellers) {
            if (seller.getEmployeeCode().equalsIgnoreCase(employeeCode.trim())) {
                return seller;
            }
        }
        return null;
    }

}
