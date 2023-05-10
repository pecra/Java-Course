package hr.fer.oprpp1.custom.scripting.lexer;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.nodes.EchoNode;
import hr.fer.oprpp1.custom.scripting.nodes.ForLoopNode;
import hr.fer.oprpp1.custom.scripting.nodes.TextNode;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;

public class LexerTester {

	//@Disabled
	@Test
	public void testNotNull() {
		Lexer lexer = new Lexer("");
		
		assertNotNull(lexer.nextToken(), "Token was expected but null was returned.");
	}

	//@Disabled
	@Test
	public void testNullInput() {
		// must throw!
		assertThrows(NullPointerException.class, () -> new Lexer(null));
	}

	//@Disabled
	@Test
	public void testEmpty() {
		Lexer lexer = new Lexer("");
		
		assertEquals(TokenType.EOF, lexer.nextToken().getType(), "Empty input must generate only EOF token.");
	}

	//@Disabled
	@Test
	public void testGetReturnsLastNext() {
		// Calling getToken once or several times after calling nextToken must return each time what nextToken returned...
		Lexer lexer = new Lexer("");
		
		Token token = lexer.nextToken();
		assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
		assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
	}

	//@Disabled
	@Test
	public void testRadAfterEOF() {
		Lexer lexer = new Lexer("");

		// will obtain EOF
		lexer.nextToken();
		// will throw!
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}
	
	//@Disabled
	@Test
	public void testNoActualContent() {
		// When input is only of spaces, tabs, newlines, etc...
		Lexer lexer = new Lexer("   \r\n\t    ");
		
		assertEquals(TokenType.TXT, lexer.nextToken().getType(), "Input had no content. Lexer should generated only EOF token.");
	}

	//@Disabled
	@Test
	public void testInvalidEscapeEnding() {
		Lexer lexer = new Lexer("   \\");  // this is three spaces and a single backslash -- 4 letters string

		// will throw!
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}

	//@Disabled
	@Test
	public void testInvalidEscape() {
		Lexer lexer = new Lexer("   \\a    ");

		// will throw!
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}

	//@Disabled
	@Test
	public void testSingleEscapedDigit() {
		Lexer lexer = new Lexer("  \\{  ");

		// We expect the following stream of tokens
		Token correctData[] = {
			new Token(TokenType.TXT, "  \\{  "),
			new Token(TokenType.EOF, null)
		};

		checkTokenStream(lexer, correctData);
	}
	
	//@Disabled
	@Test
	public void testWordWithManyEscapes() {
		// Lets check for several words...
		Lexer lexer = new Lexer("  ab\\{\\{cd\\{$ab\\{\\{cd\\{\\\\ \r\n\t ");

		// We expect the following stream of tokens
		Token correctData[] = {
			new Token(TokenType.TXT, "  ab\\{\\{cd\\{$ab\\{\\{cd\\{\\\\ \r\n\t "), // this is 8-letter long, not nine! Only single backslash!
			new Token(TokenType.EOF, null)
		};

		checkTokenStream(lexer, correctData);
	}

	//@Disabled
	@Test
	public void testTwoNumbers() {
		// Lets check for several numbers...
		Lexer lexer = new Lexer("  1234  5678   ");
		lexer.setState(LexerState.INTAG);

		Token correctData[] = {
			new Token(TokenType.INTEGER, "1234"),
			new Token(TokenType.INTEGER, "5678"),
			new Token(TokenType.EOF, null)
		};

		checkTokenStream(lexer, correctData);
	}

	
	@Disabled
	@Test
	public void testSomeSymbols() {
		// Lets check for several symbols...
		Lexer lexer = new Lexer("  - + /  ");
		lexer.setState(LexerState.INTAG);

		Token correctData[] = {
			new Token(TokenType.OPERATOR, Character.valueOf('-')),
			new Token(TokenType.OPERATOR, Character.valueOf('+')),
			new Token(TokenType.OPERATOR, Character.valueOf('/')),
			new Token(TokenType.EOF, null)
		};

		checkTokenStream(lexer, correctData);
	}
	
	
	// Helper method for checking if lexer generates the same stream of tokens
	// as the given stream.
	private void checkTokenStream(Lexer lexer, Token[] correctData) {
		int counter = 0;
		for(Token expected : correctData) {
			Token actual = lexer.nextToken();
			String msg = "Checking token "+counter + ":";
			assertEquals(expected.getType(), actual.getType(), msg);
			assertEquals(expected.getValue(), actual.getValue(), msg);
			counter++;
		}
	}

