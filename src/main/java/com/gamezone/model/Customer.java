package com.gamezone.model;

/**
 * Customer that buys products at the store. Besides the basic information
 * of every person, a customer has an email address. The purchase history of a
 * customer is obtained from the registered sales.
 */
public class Customer extends Person {

    private String email;

    /**
     * Creates a customer with all its information.
     *
     * @param name           full name of the customer
     * @param identification identification document number
     * @param phone          contact phone number
     * @param email          email address
     */
    public Customer(String name, String identification, String phone, String email) {
        super(name, identification, phone);
        this.email = email;
    }

    /**
     * Returns the email address of the customer.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the customer.
     *
     * @param email the new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Builds the customer description including the email address.
     *
     * @return the full customer description
     */
    @Override
    public String getDescription() {
        return String.format("[Cliente] %s | Identificacion: %s | Telefono: %s | Correo: %s",
                getName(), getIdentification(), getPhone(), email);
    }
}
