package taskProject;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

public class CSVParser {
    public static void main(String[] args) {
    	
    	Scanner input = new Scanner(System.in);
    	char choice;
    	boolean filePathFlag = true;
    	String inputFilePath = null;
    	
    	while(true) {
    		if(filePathFlag) {
    			System.out.println("Please specify full csv file path: ");
            	inputFilePath = input.nextLine();
            	
            	int point_split = inputFilePath.lastIndexOf('.');
            	if(!inputFilePath.substring(point_split + 1).equals("csv")) {
            		System.out.println("Incorrect file path format! Try again...");
                	continue;
            	}else {
            		filePathFlag = false;
            	}
    		}
    		System.out.println("Menu:\n 1. CSV output\n 2. XML output\n 3. Exit");

            
            System.out.print("Input(choose an option number): ");
            choice = input.next().charAt(0);

            switch (choice) {
                case '1' -> outputCSV(inputFilePath);
                case '2' -> outputXML(inputFilePath);
                case '3' -> {
                    System.out.println("Exited program!");
                    input.close();
                    System.exit(0);
                }
                default -> System.out.println("Incorrect input! Try again...");
            }
    	}
    }
    
    public static void outputCSV(String filePath){
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Read the header row
            String headerLine = br.readLine();
            Map<String, PrintWriter> csvWriters = new HashMap<>();
            
            // Read each row and split by 'buyer'
            String row;
            while ((row = br.readLine()) != null) {
                String[] values = row.split(",");
                String buyer = values[0];
                
                // Create a new CSV file or XML document for this buyer if it doesn't exist
                if (!csvWriters.containsKey(buyer)) {
                    String csvFilePath = buyer + ".csv";
                    PrintWriter csvWriter = new PrintWriter(new FileWriter(csvFilePath));
                    csvWriter.println(headerLine);
                    csvWriters.put(buyer, csvWriter);
                }
                
                // Write the row to the CSV file or XML document for this buyer
                String[] outputValues = Arrays.copyOf(values, values.length - 1);
                String outputLine = String.join(",", outputValues);
                
                PrintWriter csvWriter = csvWriters.get(buyer);
                csvWriter.println(outputLine);
            }
            
            // Close the CSV writers and write the XML documents to files
            for (PrintWriter csvWriter : csvWriters.values()) {
                csvWriter.close();
            }
            
            System.out.println("Output files created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
    
    public static void outputXML(String filePath){
    	try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Read the header row
            String headerLine = br.readLine();
            String[] headers = headerLine.split(",");
            StringBuilder normalizer = new StringBuilder();
            for(int i=0; i<headers[0].length();i++){
            	if(!(headers[0].charAt(i)>='a' && headers[0].charAt(i)>='z')){
            		normalizer.append(headers[0].charAt(i));
            	}
            }
            headers[0] = normalizer.toString();
            Map<String, Document> xmlDocuments = new HashMap<>();
            
            // Read each row and split by 'buyer'
            String row;
            while ((row = br.readLine()) != null) {
            	String[] values = row.split(",");
                String buyer = values[0];
                
                if (!xmlDocuments.containsKey(buyer)) {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document xmlDocument = builder.newDocument();
                    Element rootElement = xmlDocument.createElement("buyer");
                    xmlDocument.appendChild(rootElement);
                    xmlDocuments.put(buyer, xmlDocument);
                }
                
                // Write the row to XML document for this buyer
                String[] outputValues = Arrays.copyOf(values, values.length - 1);
               
                
                Document xmlDoc = xmlDocuments.get(buyer);
                Element invoiceElement = xmlDoc.createElement("invoice");
                for (int i = 0; i < outputValues.length; i++) {
                    Element element = xmlDoc.createElement(escapeXML(headers[i]));
                    element.appendChild(xmlDoc.createTextNode(outputValues[i]));
                    invoiceElement.appendChild(element);
                }
                xmlDoc.getDocumentElement().appendChild(invoiceElement);
                
            }
            
            // write the XML documents to files
            for (Map.Entry<String, Document> entry : xmlDocuments.entrySet()) {
                String xmlFilePath = entry.getKey() + ".xml";
                Document xmlDocument = entry.getValue();
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(xmlDocument);
                StreamResult result = new StreamResult(new File(xmlFilePath));
                transformer.transform(source, result);
            }
            
            System.out.println("Output files created successfully.");
        } catch (IOException | ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
	}
    
    public static String escapeXML(String str) {
        String escapedStr = str.replaceAll("&", "&amp;")
                              .replaceAll("<", "&lt;")
                              .replaceAll(">", "&gt;")
                              .replaceAll("\"", "&quot;")
                              .replaceAll("'", "&apos;");
        return escapeIllegalXmlChars(escapedStr);
    }

    public static String escapeIllegalXmlChars(String str) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == 0x9 || c == 0xA || c == 0xD || c >= 0x20 && c <= 0xD7FF || c >= 0xE000 && c <= 0xFFFD) {
                sb.append(c);
                
            }
        }
        return sb.toString();
    }
}