package de.nrq.core.format;

/**
 * Represents supported palette file formats.
 * Supported formats:
 * - PAL: Raw RGB color data, 3 bytes per color
 * - VH: Verilog array format with 24'hRRGGBB color values
 */
public enum PaletteFormat {
    PAL,
    VH;

    /**
     * Determines the palette format from a filename extension.
     * Case-insensitive matching of .pal or .vh extensions.
     *
     * @param filename The filename to check
     * @return The detected PaletteFormat
     * @throws IllegalArgumentException if the format is not supported
     */
    public static PaletteFormat fromFileName(String filename) {
        String lowerFilename = filename.toLowerCase();
        if (lowerFilename.endsWith(".pal")) {
            return PAL;
        } else if (lowerFilename.endsWith(".vh")) {
            return VH;
        }
        throw new IllegalArgumentException("Unsupported file format. Use .pal or .vh files.");
    }

    /**
     * Gets the file extension for this format.
     * Returns the lowercase extension including the dot.
     *
     * @return The file extension (e.g., ".pal" or ".vh")
     */
    public String getExtension() {
        return "." + name().toLowerCase();
    }
}
