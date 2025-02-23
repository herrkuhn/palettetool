package de.nrq.core.reader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class PaletteReadersTest {
    @TempDir
    Path tempDir;

    @Test
    void testPalReader() throws IOException {
        // Create a test .pal file with known colors
        byte[] palData = new byte[192]; // 64 colors * 3 bytes
        // Set first color to red
        palData[0] = (byte) 255;
        palData[1] = 0;
        palData[2] = 0;
        // Set second color to green
        palData[3] = 0;
        palData[4] = (byte) 255;
        palData[5] = 0;

        Path palFile = tempDir.resolve("test.pal");
        Files.write(palFile, palData);

        // Read using PalReader
        PaletteReader reader = new PalReader();
        int[] colors = reader.readColors(palFile.toString());

        // Verify colors
        assertEquals(64, colors.length);
        assertEquals(0xFF0000, colors[0]); // First color should be red
        assertEquals(0x00FF00, colors[1]); // Second color should be green
    }

    @Test
    void testVhReader() throws IOException {
        // Create a test VH file with known colors
        StringBuilder vhContent = new StringBuilder();
        vhContent.append("wire [23:0] lumacode_data_3s[0:63] = '{ ");
        // First 8 colors are black (from ends of rows)
        for (int i = 0; i < 8; i++) {
            vhContent.append("24'h000000");
            if (i < 7) vhContent.append(", ");
        }
        // Then red and green
        vhContent.append(", 24'hFF0000, 24'h00FF00");
        // Rest black
        for (int i = 0; i < 54; i++) {
            vhContent.append(", 24'h000000");
        }
        vhContent.append("};");

        Path vhFile = tempDir.resolve("test.vh");
        Files.writeString(vhFile, vhContent.toString());

        // Read using VhReader
        PaletteReader reader = new VhReader();
        int[] colors = reader.readColors(vhFile.toString());

        // Verify colors
        assertEquals(64, colors.length);
        assertEquals(0xFF0000, colors[0]); // First regular color should be red
        assertEquals(0x00FF00, colors[1]); // Second regular color should be green
        assertEquals(0x000000, colors[14]); // End of first row should be black
        assertEquals(0x000000, colors[15]); // End of first row should be black
    }

    @Test
    void testPalReaderInvalidFile() throws IOException {
        // Create a test file that's too small
        byte[] palData = new byte[50];
        Path palFile = tempDir.resolve("invalid.pal");
        Files.write(palFile, palData);

        PaletteReader reader = new PalReader();
        assertThrows(IllegalArgumentException.class, () -> {
            reader.readColors(palFile.toString());
        });
    }

    @Test
    void testVhReaderInvalidFile() throws IOException {
        // Create invalid VH file
        Path vhFile = tempDir.resolve("invalid.vh");
        Files.writeString(vhFile, "This is not a valid VH array");

        PaletteReader reader = new VhReader();
        assertThrows(IllegalArgumentException.class, () -> {
            reader.readColors(vhFile.toString());
        });
    }

    @Test
    void testPaletteReaderFactory() {
        assertInstanceOf(PalReader.class, PaletteReaderFactory.createReader("test.pal"));
        assertInstanceOf(VhReader.class, PaletteReaderFactory.createReader("test.vh"));
        assertThrows(IllegalArgumentException.class, () -> {
            PaletteReaderFactory.createReader("test.txt");
        });
    }
}
