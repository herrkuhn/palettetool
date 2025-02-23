package de.nrq.image;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Builder for creating BMP images from color palettes.
 * Implements the BMP file format directly without AWT dependencies.
 */
public class BmpBuilder {
    private static final int COLORS_PER_ROW = 16;
    private static final int NUM_ROWS = 4;
    private static final int TOTAL_COLORS = COLORS_PER_ROW * NUM_ROWS;
    private static final int HEADER_SIZE = 54;  // BMP header size
    private static final int BITS_PER_PIXEL = 24;
    private static final int BYTES_PER_PIXEL = BITS_PER_PIXEL / 8;
    private int squareSize = 50;  // Default size

    /**
     * Sets the size of each color square in pixels.
     * The image will contain 16x4 squares of this size.
     *
     * @param size The size in pixels for each side of the square
     * @throws IllegalArgumentException if size is less than 1 pixel
     */
    public void setSquareSize(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Square size must be at least 1 pixel");
        }
        this.squareSize = size;
    }

    /**
     * Creates a Windows Bitmap (BMP) file from the given colors.
     * The image will contain 16x4 squares, each filled with one color.
     * The BMP is written in 24-bit color depth with no compression.
     *
     * @param colors     Array of colors in integer RGB format
     * @param outputFile The file path where the BMP will be saved
     * @throws IOException              If there's an error writing the file
     * @throws IllegalArgumentException if colors array is null or not exactly 64 colors
     */
    public void createBmpFile(int[] colors, String outputFile) throws IOException {
        createBmpFile(colors, new File(outputFile));
    }

    /**
     * Creates a BMP file from the given colors.
     *
     * @param colors     Array of colors in integer format
     * @param outputFile The file to write to
     * @throws IOException              if there's an error writing the file
     * @throws IllegalArgumentException if colors array is invalid
     */
    public void createBmpFile(int[] colors, File outputFile) throws IOException {
        validateColors(colors);

        int width = getImageWidth();
        int height = getImageHeight();
        int rowSize = ((width * BITS_PER_PIXEL + 31) / 32) * 4; // BMP rows are padded to multiples of 4 bytes
        int imageSize = rowSize * height;
        int fileSize = HEADER_SIZE + imageSize;

        try (FileOutputStream fos = new FileOutputStream(outputFile);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {

            // Write BMP header
            writeHeader(bos, fileSize, width, height, rowSize);

            // Write pixel data (bottom-up, padded rows)
            byte[] row = new byte[rowSize];
            for (int y = height - 1; y >= 0; y--) {
                fillRow(row, colors, y, width);
                bos.write(row);
            }
        }
    }

    /**
     * Writes the BMP file header and DIB header.
     * Creates a standard 24-bit BMP header with no compression.
     *
     * @param os       Output stream to write headers to
     * @param fileSize Total size of the BMP file in bytes
     * @param width    Width of the image in pixels
     * @param height   Height of the image in pixels
     * @param rowSize  Size of each row in bytes (padded to 4-byte boundary)
     * @throws IOException If writing to the output stream fails
     */
    private void writeHeader(OutputStream os, int fileSize, int width, int height, int rowSize) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        // Bitmap file header (14 bytes)
        buffer.put((byte) 'B');                // Signature
        buffer.put((byte) 'M');                // Signature
        buffer.putInt(fileSize);               // File size
        buffer.putInt(0);                // Reserved
        buffer.putInt(HEADER_SIZE);            // Offset to pixel data

        // DIB header (40 bytes)
        buffer.putInt(40);                     // Header size
        buffer.putInt(width);                        // Image width
        buffer.putInt(height);                       // Image height
        buffer.putShort((short) 1);                  // Number of color planes
        buffer.putShort((short) BITS_PER_PIXEL);     // Bits per pixel
        buffer.putInt(0);                      // Compression (none)
        buffer.putInt(rowSize * height);       // Image size
        buffer.putInt(2835);                   // Horizontal resolution (72 DPI)
        buffer.putInt(2835);                   // Vertical resolution (72 DPI)
        buffer.putInt(0);                      // Number of colors in palette
        buffer.putInt(0);                      // Number of important colors

        os.write(buffer.array());
    }

    /**
     * Fills a row of pixel data in the BMP format.
     * Handles the BGR color order required by BMP format and row padding.
     *
     * @param row    Byte array to fill with pixel data
     * @param colors Source array of colors in RGB format
     * @param y      Current row being processed
     * @param width  Width of the image in pixels
     */
    private void fillRow(byte[] row, int[] colors, int y, int width) {
        int squareY = y / squareSize;

        for (int x = 0; x < width; x++) {
            int squareX = x / squareSize;

            if (squareX < COLORS_PER_ROW && squareY < NUM_ROWS) {
                int colorIndex = squareY * COLORS_PER_ROW + squareX;
                int color = colors[colorIndex];

                // Write BGR (BMP uses BGR color order)
                int pos = x * BYTES_PER_PIXEL;
                row[pos] = (byte) (color & 0xFF);          // Blue
                row[pos + 1] = (byte) ((color >> 8) & 0xFF);  // Green
                row[pos + 2] = (byte) ((color >> 16) & 0xFF); // Red
            }
        }

        // Remaining bytes in row (if any) are already zero from array initialization
    }

    /**
     * Validates the input colors array for BMP creation.
     * Ensures the array contains exactly 64 colors and is not null.
     *
     * @param colors Array of colors to validate
     * @throws IllegalArgumentException if array is null or not exactly 64 colors
     */
    private void validateColors(int[] colors) {
        if (colors == null) {
            throw new IllegalArgumentException("Colors array cannot be null");
        }
        if (colors.length != TOTAL_COLORS) {
            throw new IllegalArgumentException(
                    String.format("Colors array must contain exactly %d colors", TOTAL_COLORS)
            );
        }
    }

    /**
     * Gets the current width of the image that would be created.
     * Width is calculated as COLORS_PER_ROW (16) × squareSize.
     *
     * @return The width in pixels
     */
    public int getImageWidth() {
        return COLORS_PER_ROW * squareSize;
    }

    /**
     * Gets the current height of the image that would be created.
     * Height is calculated as NUM_ROWS (4) × squareSize.
     *
     * @return The height in pixels
     */
    public int getImageHeight() {
        return NUM_ROWS * squareSize;
    }
}