package de.nrq.core.reader;

import de.nrq.core.color.ColorArrangement;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of PaletteReader for VH format files.
 * Handles Verilog arrays containing 64 colors in 24'hRRGGBB format.
 * Converts the VH color order to PAL color order during reading.
 */
public class VhReader implements PaletteReader {
    private static final Pattern COLOR_PATTERN = Pattern.compile("24'h([0-9a-fA-F]{6})");

    @Override
    public int[] readColors(String filename) throws IOException {
        String content = Files.readString(Paths.get(filename));

        // Extract all hex values
        Matcher matcher = COLOR_PATTERN.matcher(content);
        List<Integer> colors = new ArrayList<>();

        while (matcher.find()) {
            colors.add(Integer.parseInt(matcher.group(1), 16));
        }

        if (colors.size() != TOTAL_COLORS) {
            throw new IllegalArgumentException(
                    String.format("Invalid number of colors in VH file. Expected %d colors.", TOTAL_COLORS)
            );
        }

        // Convert colors to array and transform to PAL order
        int[] vhColors = colors.stream().mapToInt(Integer::intValue).toArray();
        return ColorArrangement.vhToPalOrder(vhColors);
    }
}