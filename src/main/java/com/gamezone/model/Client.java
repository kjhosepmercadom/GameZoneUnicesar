package com.gamezone.model;

/**
 * Represents a customer who buys products at GameZone.
 */
public class Client extends Person {

    private String email;

    /**
     * Creates a new Client.
     *
     * @param id    unique identification of the client
     * @param name  full name of the client
     * @param phone contact phone number
     * @param email email address of the client
     */
    public Client(String id, String name, String phone, String email) {
        super(id, name, phone);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getDescription() {
        return "Client{id=" + getId() + ", name=" + getName()
                + ", phone=" + getPhone() + ", email=" + email + "}";
    }
}