package hr.fer.oprpp1.custom.scripting.lexer;

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