	//@Disabled
	@Test
	public void testNullState() {
		assertThrows(NullPointerException.class, () -> new Lexer("").setState(null));
	}
	
	//@Disabled
	@Test
	public void testNotNullInExtended() {
		Lexer lexer = new Lexer("");
		lexer.setState(LexerState.INTAG);
		
		assertNotNull(lexer.nextToken(), "Token was expected but null was returned.");
	}

	//@Disabled
	@Test
	public void testEmptyInExtended() {
		Lexer lexer = new Lexer("");
		lexer.setState(LexerState.INTAG);
		
		assertEquals(TokenType.EOF, lexer.nextToken().getType(), "Empty input must generate only EOF token.");
	}

	//@Disabled
	@Test
	public void testGetReturnsLastNextInExtended() {
		// Calling getToken once or several times after calling nextToken must return each time what nextToken returned...
		Lexer lexer = new Lexer("");
		lexer.setState(LexerState.INTAG);
		
		Token token = lexer.nextToken();
		assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
		assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
	}

	//@Disabled
	@Test
	public void testRadAfterEOFInExtended() {
		Lexer lexer = new Lexer("");
		lexer.setState(LexerState.INTAG);

		// will obtain EOF
		lexer.nextToken();
		// will throw!
		assertThrows(LexerException.class, () -> lexer.nextToken());
	}
	
	//@Disabled
	@Test
	public void testNoActualContentInExtended() {
		// When input is only of spaces, tabs, newlines, etc...
		Lexer lexer = new Lexer("   \r\n\t    ");
		lexer.setState(LexerState.INTAG);
		
		assertEquals(TokenType.EOF, lexer.nextToken().getType(), "Input had no content. Lexer should generated only EOF token.");
	}
	
	//@Disabled
	@Test
	public void testInteger() {
		// When input is only of spaces, tabs, newlines, etc...
		Lexer lexer = new Lexer("5   r    ");
		lexer.setState(LexerState.INTAG);
		checkToken(lexer.nextToken(), new Token(TokenType.INTEGER,"5"));
		checkToken(lexer.nextToken(), new Token(TokenType.VARIABLE,"r"));
		checkToken(lexer.nextToken(), new Token(TokenType.EOF, null));
	}
	//@Disabled
	@Test
	public void testFunction() {
		// When input is only of spaces, tabs, newlines, etc...
		Lexer lexer = new Lexer("@sin   ");
		lexer.setState(LexerState.INTAG);
		checkToken(lexer.nextToken(), new Token(TokenType.FUNCTION,"@sin"));
		checkToken(lexer.nextToken(), new Token(TokenType.EOF, null));
	}
	//@Disabled
	@Test
	public void testString() {
		// When input is only of spaces, tabs, newlines, etc...
		Lexer lexer = new Lexer(" \"54\"   ");
		lexer.setState(LexerState.INTAG);
		checkToken(lexer.nextToken(), new Token(TokenType.STRING,"54"));
		checkToken(lexer.nextToken(), new Token(TokenType.EOF, null));
	}		
	//@Disabled
	@Test
	public void testStateChange() {
		// When input is only of spaces, tabs, newlines, etc...
		Lexer lexer = new Lexer("{$");
		checkToken(lexer.nextToken(), new Token(TokenType.OPEN,"{$"));
		checkToken(lexer.nextToken(), new Token(TokenType.EOF, null));
	}
	
	//@Disabled
	@Test
	public void testMultipartInput() {
	// Test input which has parts which are tokenized by different rules...
	Lexer lexer = new Lexer("This is sample text.\r\n {$FOR r $}");

	checkToken(lexer.nextToken(), new Token(TokenType.TXT, "This is sample text.\r\n "));
		
	lexer.setState(LexerState.INTAG);
	checkToken(lexer.nextToken(),new Token(TokenType.OPEN,"{$"));
    checkToken(lexer.nextToken(), new Token(TokenType.FOR,"FOR"));
	checkToken(lexer.nextToken(), new Token(TokenType.VARIABLE,"r"));
    checkToken(lexer.nextToken(),new Token(TokenType.CLOSE,"$}"));
	checkToken(lexer.nextToken(), new Token(TokenType.EOF, null));
		
	}
	
