# CSV to XML Converter

This is a Java program that converts CSV files to XML files. The program reads in a CSV file, splits it by the "buyer" column, and outputs a separate XML file for each buyer. The program can also output the data in CSV format. The program can handle invalid or illegal XML characters in the input data and correctly escape them in the output XML files. Additionally, the program can handle large input files without crashing or running out of memory.

## Requirements

The program requires Java 8 or higher to be installed on your system.

## Installation

To use this program, you can download the source code and compile it yourself, or you can download the pre-compiled JAR file.

### Option 1: Download the Source Code

1. Clone the repository or download the ZIP file.
2. Open the command prompt and navigate to the project directory.
3. Compile the program using the following command: `javac CSVParser.java`.
4. Run the program using the following command: `java CSVParser`.

### Option 2: Download the Pre-Compiled JAR File

1. Download the JAR file from the releases page on GitHub.
2. Open the command prompt and navigate to the directory where the JAR file is located.
3. Run the program using the following command: `java -jar CSVParser.jar`.

## Usage

To use the program, follow these steps:

1. Open the command prompt and navigate to the directory where the program is located.
2. Run the program using the following command: `java CSVParser`.
3. Enter the full path of the CSV file you want to convert.
4. Choose an option from the menu:

    * Enter `1` to output the data in CSV format.
    * Enter `2` to output the data in XML format.
    * Enter `3` to exit the program.

5. The program will create the output file(s) in the same directory as the input file.

## Testing

The program includes unit tests that can be run using JUnit. To run the tests, follow these steps:

1. Open the command prompt and navigate to the directory where the program is located.
2. Compile the program and the tests using the following command: `javac -cp junit-platform-console-standalone-1.7.2.jar;. *.java`.
3. Run the tests using the following command: `java -jar junit-platform-console-standalone-1.7.2.jar -cp . -c CSVParserTest`.

## Credits

This program was developed by Dayana Mladenova.