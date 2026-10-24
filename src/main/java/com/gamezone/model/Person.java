package com.gamezone.model;

/**
 * Abstract base class for every person that interacts with the store.
 * It holds the basic information shared by customers and sellers: full name,
 * identification number and contact phone.
 */
public abstract class Person {

    private String name;
    private String identification;
    private String phone;

    /**
     * Creates a person with its basic information.
     *
     * @param name           full name of the person
     * @param identification identification document number
     * @param phone          contact phone number
     */
    public Person(String name, String identification, String phone) {
        this.name = name;
        this.identification = identification;
        this.phone = phone;
    }

    /**
     * Returns the full name of the person.
     *
     * @return the full name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the full name of the person.
     *
     * @param name the new full name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the identification document number.
     *
     * @return the identification number
     */
    public String getIdentification() {
        return identification;
    }

    /**
     * Sets the identification document number.
     *
     * @param identification the new identification number
     */
    public void setIdentification(String identification) {
        this.identification = identification;
    }

    /**
     * Returns the contact phone number.
     *
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the contact phone number.
     *
     * @param phone the new phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Builds a description of the person that includes the information of
     * its specific role in the store. Every subclass must provide its own
     * implementation.
     *
     * @return the full person description
     */
    public abstract String getDescription();
}
