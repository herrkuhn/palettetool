package de.nrq.core.reader;

import java.io.IOException;

/**
 * Interface for reading color palettes from files.
 * Supports various formats such as PAL and VH, converting them
 * to a standardized array of integer RGB colors in PAL format order.
 */
public interface PaletteReader {
    /**
     * The number of expected colors in a palette.
     */
    int TOTAL_COLORS = 64;

    /**
     * Reads colors from a file and converts them to PAL format order.
     * Each color in the returned array is in 0xRRGGBB format.
     * The array will contain 64 colors arranged in 4 rows of 16 colors each.
     *
     * @param filename The file to read from
     * @return Array of integer colors in PAL format order
     * @throws IOException              If there's an error reading the file
     * @throws IllegalArgumentException If the file content is invalid
     */
    int[] readColors(String filename) throws IOException;
}