	//@Disabled
	@Test
	public void testMultipartInput2() {
		// Test input which has parts which are tokenized by different rules...
		Lexer lexer = new Lexer("{$ FOR i 1 10 1 $}\r\n"
				+ " This is {$= i $}"
				+ "^2) = {$= i i * @sin \"5.3 \\\"\"$}");

		lexer.setState(LexerState.INTAG);
		checkToken(lexer.nextToken(),new Token(TokenType.OPEN,"{$"));
        checkToken(lexer.nextToken(), new Token(TokenType.FOR,"FOR"));
		checkToken(lexer.nextToken(), new Token(TokenType.VARIABLE,"i"));
		checkToken(lexer.nextToken(), new Token(TokenType.INTEGER,"1"));
		checkToken(lexer.nextToken(), new Token(TokenType.INTEGER,"10"));
		checkToken(lexer.nextToken(), new Token(TokenType.INTEGER,"1"));
        checkToken(lexer.nextToken(),new Token(TokenType.CLOSE,"$}"));
        lexer.setState(LexerState.OUTOFTAG);
		checkToken(lexer.nextToken(),new Token(TokenType.TXT,"\r\n This is "));
		lexer.setState(LexerState.INTAG);
		checkToken(lexer.nextToken(),new Token(TokenType.OPEN,"{$"));
        checkToken(lexer.nextToken(), new Token(TokenType.EQUALS,"="));
		checkToken(lexer.nextToken(), new Token(TokenType.VARIABLE,"i"));
        checkToken(lexer.nextToken(),new Token(TokenType.CLOSE,"$}"));
        lexer.setState(LexerState.OUTOFTAG);
        checkToken(lexer.nextToken(),new Token(TokenType.TXT,"^2) = "));
        lexer.setState(LexerState.INTAG);
		checkToken(lexer.nextToken(),new Token(TokenType.OPEN,"{$"));
        checkToken(lexer.nextToken(), new Token(TokenType.EQUALS,"="));
		checkToken(lexer.nextToken(), new Token(TokenType.VARIABLE,"i"));
		checkToken(lexer.nextToken(), new Token(TokenType.VARIABLE,"i"));
		checkToken(lexer.nextToken(), new Token(TokenType.OPERATOR,'*'));
		checkToken(lexer.nextToken(), new Token(TokenType.FUNCTION,"@sin"));
		checkToken(lexer.nextToken(), new Token(TokenType.STRING,"5.3 \\\""));
        checkToken(lexer.nextToken(),new Token(TokenType.CLOSE,"$}"));
        lexer.setState(LexerState.OUTOFTAG);
		checkToken(lexer.nextToken(), new Token(TokenType.EOF, null));
		
	}
	
	
	
	private void checkToken(Token actual, Token expected) {
			String msg = "Token are not equal.";

			assertEquals(expected.getValue(), actual.getValue(), msg);
			assertEquals(expected.getType(), actual.getType(), msg);
	}
	
