package de.nrq.cli;

import de.nrq.core.color.ColorArrangement;
import de.nrq.core.color.ColorConverter;
import de.nrq.core.reader.PaletteReader;
import de.nrq.core.reader.PaletteReaderFactory;
import de.nrq.image.BmpBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Main tool for converting between different palette formats and creating visualizations.
 */
public class PaletteTool {
    private static final BmpBuilder bmpBuilder = new BmpBuilder();

    public static void main(String[] args) {
        if (args.length < 2) {
            printUsage();
            System.exit(1);
        }

        try {
            String command = args[0].toLowerCase();
            String inputFile = args[1];
            String outputFile = args.length > 2 ? args[2] : null;

            switch (command) {
                case "tobmp" -> {
                    if (outputFile == null) {
                        outputFile = inputFile.replaceFirst("(?i)\\.(pal|vh)$", ".bmp");
                    }
                    convertToBmp(inputFile, outputFile);
                }
                case "topal" -> {
                    if (outputFile == null) {
                        outputFile = inputFile.replaceFirst("(?i)\\.vh$", ".pal");
                    }
                    convertToPal(inputFile, outputFile);
                }
                case "tovh" -> {
                    if (outputFile == null) {
                        outputFile = inputFile.replaceFirst("(?i)\\.pal$", ".vh");
                    }
                    convertToVh(inputFile, outputFile);
                }
                default -> {
                    System.err.println("Error: Unknown command: " + command);
                    printUsage();
                    System.exit(1);
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Converts an input file to BMP format and saves it.
     *
     * @param inputFile  Path to the input file (.pal or .vh)
     * @param outputFile Path where the BMP file will be saved
     * @throws IOException If there are errors reading the input or writing the output
     */
    private static void convertToBmp(String inputFile, String outputFile) throws IOException {
        // Read colors using appropriate reader
        PaletteReader reader = PaletteReaderFactory.createReader(inputFile);
        int[] colors = reader.readColors(inputFile);

        // Generate and save BMP
        bmpBuilder.createBmpFile(colors, outputFile);
        System.out.println("Successfully created palette visualization: " + outputFile);
    }

    /**
     * Converts a VH file to PAL format.
     *
     * @param inputFile  Path to the input VH file
     * @param outputFile Path where the PAL file will be saved
     * @throws IOException If there are errors reading the input or writing the output
     */
    private static void convertToPal(String inputFile, String outputFile) throws IOException {
        // Read input using VH reader
        PaletteReader reader = PaletteReaderFactory.createReader(inputFile);
        int[] colors = reader.readColors(inputFile);

        // Convert to PAL format bytes and write
        byte[] palData = ColorConverter.intArrayToRgbBytes(colors);
        Path outputPath = Paths.get(outputFile);
        Files.write(outputPath, palData);

        System.out.println("Successfully converted to PAL format: " + outputFile);
    }


    /**
     * Converts a PAL file to VH format.
     *
     * @param inputFile  Path to the input PAL file
     * @param outputFile Path where the VH file will be saved
     * @throws IOException If there are errors reading the input or writing the output
     */
    private static void convertToVh(String inputFile, String outputFile) throws IOException {
        // Read input using PAL reader
        PaletteReader reader = PaletteReaderFactory.createReader(inputFile);
        int[] palColors = reader.readColors(inputFile);

        // Convert to VH format
        int[] vhColors = ColorArrangement.palToVhOrder(palColors);

        // Create VH array content
        StringBuilder vhContent = new StringBuilder();
        vhContent.append("wire [23:0] lumacode_data_3s[0:63] = '{ ");

        for (int i = 0; i < vhColors.length; i++) {
            vhContent.append(String.format("24'h%06X", vhColors[i]));
            if (i < vhColors.length - 1) {
                vhContent.append(", ");
            }
        }
        vhContent.append("};");

        // Write VH file
        Path outputPath = Paths.get(outputFile);
        Files.writeString(outputPath, vhContent.toString());

        System.out.println("Successfully converted to VH format: " + outputFile);
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  PaletteTool tobmp <input.pal or input.vh> [output.bmp]");
        System.out.println("  PaletteTool topal <input.vh> [output.pal]");
        System.out.println("  PaletteTool tovh  <input.pal> [output.vh]");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("  tobmp  - Convert PAL or VH file to BMP visualization");
        System.out.println("  topal  - Convert VH file to PAL format");
        System.out.println("  tovh   - Convert PAL file to VH format");
        System.out.println();
        System.out.println("If output file is not specified, it will be created with");
        System.out.println("the same name as the input file but with the new extension.");
    }
}