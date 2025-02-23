# PaletteTool

# PaletteTool

PaletteTool is a command-line tool for converting and visualizing color palettes. It helps convert `.pal` files for OSSC firmware and create BMP previews.

## Features

- Convert `.pal` to `.vh` (OSSC firmware format)
- Convert `.vh` to `.pal` (raw RGB format)
- Create BMP previews from `.pal` or `.vh`
- Ensures correct 64-color format

## Installation

### Requirements
- Java 21

### Build

Use Maven to build:
```sh
mvn clean package
```

## Usage

```sh
java -jar PaletteTool.jar <command> <input file> [output file]
```

### Commands

- Convert `.pal` to `.vh`:
  ```sh
  java -jar PaletteTool.jar tovh input.pal [output.vh]
  ```
- Convert `.vh` to `.pal`:
  ```sh
  java -jar PaletteTool.jar topal input.vh [output.pal]
  ```
- Create BMP preview:
  ```sh
  java -jar PaletteTool.jar tobmp input.pal [output.bmp]
  ```

## File Formats

- **`.pal`**: 64 colors, 3 bytes per color (R, G, B)
- **`.vh`**: Verilog format, 64 colors in `24'hRRGGBB`

## Example

Convert and preview a palette:
```sh
java -jar PaletteTool.jar tovh example.pal
java -jar PaletteTool.jar tobmp example.vh
```

## License
MIT License

## Contributing
Pull requests welcome!
