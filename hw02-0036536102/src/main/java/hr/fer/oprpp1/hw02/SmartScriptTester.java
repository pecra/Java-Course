package hr.fer.oprpp1.hw02;

import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.nodes.TextNode;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * Razred za testiranje SmartScriptParsera.
 * @author Petra
 *
 */
public class SmartScriptTester {
	
	/**
	 * Glavna metoda koja parsira predani tekst iz filea,
	 * pretvara dobiveni documentnode nazad u tekst i ponovo ga parsira 
	 * te provjerava jednakost prvog documenNodea i novostvorenog documentNodes
	 * @param args
	 */
	public static void main(String[] args) {
		String filepath = args[0];
		String docBody = null;
		try {
			docBody = new String(
					 Files.readAllBytes(Paths.get(filepath)),
					 StandardCharsets.UTF_8
					);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		SmartScriptParser parser = null;
		try {
		 parser = new SmartScriptParser(docBody);
		} catch(SmartScriptParserException e) {
		 System.out.println("Unable to parse document!");
		 System.exit(-1);
		} catch(Exception e) {
		 System.out.println("If this line ever executes, you have failed this class!");
		 System.exit(-1);
		}
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = document.toString();
//		System.out.println(originalDocumentBody);
		
		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode document2 = parser2.getDocumentNode();
		// now document and document2 should be structurally identical trees
		boolean same = document.equals(document2);
		System.out.println(same);
		

	}
	

}
