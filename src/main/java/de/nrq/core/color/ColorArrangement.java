package de.nrq.core.color;

/**
 * Utility class for handling color arrangement conversions between PAL and VH formats.
 */
public class ColorArrangement {
    private static final int TOTAL_COLORS = 64;
    private static final int COLORS_PER_ROW = 16;
    private static final int NUM_ROWS = 4;
    private static final int BLACK_COLORS = 8;

    private ColorArrangement() {
        // Utility class, prevent instantiation
    }

    /**
     * Converts colors from VH arrangement to PAL arrangement.
     * In VH format, the first 8 colors are black colors that appear
     * at the end of each row in PAL format. This method rearranges them
     * to their proper positions in PAL format.
     *
     * @param vhColors Array of colors in VH arrangement (must contain exactly 64 colors)
     * @return Array of colors in PAL arrangement (16 colors per row, 4 rows)
     * @throws IllegalArgumentException if input array is null or not exactly 64 colors
     */
    public static int[] vhToPalOrder(int[] vhColors) {
        validateInput(vhColors);

        int[] palColors = new int[TOTAL_COLORS];

        // Place main colors (everything except the black colors from the first 8 positions)
        for (int i = 0; i < TOTAL_COLORS - BLACK_COLORS; i++) {
            int vhIndex = i + BLACK_COLORS;  // Skip the 8 black colors at start
            int row = i / (COLORS_PER_ROW - 2);  // 14 colors per row (excluding 2 black at end)
            int col = i % (COLORS_PER_ROW - 2);  // Position within row
            int palIndex = row * COLORS_PER_ROW + col;  // Position in PAL format
            palColors[palIndex] = vhColors[vhIndex];
        }

        // Place black colors at the end of each row
        for (int row = 0; row < NUM_ROWS; row++) {
            int palIndex1 = row * COLORS_PER_ROW + (COLORS_PER_ROW - 2);  // Second to last in row
            int palIndex2 = row * COLORS_PER_ROW + (COLORS_PER_ROW - 1);  // Last in row
            int vhIndex1 = row * 2;        // Two black colors per row, from start of VH
            int vhIndex2 = row * 2 + 1;
            palColors[palIndex1] = vhColors[vhIndex1];
            palColors[palIndex2] = vhColors[vhIndex2];
        }

        return palColors;
    }

    /**
     * Converts colors from PAL arrangement to VH arrangement.
     * Collects all black colors from the end of each row in PAL format
     * and moves them to the start of the array for VH format.
     *
     * @param palColors Array of colors in PAL arrangement (must contain exactly 64 colors)
     * @return Array of colors in VH arrangement with black colors at the start
     * @throws IllegalArgumentException if input array is null or not exactly 64 colors
     */
    public static int[] palToVhOrder(int[] palColors) {
        validateInput(palColors);

        int[] vhColors = new int[TOTAL_COLORS];

        // First collect black colors from the end of each row
        for (int row = 0; row < NUM_ROWS; row++) {
            int palIndex1 = row * COLORS_PER_ROW + (COLORS_PER_ROW - 2);
            int palIndex2 = row * COLORS_PER_ROW + (COLORS_PER_ROW - 1);
            int vhIndex1 = row * 2;
            int vhIndex2 = row * 2 + 1;
            vhColors[vhIndex1] = palColors[palIndex1];
            vhColors[vhIndex2] = palColors[palIndex2];
        }

        // Then place the remaining colors
        int vhIndex = BLACK_COLORS;
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < COLORS_PER_ROW - 2; col++) {
                int palIndex = row * COLORS_PER_ROW + col;
                vhColors[vhIndex++] = palColors[palIndex];
            }
        }

        return vhColors;
    }

    /**
     * Validates the input color array for conversion operations.
     * Checks that the array is not null and contains exactly 64 colors.
     *
     * @param colors Array of colors to validate
     * @throws IllegalArgumentException if array is null or not exactly 64 colors
     */
    private static void validateInput(int[] colors) {
        if (colors == null) {
            throw new IllegalArgumentException("Colors array cannot be null");
        }
        if (colors.length != TOTAL_COLORS) {
            throw new IllegalArgumentException(
                    String.format("Colors array must contain exactly %d colors", TOTAL_COLORS)
            );
        }
    }
}
