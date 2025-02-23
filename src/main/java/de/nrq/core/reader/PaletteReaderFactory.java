package de.nrq.core.reader;

import de.nrq.core.format.PaletteFormat;

/**
 * Factory for creating PaletteReader instances based on file format.
 * Determines the appropriate reader based on file extension.
 */
public class PaletteReaderFactory {
    /**
     * Creates a PaletteReader for the specified format.
     * Returns a reader instance that can handle the given format.
     *
     * @param format The palette format to create a reader for
     * @return A PaletteReader instance appropriate for the format
     */
    public static PaletteReader createReader(PaletteFormat format) {
        return switch (format) {
            case PAL -> new PalReader();
            case VH -> new VhReader();
        };
    }

    /**
     * Creates a PaletteReader based on the filename extension.
     * Determines format from the file extension and returns appropriate reader.
     *
     * @param filename The filename to determine the format from
     * @return A PaletteReader instance appropriate for the file
     * @throws IllegalArgumentException if the file extension is not supported
     */
    public static PaletteReader createReader(String filename) {
        return createReader(PaletteFormat.fromFileName(filename));
    }
}
