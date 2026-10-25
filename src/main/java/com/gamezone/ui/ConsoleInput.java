package com.gamezone.ui;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Reads and validates the values typed by the user in the console. It is a
 * pure fabrication shared by every menu of the user interface, so the input
 * rules (required text, numbers, dates) are written only once.
 */
public class ConsoleInput {

    private final Scanner scanner;

    /**
     * Creates the reader over the given scanner.
     *
     * @param scanner source of the user input
     */
    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Shows a prompt and reads a line, without the surrounding spaces.
     *
     * @param prompt text shown before reading
     * @return the line read, possibly empty
     * @throws IllegalStateException if there is no more input
     */
    public String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            throw new IllegalStateException("No hay mas datos de entrada.");
        }
        return scanner.nextLine().trim();
    }

    /**
     * Reads a line until the user types a non empty value.
     *
     * @param prompt text shown before reading
     * @return the non empty value
     */
    public String readText(String prompt) {
        while (true) {
            String value = readLine(prompt);
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Este campo es obligatorio.");
        }
    }

    /**
     * Reads a line until the user types an integer.
     *
     * @param prompt text shown before reading
     * @return the integer typed
     */
    public int readInt(String prompt) {
        while (true) {
            String value = readLine(prompt);
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero entero.");
            }
        }
    }

    /**
     * Reads a line until the user types a number. Accepts the Colombian
     * thousands separator (150.000) and the decimal comma.
     *
     * @param prompt text shown before reading
     * @return the number typed
     */
    public double readDouble(String prompt) {
        while (true) {
            String value = readLine(prompt).replace("$", "").replace(" ", "");
            if (value.matches("\\d{1,3}(\\.\\d{3})+")) {
                value = value.replace(".", "");
            }
            value = value.replace(",", ".");
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero valido.");
            }
        }
    }

    /**
     * Reads a line until the user types a date with the format AAAA-MM-DD.
     *
     * @param prompt text shown before reading
     * @return the date typed
     */
    public LocalDate readDate(String prompt) {
        while (true) {
            String value = readLine(prompt);
            try {
                return LocalDate.parse(value);
            } catch (DateTimeParseException e) {
                System.out.println("Debe ingresar una fecha valida con el formato AAAA-MM-DD.");
            }
        }
    }
}
