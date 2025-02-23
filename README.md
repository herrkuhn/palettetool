# PaletteTool

PaletteTool is a command-line tool for converting and visualizing color palettes. It helps convert `.pal` files for OSSC firmware and create BMP previews.

## Features

- Convert `.pal` to `.vh` (OSSC firmware format)
- Convert `.vh` to `.pal` (raw RGB format)
- Create BMP previews from `.pal` or `.vh`
- Ensures correct 64-color format

## Installation

Binaries for Linux, OSX and Windows are provided with each [Release](https://github.com/herrkuhn/palettetool/releases). Additionally every commit triggers a build under [Actions](https://github.com/herrkuhn/palettetool/actions), artefacts are at the bottom of each workflow.

Here's how to build from source:

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
java -jar PaletteTool.jar topal example.pal
java -jar PaletteTool.jar tobmp example.vh
```

## OSSC Compilation

The content of the generated `.vh` file either replaces the array `lumacode_data_3s` in `rtl/tvp7002_frontend.v` at line 158, or `lumacode_data_3s` is pulled in directly by including it (and removing the `lumacode_data_3s` entry in `tvp7002_frontend.v`).

OSSC firmware can be generated following the instructions in the official OSSC repository.

Additionally between steps #3 and #4 a patch needs to be applied, that seems to be a rather recent change that hasn't been documented yet:

```sh
patch -p0 < scripts/qsys.patch
```

After successful compilation and the "make generate_hex" step I had to copy the generated .hex (`software/sys_controller/mem_init/sys_onchip_memory2_0.hex`) manually to `sys/synthesis/submodules/` and follow the steps from the NOTE section under "Building RTL (bitstream)".

## License
MIT License
