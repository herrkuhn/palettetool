package de.nrq.core.color;

/**
 * Handles conversion between different color formats.
 */
public class ColorConverter {
    /**
     * Converts separate RGB components to a single integer color value.
     * The resulting integer contains the RGB values in the format 0xRRGGBB.
     *
     * @param r Red component (0-255, will be masked to 8 bits)
     * @param g Green component (0-255, will be masked to 8 bits)
     * @param b Blue component (0-255, will be masked to 8 bits)
     * @return Integer representation of the color in 0xRRGGBB format
     */
    public static int rgbToInt(byte r, byte g, byte b) {
        return ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    /**
     * Converts a single integer color to RGB byte components.
     * Extracts RGB values from a color in 0xRRGGBB format.
     *
     * @param color Integer color value in 0xRRGGBB format
     * @return Array of 3 bytes containing RGB components in order [R,G,B]
     */
    public static byte[] intToRgb(int color) {
        return new byte[]{
                (byte) ((color >> 16) & 0xFF),  // R
                (byte) ((color >> 8) & 0xFF),   // G
                (byte) (color & 0xFF)           // B
        };
    }

    /**
     * Converts an array of RGB bytes to an array of integer colors.
     * Each three consecutive bytes in the input represent one RGB color.
     *
     * @param data Array of RGB bytes where each color takes 3 bytes [R,G,B,R,G,B,...]
     * @return Array of integer colors in 0xRRGGBB format
     * @throws IllegalArgumentException if data length is not a multiple of 3
     */
    public static int[] rgbBytesToIntArray(byte[] data) {
        if (data.length % 3 != 0) {
            throw new IllegalArgumentException("Data length must be multiple of 3");
        }

        int[] colors = new int[data.length / 3];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = rgbToInt(
                    data[i * 3],
                    data[i * 3 + 1],
                    data[i * 3 + 2]
            );
        }
        return colors;
    }

    /**
     * Converts an array of integer colors to RGB bytes.
     * Each color is split into its RGB components.
     *
     * @param colors Array of integer colors in 0xRRGGBB format
     * @return Array of bytes where each color is represented by 3 bytes [R,G,B,R,G,B,...]
     */
    public static byte[] intArrayToRgbBytes(int[] colors) {
        byte[] data = new byte[colors.length * 3];
        for (int i = 0; i < colors.length; i++) {
            byte[] rgb = intToRgb(colors[i]);
            data[i * 3] = rgb[0];
            data[i * 3 + 1] = rgb[1];
            data[i * 3 + 2] = rgb[2];
        }
        return data;
    }
}
