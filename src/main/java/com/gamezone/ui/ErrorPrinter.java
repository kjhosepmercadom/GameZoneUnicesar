package com.gamezone.ui;

import com.gamezone.exceptions.BusinessRuleException;
import com.gamezone.exceptions.InvalidDataException;
import com.gamezone.exceptions.PersistenceException;
import com.gamezone.exceptions.ResourceNotFoundException;

/**
 * Shows to the user a message in Spanish that depends on the type of error,
 * so every menu reports the errors in the same way and the user never sees a
 * technical trace.
 */
public class ErrorPrinter {

    /**
     * Prints the message that corresponds to the type of the error.
     *
     * @param error the error raised by an operation
     */
    public void print(RuntimeException error) {
        try {
            throw error;
        } catch (ResourceNotFoundException e) {
            System.out.println("No se encontro el recurso solicitado: " + e.getMessage());
        } catch (BusinessRuleException e) {
            System.out.println("No se puede completar la operacion: " + e.getMessage());
        } catch (InvalidDataException e) {
            System.out.println("Los datos ingresados no son validos: " + e.getMessage());
        } catch (PersistenceException e) {
            System.out.println("Ocurrio un problema al acceder a los datos del sistema. Contacte al administrador.");
        } catch (IllegalArgumentException e) {
            System.out.println("No se puede completar la operacion: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Ocurrio un error inesperado. Intente de nuevo.");
        }
    }
}
