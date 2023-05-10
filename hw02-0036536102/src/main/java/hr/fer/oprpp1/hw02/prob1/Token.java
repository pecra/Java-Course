package hr.fer.oprpp1.hw02.prob1;

/**
 * Token koji se generira iz nekog teksta.
 * @author Petra
 *
 */
public class Token {
	
	TokenType tokenType;
	private Object value;
    
	/**
	 * Konstruktor za novi token.
	 * @param type Vrsta tokena
	 * @param value vrijednost tokena
	 */
    public Token(TokenType type, Object value) {
    	this.tokenType = type;
    	this.value = value;
    }
    
    /**
     * Vraca vrijednost tokena.
     * @return objekt vrijednost tokena
     */
    public Object getValue() {
    	return this.value;
    }
    
    /**
     * Vraca vrstu tokena.
     * @return TokenType vrsta tokena
     */
    public TokenType getType() {
    	return this.tokenType;
    }
}
