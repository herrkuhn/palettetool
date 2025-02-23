package de.nrq.image;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class BmpBuilderTest {
    @TempDir
    Path tempDir;
    private BmpBuilder builder;
    private int[] testColors;

    @BeforeEach
    void setUp() {
        builder = new BmpBuilder();

        // Create test colors array (64 colors)
        testColors = new int[64];
        // Set first color to red
        testColors[0] = 0xFF0000;
        // Set second color to green
        testColors[1] = 0x00FF00;
        // Rest black
        for (int i = 2; i < 64; i++) {
            testColors[i] = 0x000000;
        }
    }

    @Test
    void testSetSquareSize() {
        builder.setSquareSize(10);
        assertEquals(160, builder.getImageWidth());  // 16 * 10
        assertEquals(40, builder.getImageHeight());  // 4 * 10
    }

    @Test
    void testInvalidSquareSize() {
        assertThrows(IllegalArgumentException.class, () -> {
            builder.setSquareSize(0);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            builder.setSquareSize(-1);
        });
    }

    @Test
    void testNullColors() {
        assertThrows(IllegalArgumentException.class, () -> {
            builder.createBmpFile(null, "test.bmp");
        });
    }

    @Test
    void testInvalidColorArraySize() {
        assertThrows(IllegalArgumentException.class, () -> {
            builder.createBmpFile(new int[10], "test.bmp");
        });
    }

    @Test
    void testCreateBmpFile() throws IOException {
        File outputFile = tempDir.resolve("test.bmp").toFile();
        builder.createBmpFile(testColors, outputFile);

        // Verify file exists and has correct format
        assertTrue(outputFile.exists());
        assertTrue(outputFile.length() > 0);

        try (RandomAccessFile raf = new RandomAccessFile(outputFile, "r")) {
            // Check BMP signature
            assertEquals('B', raf.read());
            assertEquals('M', raf.read());

            // Skip to width/height in DIB header (starting at offset 18)
            raf.seek(18);

            // Read width and height (4 bytes each, little-endian)
            int width = Integer.reverseBytes(raf.readInt());
            int height = Integer.reverseBytes(raf.readInt());

            // Verify dimensions
            assertEquals(800, width);   // 16 * 50
            assertEquals(200, height);  // 4 * 50
        }
    }

    @Test
    void testGetDimensions() {
        builder.setSquareSize(30);
        assertEquals(480, builder.getImageWidth());  // 16 * 30
        assertEquals(120, builder.getImageHeight()); // 4 * 30
    }
}