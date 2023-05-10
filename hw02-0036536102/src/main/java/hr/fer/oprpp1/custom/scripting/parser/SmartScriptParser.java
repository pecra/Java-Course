package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.collections.ObjectStack;
import hr.fer.oprpp1.custom.scripting.elems.*;
import hr.fer.oprpp1.custom.scripting.lexer.Lexer;
import hr.fer.oprpp1.custom.scripting.lexer.Token;
import hr.fer.oprpp1.custom.scripting.lexer.TokenType;
import hr.fer.oprpp1.custom.scripting.nodes.*;

/**
 * Razred predstavlja parser koji stvara tree
 * cvorova koji nastaju parsiranjem nekog teksta.
 * @author Petra
 *
 */
public class SmartScriptParser {
	
	private Lexer lexer;
	private DocumentNode documentNode;
	
	/**
	 * Konstruktor stvara novi lekser iz predanog teksta
	 * i poziva funkciju parsiranja predanog teksta.
	 * @param docBody
	 */
	public SmartScriptParser(String docBody) {
		this.lexer = new Lexer(docBody);
	    this.parse();
	}
	
	/**
	 * Parsira tekst pomocu novostvorenog lexera.
	 * U documentNode pohranjuje tree novonastalih
	 * cvorova iz teksta ispunjenih vrijednostima
	 * iz teksta u obliku stringa.
	 * @throws SmartScriptParserException u slucaju greske
	 * ili nemogucnosti parsiranja
	 */
	public void parse() {
		 try {
			 this.documentNode= new DocumentNode();
				int forOpened = 0;
				ObjectStack stack = new ObjectStack();
				stack.push(documentNode);
				Token k = lexer.nextToken();
			
				while(k.getType() != TokenType.EOF) {
					
					if(lexer.getToken().getType() == TokenType.OPEN) {
						if(lexer.nextToken().getType() == TokenType.FOR) {
							if(lexer.nextToken().getType() != TokenType.VARIABLE) {
								throw new SmartScriptParserException();
							}
							Token t = lexer.getToken();
							Token a = lexer.nextToken();
							int i = 0;
							Token[] arr = {null,null,null,null,null};
							
							while(a.getType() != TokenType.CLOSE) {
								if(a.getType() == TokenType.EOF) {
									throw new SmartScriptParserException();
								}
								if(!(a.getType() == TokenType.VARIABLE ||
										a.getType() == TokenType.STRING ||
										a.getType() == TokenType.DOUBLE ||
										a.getType() == TokenType.INTEGER)) {
									throw new SmartScriptParserException();
								}
								arr[i] = a;
								i++;
								a = lexer.nextToken();					}
							
							if(i != 2 && i != 3) {
								System.out.println(i);
								throw new SmartScriptParserException();
							}
							
							ForLoopNode forLoop = new ForLoopNode((ElementVariable) this.tokenToElement(t),
									this.tokenToElement(arr[0]),
									this.tokenToElement(arr[1]),
									this.tokenToElement(arr[2]));
							forOpened++;
							Node n = (Node) stack.peek();
							n.addChildNode(forLoop);
							stack.push(forLoop);
						}
						
						if(lexer.getToken().getType() == TokenType.EQUALS) {
							Element[] e = new Element[10];
							int cntr = 0;
							while(lexer.nextToken().getType() != TokenType.CLOSE) {
								if(lexer.getToken().getType() == TokenType.EOF) {
									throw new SmartScriptParserException();
								}
								if(cntr >= e.length) {
									Element[] ee = new Element[e.length * 2];
									for(int i = 0; i < e.length; i++) {
										ee[i] = e[i];
									}
									e = ee;
								}
								e[cntr] = this.tokenToElement(lexer.getToken());
								cntr++;
							}
							Node n = (Node) stack.peek();
							n.addChildNode(new EchoNode(e));
						}
						
						if(lexer.getToken().getType() == TokenType.END) {
							if(forOpened < 1) {
								throw new SmartScriptParserException();
							}
							if(lexer.nextToken().getType() != TokenType.CLOSE) {
								throw new SmartScriptParserException();
							}
							forOpened--;
							stack.pop();
						}
						
						
					}
					else{
						if(lexer.getToken().getType() == TokenType.TXT) {
							Node n = (Node) stack.peek();
							n.addChildNode(new TextNode((String)lexer.getToken().getValue()));
						}
						else {
							throw new SmartScriptParserException();
						}
					}
					 k = lexer.nextToken();
				}
				if(forOpened != 0) {
					throw new SmartScriptParserException();
				}
		 } catch(Exception e) {
			 throw new SmartScriptParserException();
		 }
		
	}
	
	/**
	 * pretvara predani token u element odredene vrste.
	 * @param t predani token
	 * @return element nastao iz tokena
	 */
	private Element tokenToElement(Token t) {
		if(t.getType() == TokenType.DOUBLE) {
			return new ElementConstantDouble(Double.valueOf((String) t.getValue()));
		}
		if(t.getType() == TokenType.INTEGER) {
			return new ElementConstantInteger(Integer.valueOf((String) t.getValue()));
		}
		if(t.getType() == TokenType.FUNCTION) {
			return new ElementFunction((String) t.getValue());
		}
		if(t.getType() == TokenType.OPERATOR) {
			return new ElementOperator(String.valueOf(t.getValue()));
		}
		if(t.getType() == TokenType.STRING) {
			return new ElementString((String) t.getValue());
		}
		if(t.getType() == TokenType.VARIABLE) {
			return new ElementVariable((String) t.getValue());
		}
		else {
			throw new SmartScriptParserException();
		}
	}

	/**
	 * Vraca varijablu documertNode
	 * @return
	 */
	public DocumentNode getDocumentNode() {
		return this.documentNode;
	}

}
