package com.gamezone.model;

/**
 * Represents a generic person interacting with the GameZone store.
 * This class cannot be instantiated directly; every person must be
 * either a Client or a Vendor.
 */
public abstract class Person {

    private String id;
    private String name;
    private String phone;

    /**
     * Creates a new Person with the common identity attributes.
     *
     * @param id    unique identification of the person
     * @param name  full name of the person
     * @param phone contact phone number
     */
    protected Person(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Builds a full description of the person, including the
     * attributes specific to its concrete role.
     *
     * @return a human-readable description of the person
     */
    public abstract String getDescription();
}