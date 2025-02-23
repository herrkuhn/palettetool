package de.nrq.core.color;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorArrangementTest {

    @Test
    void testVhToPalOrder() {
        // Create VH-ordered colors
        int[] vhColors = new int[64];
        // First 8 are black
        for (int i = 0; i < 8; i++) {
            vhColors[i] = 0x000000;
        }
        // Then some test colors
        vhColors[8] = 0xFF0000;   // Red
        vhColors[9] = 0x00FF00;   // Green
        // Rest black
        for (int i = 10; i < 64; i++) {
            vhColors[i] = 0x000000;
        }

        int[] palColors = ColorArrangement.vhToPalOrder(vhColors);

        // Verify first row's content
        assertEquals(0xFF0000, palColors[0]);     // Red at start
        assertEquals(0x00FF00, palColors[1]);     // Green second
        assertEquals(0x000000, palColors[14]);    // Black at end
        assertEquals(0x000000, palColors[15]);    // Black at end
    }

    @Test
    void testPalToVhOrder() {
        // Create PAL-ordered colors
        int[] palColors = new int[64];
        palColors[0] = 0xFF0000;   // Red
        palColors[1] = 0x00FF00;   // Green
        // Set blacks at the end of each row
        palColors[14] = 0x000000;
        palColors[15] = 0x000000;
        palColors[30] = 0x000000;
        palColors[31] = 0x000000;
        palColors[46] = 0x000000;
        palColors[47] = 0x000000;
        palColors[62] = 0x000000;
        palColors[63] = 0x000000;

        int[] vhColors = ColorArrangement.palToVhOrder(palColors);

        // Verify black colors are at the start
        for (int i = 0; i < 8; i++) {
            assertEquals(0x000000, vhColors[i], "Black color at position " + i);
        }
        // Verify regular colors are after blacks
        assertEquals(0xFF0000, vhColors[8]);    // Red after blacks
        assertEquals(0x00FF00, vhColors[9]);    // Green after red
    }

    @Test
    void testRoundTripConversion() {
        // Create original PAL-ordered colors
        int[] original = new int[64];
        for (int i = 0; i < 64; i++) {
            original[i] = 0xFF0000 + i;  // Unique color for each position
        }

        // Convert PAL -> VH -> PAL
        int[] vhColors = ColorArrangement.palToVhOrder(original);
        int[] roundTrip = ColorArrangement.vhToPalOrder(vhColors);

        // Should match original
        assertArrayEquals(original, roundTrip, "Round-trip conversion should preserve colors");
    }

    @Test
    void testInvalidInput() {
        // Test null input
        assertThrows(IllegalArgumentException.class, () -> {
            ColorArrangement.vhToPalOrder(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            ColorArrangement.palToVhOrder(null);
        });

        // Test wrong array size
        assertThrows(IllegalArgumentException.class, () -> {
            ColorArrangement.vhToPalOrder(new int[10]);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            ColorArrangement.palToVhOrder(new int[10]);
        });
    }
}
