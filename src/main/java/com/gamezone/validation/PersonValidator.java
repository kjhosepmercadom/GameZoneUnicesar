package com.gamezone.validation;

import com.gamezone.exceptions.BusinessRuleException;
import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.exceptions.ResourceNotFoundException;
import com.gamezone.model.Person;

import java.util.List;

/**
 * Validations of the person module. They are invoked by the services before
 * executing a business operation and throw the custom exceptions when a rule
 * is not met. Validators never access the persistence layer.
 */
public final class PersonValidator {

    private PersonValidator() {
    }

    /**
     * Validates the data of a person: required fields and a reasonable email
     * format (an at sign followed by a dot).
     *
     * @param id    identification number, required
     * @param name  full name, required
     * @param email email address, required and well formed
     * @param phone contact phone, required
     * @throws InvalidDataException if any value is invalid
     */
    public static void validatePersonData(String id, String name, String email, String phone) {
        requireText(name, "nombre");
        requireText(id, "identificacion");
        requireText(phone, "telefono");
        requireText(email, "correo");
        if (!isValidEmail(email.trim())) {
            throw new InvalidDataException("correo", "no tiene un formato valido: " + email.trim() + ".");
        }
    }

    /**
     * Validates that a person was found.
     *
     * @param person     the person found, possibly {@code null}
     * @param personType name of the person type shown to the user, for example "cliente"
     * @param personId   the identifier that was searched
     * @throws ResourceNotFoundException if the person is {@code null}
     */
    public static void validatePersonExists(Person person, String personType, String personId) {
        if (person == null) {
            throw new ResourceNotFoundException(personType, personId);
        }
    }

    /**
     * Validates that no registered person has the same identification.
     *
     * @param existing the registered people
     * @param newId    identification of the new person
     * @throws BusinessRuleException if the identification is already registered
     */
    public static void validateUniquePerson(List<? extends Person> existing, String newId) {
        for (Person person : existing) {
            if (person.getIdentification().equals(newId.trim())) {
                throw new BusinessRuleException("Ya existe un cliente con identificacion " + newId.trim() + ".");
            }
        }
    }

    private static boolean isValidEmail(String email) {
        int at = email.indexOf('@');
        return at > 0 && at == email.lastIndexOf('@') && email.indexOf('.', at) > at + 1
                && !email.endsWith(".") && !email.contains(" ");
    }

    private static void requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidDataException(fieldName, "es obligatorio.");
        }
    }
}
