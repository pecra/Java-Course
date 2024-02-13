package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParserException;
import hr.fer.zemris.java.custom.scripting.tokens.INodeVisitor;

public class TreeWriter {
	
	/**
	 * ya svaki node ispisuje od cega je nastao.
	 * @author Petra
	 *
	 */
	private static class WriterVisitor 
	implements INodeVisitor{
		
		StringBuilder sb = new StringBuilder();

		public void visitTextNode(TextNode node) {
			sb.append(node.getProp());
			
		}

		public void visitForLoopNode(ForLoopNode node) {
			sb.append("{$FOR");
			sb.append(node.getVariable().asText());
			sb.append(" ");
			sb.append(node.getStartExpression().asText());
			sb.append(" ");
			sb.append(node.getEndExpression().asText());
			sb.append(" ");
			if(node.getStepExpression() != null) {
				sb.append(node.getStepExpression().asText());
				sb.append(" ");
			}
			sb.append("$}");
			
			int j = node.numberOfChildren();
			for(int i = 0; i < j; i++) {
				sb.append(node.getChild(i).toString());
	            sb.append(" ");
			}
			
			sb.append("{$END$}");
			
		}

		public void visitEchoNode(EchoNode node) {
			sb.append("{$=");
			for(int i = 0, j = node.getProp().length; i < j && node.getProp()[i] != null; i++) {
					sb.append(node.getProp()[i].asText());
					sb.append(" ");
			}
			sb.append("$}");
			
		}

		public void visitDocumentNode(DocumentNode node) {
			
			int j = node.numberOfChildren();
			for(int i = 0; i < j; i++) {
				node.getChild(i).accept(this);
	            sb.append(" ");
			}
			this.write();
		}
		
		public void write() {
			System.out.println(this.sb.toString());
		}
		
	}
	
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
		WriterVisitor visitor = new WriterVisitor();
		document.accept(visitor);
		

	}

}
