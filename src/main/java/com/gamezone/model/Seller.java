package com.gamezone.model;

/**
 * Employee in charge of attending customers and registering sales. Besides
 * the basic information of every person, a seller has an employee code and an
 * assigned work shift.
 */
public class Seller extends Person {

    private String employeeCode;
    private String shift;

    /**
     * Creates a seller with all its information.
     *
     * @param name           full name of the seller
     * @param identification identification document number
     * @param phone          contact phone number
     * @param employeeCode   unique employee code
     * @param shift          assigned work shift
     */
    public Seller(String name, String identification, String phone, String employeeCode, String shift) {
        super(name, identification, phone);
        this.employeeCode = employeeCode;
        this.shift = shift;
    }

    /**
     * Returns the employee code of the seller.
     *
     * @return the employee code
     */
    public String getEmployeeCode() {
        return employeeCode;
    }

    /**
     * Sets the employee code of the seller.
     *
     * @param employeeCode the new employee code
     */
    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    /**
     * Returns the work shift assigned to the seller.
     *
     * @return the work shift
     */
    public String getShift() {
        return shift;
    }

    /**
     * Sets the work shift assigned to the seller.
     *
     * @param shift the new work shift
     */
    public void setShift(String shift) {
        this.shift = shift;
    }

    /**
     * Builds the seller description including employee code and shift.
     *
     * @return the full seller description
     */
    @Override
    public String getDescription() {
        return String.format("[Vendedor] %s - %s | Identificacion: %s | Telefono: %s | Turno: %s",
                employeeCode, getName(), getIdentification(), getPhone(), shift);
    }
}
