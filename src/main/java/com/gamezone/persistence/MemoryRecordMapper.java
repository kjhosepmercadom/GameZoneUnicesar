package com.gamezone.persistence;

import com.gamezone.model.Accessory;
import com.gamezone.model.Memory;

/**
 * CSV mapper of memories (accessory type {@code MEMORY}).
 */
class MemoryRecordMapper extends AccessoryRecordMapper {

    private static final String SEPARATOR = ";";

    /**
     * Returns the discriminator of memories.
     *
     * @return {@code "MEMORY"}
     */
    @Override
    public String getTypeCode() {
        return "MEMORY";
    }

    /**
     * Builds the columns of a memory: the common accessory columns and its own ones.
     *
     * @param value the memory to store
     * @return the columns separated by semicolons
     */
    @Override
    public String toColumns(Accessory value) {
        Memory memory = (Memory) value;
        return commonColumns(memory) + SEPARATOR + memory.getCapacityInGb() + SEPARATOR + CsvFile.clean(memory.getMemoryType());
    }

    /**
     * Rebuilds a memory from the columns of a line.
     *
     * @param fields every column of the line
     * @return the memory
     */
    @Override
    public Accessory fromFields(String[] fields) {
        return new Memory(fields[1], fields[2], Double.parseDouble(fields[3]), Integer.parseInt(fields[4]),
                compatibleIds(fields), Integer.parseInt(fields[6]), fields[7]);
    }
}
