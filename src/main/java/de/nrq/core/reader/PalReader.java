package de.nrq.core.reader;

import de.nrq.core.color.ColorConverter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Implementation of PaletteReader for PAL format files.
 * Reads raw RGB color data where each color is represented by 3 consecutive bytes.
 */
public class PalReader implements PaletteReader {
    private static final int BYTES_PER_COLOR = 3;
    private static final int EXPECTED_FILE_SIZE = TOTAL_COLORS * BYTES_PER_COLOR;

    @Override
    public int[] readColors(String filename) throws IOException {
        byte[] palData = Files.readAllBytes(Paths.get(filename));

        if (palData.length < EXPECTED_FILE_SIZE) {
            throw new IllegalArgumentException(
                    String.format("Invalid .pal file size. Must be at least %d bytes.", EXPECTED_FILE_SIZE)
            );
        }

        // Convert byte data to integer colors
        return ColorConverter.rgbBytesToIntArray(palData);
    }
}
