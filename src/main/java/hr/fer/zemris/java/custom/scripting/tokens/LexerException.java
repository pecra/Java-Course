package hr.fer.zemris.java.custom.scripting.tokens;

/**
 * Greska kju uzrokuje Lexer.
 * @author Petra
 *
 */
public class LexerException extends RuntimeException{
	
	public LexerException(String s) {
		super(s);
	}
	
	public LexerException() {
		super();
	}

}
