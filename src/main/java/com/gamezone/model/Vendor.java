package com.gamezone.model;

/**
 * Represents an employee who attends customers and registers sales.
 */
public class Vendor extends Person {

    private String employeeCode;
    private String shift;

    /**
     * Creates a new Vendor.
     *
     * @param id           unique identification of the vendor
     * @param name         full name of the vendor
     * @param phone        contact phone number
     * @param employeeCode internal employee code
     * @param shift        assigned work shift
     */
    public Vendor(String id, String name, String phone, String employeeCode, String shift) {
        super(id, name, phone);
        this.employeeCode = employeeCode;
        this.shift = shift;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    @Override
    public String getDescription() {
        return "Vendor{id=" + getId() + ", name=" + getName()
                + ", phone=" + getPhone() + ", employeeCode=" + employeeCode
                + ", shift=" + shift + "}";
    }
}