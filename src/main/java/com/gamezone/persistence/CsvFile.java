package com.gamezone.persistence;

import com.gamezone.exceptions.PersistenceException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads and writes one CSV data file of the application. It is a pure
 * fabrication shared by every repository, so the file handling (encoding,
 * missing file, folder creation, error wrapping) is written only once.
 */
final class CsvFile {

    private static final String SEPARATOR = ";";

    private final Path path;

    /**
     * Creates the helper for a data file.
     *
     * @param path path of the CSV file
     */
    CsvFile(String path) {
        this.path = Paths.get(path);
    }

    /**
     * Returns the path of the file, used in the error messages.
     *
     * @return the file path
     */
    Path getPath() {
        return path;
    }

    /**
     * Reads every line of the file.
     *
     * @param errorMessage beginning of the message used if the file cannot be read
     * @return the lines of the file, or an empty list if the file does not exist
     * @throws PersistenceException if the file exists but cannot be read
     */
    List<String> readLines(String errorMessage) {
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }
        try {
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new PersistenceException(errorMessage + path, e);
        }
    }

    /**
     * Replaces the content of the file with the given lines, creating the
     * folder of the file when it does not exist.
     *
     * @param lines        the lines to write
     * @param errorMessage beginning of the message used if the file cannot be written
     * @throws PersistenceException if the file cannot be written
     */
    void writeLines(List<String> lines, String errorMessage) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new PersistenceException(errorMessage + path, e);
        }
    }

    /**
     * Makes a text value safe to be stored in one CSV column: the separator
     * and the line breaks are replaced.
     *
     * @param value the value to store
     * @return the cleaned value, empty if the value is {@code null}
     */
    static String clean(String value) {
        return value == null ? "" : value.replace(SEPARATOR, ",").replace("\n", " ").replace("\r", " ");
    }
}
