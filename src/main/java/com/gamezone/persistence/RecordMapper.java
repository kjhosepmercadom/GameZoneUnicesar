package com.gamezone.persistence;

/**
 * Converts the objects of one concrete subtype to a CSV line and back. A
 * repository keeps one mapper per subtype, so a new subtype is supported by
 * adding a new mapper instead of modifying the repository.
 *
 * @param <T> base type stored by the repository
 */
interface RecordMapper<T> {

    /**
     * Returns the discriminator written in the first column of the line.
     *
     * @return the type code of the subtype
     */
    String getTypeCode();

    /**
     * Tells whether this mapper converts the given object.
     *
     * @param value the object to store
     * @return {@code true} if the object is of the subtype of this mapper
     */
    boolean supports(T value);

    /**
     * Builds the columns of the line that follow the type code.
     *
     * @param value the object to store, of the subtype of this mapper
     * @return the columns separated by semicolons
     */
    String toColumns(T value);

    /**
     * Rebuilds an object from the columns of a line.
     *
     * @param fields every column of the line, including the type code at position 0
     * @return the rebuilt object
     */
    T fromFields(String[] fields);
}