	private String readExample(int n) {
		  try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("extra/primjer"+n+".txt")) {
		    if(is==null) throw new RuntimeException("Datoteka extra/primjer"+n+".txt je nedostupna.");
		    byte[] data = is.readAllBytes();
		    String text = new String(data, StandardCharsets.UTF_8);
		    return text;
		  } catch(IOException ex) {
		    throw new RuntimeException("Greška pri čitanju datoteke.", ex);
		  }
		}
	
	//@Disabled
		@Test
		public void testParser1() {
			String text = readExample(1);
			SmartScriptParser p = new SmartScriptParser(text);
			DocumentNode doc = p.getDocumentNode();
			assertEquals(doc.getChild(0).getClass(), new TextNode(text).getClass());
			assertEquals(doc.numberOfChildren(), 1);
			// isparsiraj text
			// assertaj da si dobio točno jedan textnode
		}
		//@Disabled
		    @Test
			public void testParser2() {
				String text = readExample(2);
				SmartScriptParser p = new SmartScriptParser(text);
				DocumentNode doc = p.getDocumentNode();
				assertEquals(doc.getChild(0).getClass(), new TextNode(text).getClass());
				assertEquals(doc.numberOfChildren(), 1);
			}
		//@Disabled
			@Test
			public void testParser3() {
				String text = readExample(3);
				SmartScriptParser p = new SmartScriptParser(text);
				DocumentNode doc = p.getDocumentNode();
				assertEquals(doc.getChild(0).getClass(), new TextNode(text).getClass());
				assertEquals(doc.numberOfChildren(), 1);
			}
				@Disabled
				@Test
				public void testParser4() {
					String text = readExample(4);
					assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(text));
				}
				@Disabled
				@Test
				public void testParser5() {
					String text = readExample(5);
					assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(text));
				}
				//@Disabled
				@Test
				public void testParser6() {
					String text = readExample(6);
					SmartScriptParser p = new SmartScriptParser(text);
					DocumentNode doc = p.getDocumentNode();
					assertEquals(doc.getChild(0).getClass(), new TextNode(text).getClass());
					assertEquals(doc.getChild(1).getClass(), new EchoNode(null).getClass());
					assertEquals(doc.getChild(2).getClass(), new TextNode(text).getClass());
					assertEquals(doc.numberOfChildren(), 3);
				}
				//@Disabled
				@Test
				public void testParser7() {
					String text = readExample(7);
					SmartScriptParser p = new SmartScriptParser(text);
					DocumentNode doc = p.getDocumentNode();
					assertEquals(doc.getChild(0).getClass(), new TextNode(text).getClass());
					assertEquals(doc.getChild(1).getClass(), new EchoNode(null).getClass());
					assertEquals(doc.getChild(2).getClass(), new TextNode(text).getClass());
					assertEquals(doc.numberOfChildren(), 3);
				}
				
				//@Disabled
				@Test
				public void testParser8() {
					String text = readExample(9);
					assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(text));
				}
				
				//@Disabled
				@Test
				public void testParser9() {
					String text = readExample(9);
					assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(text));
				}
				
				//@Disabled
				@Test
				public void testParser10() {
					String text = readExample(10);
					SmartScriptParser p = new SmartScriptParser(text);
					DocumentNode doc = p.getDocumentNode();
					ForLoopNode nod = (ForLoopNode) doc.getChild(1);
					ForLoopNode nod2 = (ForLoopNode) doc.getChild(3);
					assertEquals(doc.getChild(0).getClass(), new TextNode(text).getClass());
					assertEquals(doc.getChild(2).getClass(), new TextNode(text).getClass());
					assertEquals(doc.numberOfChildren(), 4);
					assertEquals(nod.getChild(0).getClass(), new TextNode(text).getClass());
					assertEquals(nod.getChild(1).getClass(), new EchoNode(null).getClass());
                    assertEquals(nod.getChild(2).getClass(), new TextNode(text).getClass());
					assertEquals(nod.numberOfChildren(), 3);
					assertEquals(nod2.getChild(0).getClass(), new TextNode(text).getClass());
					assertEquals(nod2.getChild(1).getClass(), new EchoNode(null).getClass());
                    assertEquals(nod2.getChild(2).getClass(), new TextNode(text).getClass());
                    assertEquals(nod2.getChild(3).getClass(), new EchoNode(null).getClass());
                    assertEquals(nod2.getChild(4).getClass(), new TextNode(text).getClass());
					assertEquals(nod2.numberOfChildren(),5);
				}	
				
				//@Disabled
				@Test
				public void testParser11() {
					String text = readExample(11);
					SmartScriptParser p = new SmartScriptParser(text);
					DocumentNode doc = p.getDocumentNode();
					ForLoopNode nod = (ForLoopNode) doc.getChild(0);
					ForLoopNode nod2 = (ForLoopNode) nod.getChild(1);
					assertEquals(doc.getChild(0).getClass(), new ForLoopNode(null, null, null, null).getClass());
					assertEquals(nod.getChild(1).getClass(), new ForLoopNode(null, null, null, null).getClass());
					assertEquals(doc.numberOfChildren(), 1);
					assertEquals(nod.numberOfChildren(), 3);
				}	
}
