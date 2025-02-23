package de.nrq.core.color;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorConverterTest {
    @Test
    void testRgbToInt() {
        // Test pure red
        assertEquals(0xFF0000, ColorConverter.rgbToInt((byte) 255, (byte) 0, (byte) 0));
        // Test pure green
        assertEquals(0x00FF00, ColorConverter.rgbToInt((byte) 0, (byte) 255, (byte) 0));
        // Test pure blue
        assertEquals(0x0000FF, ColorConverter.rgbToInt((byte) 0, (byte) 0, (byte) 255));
        // Test black
        assertEquals(0x000000, ColorConverter.rgbToInt((byte) 0, (byte) 0, (byte) 0));
        // Test white
        assertEquals(0xFFFFFF, ColorConverter.rgbToInt((byte) 255, (byte) 255, (byte) 255));
    }

    @Test
    void testIntToRgb() {
        // Test pure red
        assertArrayEquals(
                new byte[]{(byte) 255, (byte) 0, (byte) 0},
                ColorConverter.intToRgb(0xFF0000)
        );
        // Test pure green
        assertArrayEquals(
                new byte[]{(byte) 0, (byte) 255, (byte) 0},
                ColorConverter.intToRgb(0x00FF00)
        );
        // Test pure blue
        assertArrayEquals(
                new byte[]{(byte) 0, (byte) 0, (byte) 255},
                ColorConverter.intToRgb(0x0000FF)
        );
    }

    @Test
    void testRgbBytesToIntArray() {
        byte[] input = {
                (byte) 255, (byte) 0, (byte) 0,    // Red
                (byte) 0, (byte) 255, (byte) 0     // Green
        };
        int[] expected = {0xFF0000, 0x00FF00};
        assertArrayEquals(expected, ColorConverter.rgbBytesToIntArray(input));
    }

    @Test
    void testIntArrayToRgbBytes() {
        int[] input = {0xFF0000, 0x00FF00}; // Red, Green
        byte[] expected = {
                (byte) 255, (byte) 0, (byte) 0,    // Red
                (byte) 0, (byte) 255, (byte) 0     // Green
        };
        assertArrayEquals(expected, ColorConverter.intArrayToRgbBytes(input));
    }

    @Test
    void testRgbBytesToIntArrayInvalidLength() {
        byte[] input = {(byte) 255, (byte) 0}; // Invalid length (not multiple of 3)
        assertThrows(IllegalArgumentException.class, () -> {
            ColorConverter.rgbBytesToIntArray(input);
        });
    }

    @Test
    void testRoundTripConversion() {
        // Original colors
        int[] originalColors = {0xFF0000, 0x00FF00, 0x0000FF};

        // Convert to bytes and back
        byte[] bytes = ColorConverter.intArrayToRgbBytes(originalColors);
        int[] convertedColors = ColorConverter.rgbBytesToIntArray(bytes);

        // Should be identical
        assertArrayEquals(originalColors, convertedColors);
    }
}
