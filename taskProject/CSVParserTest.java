package taskProject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


import org.junit.jupiter.api.*;


public class CSVParserTest{

	@Test
	public void testOutputCSV() {
		String inputFilePath = "test.csv";

        CSVParser.outputCSV(inputFilePath);

        // Assert that output files were created for each buyer in the input file
        String[] buyers = {"Alice", "Bob", "Charlie"};
        for (String buyer : buyers) {
            String csvFilePath = buyer + ".csv";
            try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
                String headerLine = br.readLine();
                Assertions.assertEquals("Name,Item,Price", headerLine);
                String row;
                while ((row = br.readLine()) != null) {
                    String[] values = row.split(",");
                    Assertions.assertEquals(buyer, values[0]);
                    Assertions.assertTrue(values[1].startsWith("Item"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}

	@Test
	public void testOutputXML() {
		String inputFilePath = "test.csv";

        CSVParser.outputXML(inputFilePath);
        
     // Assert that output files were created for each buyer in the input file
        String[] buyers = {"Alice", "Bob", "Charlie"};
        for (String buyer : buyers) {
            String xmlFilePath = buyer + ".xml";
            try (BufferedReader br = new BufferedReader(new FileReader(xmlFilePath))) {
                String xmlContent = br.readLine() + br.readLine(); // Skip XML header
                Assertions.assertTrue(xmlContent.contains("<buyer>"));
                Assertions.assertTrue(xmlContent.contains("<invoice>"));
                Assertions.assertTrue(xmlContent.contains("<Name>"));
                Assertions.assertTrue(xmlContent.contains("<Item>"));
                Assertions.assertTrue(xmlContent.contains("</invoice>"));
                Assertions.assertTrue(xmlContent.contains("</buyer>"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}

	@Test
	public void testEscapeXML() {
		// Test escaping special XML characters
		String input = "This <tag> contains &amp; and \" quotes";
        String expectedOutput = "This &lt;tag&gt; contains &amp;amp; and &quot; quotes";
        String output = CSVParser.escapeXML(input);
        Assertions.assertEquals(expectedOutput, output);
        
        // Test escaping an empty string
        input = "";
        expectedOutput = "";
        output = CSVParser.escapeXML(input);
        Assertions.assertEquals(expectedOutput, output);
        
        // Test escaping a string that contains only special XML characters
        input = "<>&\"'";
        expectedOutput = "&lt;&gt;&amp;&quot;&apos;";
        output = CSVParser.escapeXML(input);
        Assertions.assertEquals(expectedOutput, output);
	}

	@Test
	public void testEscapeIllegalXmlChars() {
		String input = "This contains a \u0000 null character";
        String expectedOutput = "This contains a  null character";
        String output = CSVParser.escapeIllegalXmlChars(input);
        Assertions.assertEquals(expectedOutput, output);
        
        // Test escaping an empty string
        input = "";
        expectedOutput = "";
        output = CSVParser.escapeIllegalXmlChars(input);
        Assertions.assertEquals(expectedOutput, output);
        
        // Test escaping a string that contains no illegal XML characters
        input = "This is a normal string";
        expectedOutput = "This is a normal string";
        output = CSVParser.escapeIllegalXmlChars(input);
        Assertions.assertEquals(expectedOutput, output);

	}

}
