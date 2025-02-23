package de.nrq.core.format;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaletteFormatTest {
    @Test
    void testFromFileName() {
        assertEquals(PaletteFormat.PAL, PaletteFormat.fromFileName("test.pal"));
        assertEquals(PaletteFormat.PAL, PaletteFormat.fromFileName("test.PAL"));
        assertEquals(PaletteFormat.VH, PaletteFormat.fromFileName("test.vh"));
        assertEquals(PaletteFormat.VH, PaletteFormat.fromFileName("test.VH"));
    }

    @Test
    void testFromFileNameInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            PaletteFormat.fromFileName("test.txt");
        });
    }

    @Test
    void testGetExtension() {
        assertEquals(".pal", PaletteFormat.PAL.getExtension());
        assertEquals(".vh", PaletteFormat.VH.getExtension());
    }

    @Test
    void testCaseInsensitivity() {
        assertEquals(PaletteFormat.PAL, PaletteFormat.fromFileName("TEST.PAL"));
        assertEquals(PaletteFormat.VH, PaletteFormat.fromFileName("TEST.VH"));
    }
}
