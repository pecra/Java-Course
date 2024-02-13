package hr.fer.zemris.java.custom.scripting.tokens;


/**
 * Jezicni procesor za leksicku analizu teksta.
 * Pretvara dijelove teksta u tokene navedene u 
 * TokenType.
 * @author Petra
 * @version 1.0
 *
 */


public class Lexer {
	
	private LexerState state = LexerState.OUTOFTAG;
    private char[] data; // ulazni tekst
    private Token token; // trenutni token
    private int currentIndex; // indek
    
    /**
     * Konstruktor leksera stvara novi lekser
     * za predani tekst.
     * @param text String kojeg pohranjuje kao CharArray
     */
    public Lexer(String text) {
    	data = text.toCharArray(); 
    	currentIndex = 0; 
    }
    
    /**
     * Vraca iduci token iz teksta.
     * @return iduci token
     * @throws LexerException u slucaju pogreske prilikom tokeniziranja
     * @throws LexerException ako je zadnji token prije ponovnog poziva bio EOF
     */
    public Token nextToken() {
    	boolean negNumber = false;
    	boolean funct = false;
    	Token t;
    	int dataLen = data.length;
    	 
    	
    	if(!(this.token == null) && this.token.tokenType.equals(TokenType.EOF)) {
    		throw new LexerException("Kraj filea!");
    	}
    	
    	if(this.endOfFile()) {
    		t = new Token(TokenType.EOF,null);
    		this.token = t;
    		return t;
    	}
    	
    	if(data[currentIndex] == '{') {
    		if(currentIndex + 1 < dataLen &&
    				data[currentIndex + 1] == '$') {
    			this.setState(LexerState.INTAG) ;
    			this.currentIndex = currentIndex + 2;
    			t = new Token(TokenType.OPEN,"{$");
    			this.token = t;
    			return t;
    		}
    	}
    	
    	if((currentIndex < dataLen)  && 
    		    this.state == LexerState.INTAG &&
    			(data[currentIndex] == '\r' ||
				data[currentIndex] == '\n' ||
				data[currentIndex] == '\t' ||
				data[currentIndex] == ' ')) {
			while((currentIndex < dataLen)  && 
        			(data[currentIndex] == '\r' ||
    				data[currentIndex] == '\n' ||
    				data[currentIndex] == '\t' ||
    				data[currentIndex] == ' ')) {
				currentIndex++;
			}
		}
    	
    	if(!(this.token == null) && this.token.tokenType.equals(TokenType.EOF)) {
    		throw new LexerException("Kraj filea!");
    	}
    	
    	if(this.endOfFile()) {
    		t = new Token(TokenType.EOF,null);
    		this.token = t;
    		return t;
    	}
    	
    	if(data[currentIndex] == '$') {
    		if(currentIndex + 1 < dataLen &&
    				data[currentIndex + 1] == '}') {
    			this.setState(LexerState.OUTOFTAG) ;
    			this.currentIndex = currentIndex + 2;
    			t = new Token(TokenType.CLOSE,"$}");
    			this.token = t;
    			return t;
    		}
    	}
    	if(this.state == LexerState.INTAG) {

        	if(data[currentIndex] == '-') {
        		if(currentIndex + 1 < dataLen
            				&& this.isDigit(data[currentIndex + 1])) {
        			negNumber = true;
        			currentIndex++;
        		}
        	}
        	
        	if(data[currentIndex] == '@') {
        		if(currentIndex + 1 < dataLen
            				&& Character.isLetter(data[currentIndex + 1])) {
        			funct = true;
        			currentIndex++;
        		}
        	}
        	
    		if(data[currentIndex] == '\"') {
    			StringBuilder sb = new StringBuilder();
    			currentIndex++;
    			while(currentIndex < dataLen
            				&& data[currentIndex] != '\"') {
    				if(data[currentIndex] == '\\') {
    					currentIndex++;
    					if(currentIndex < dataLen && 
    							(data[currentIndex] == '\\' ||
                				    data[currentIndex] == '\"' ||
                				    data[currentIndex] == 'n' ||
                				    data[currentIndex] == 'r' ||
                				    data[currentIndex] == 't')) {
    						sb.append('\\');
                			sb.append(data[currentIndex]);
                			currentIndex++;
                		}
                		else {
                			throw new LexerException();
                		}
                	}
    				else {
    					sb.append(data[currentIndex]);
    					currentIndex++;
    				}
    			
    			}
    			currentIndex++;
    			t = new Token(TokenType.STRING,sb.toString());
    			this.token = t;
    			return 	t;
    		}
    		
    		 
    		if(data[currentIndex] == '=') {
    			currentIndex++;
        		t = new Token(TokenType.EQUALS,"=");
    			this.token = t;
    			return 	t;
        	}
    		
    		if(Character.toUpperCase(data[currentIndex]) == 'F') {
        		if(Character.toUpperCase(data[currentIndex + 1]) == 'O') {
        			if(Character.toUpperCase(data[currentIndex + 2]) == 'R') {
            			this.currentIndex = currentIndex + 3;
            			t = new Token(TokenType.FOR,"FOR");
            			this.token = t;
            			return 	t;
        			}
        		}
        	}
    		
    		if(Character.toUpperCase(data[currentIndex]) == 'E') {
        		if(Character.toUpperCase(data[currentIndex + 1]) == 'N') {
        			if(Character.toUpperCase(data[currentIndex + 2]) == 'D') {
            			this.currentIndex = currentIndex + 3;
            			t = new Token(TokenType.END,"END");
            			this.token = t;
            			return 	t;
        			}
        		}
        	}
    		
    		try {
        		if(Character.isLetter(data[currentIndex])) {
        			int tmpInd = Integer.valueOf(currentIndex);
        			StringBuilder sb = new StringBuilder();
        			if(funct) {
        				sb.append('@');
        			}
        			while((tmpInd < dataLen) &&
        					(Character.isLetter(data[tmpInd]) ||
        					    this.isDigit(data[currentIndex]) ||
        					    data[currentIndex] == '_')) {
        				sb.append(data[tmpInd]);
        				tmpInd++;
        				if((tmpInd < dataLen) && data[tmpInd] == '\\'){
        	    			if(!Character.isLetter(data[tmpInd + 1])) {
        	    				tmpInd = tmpInd + 1;;
        	    			}
        	    			else {
        	    				throw new LexerException();
        	    			}
        				}	
        			}
        			TokenType ti = TokenType.VARIABLE;
        			if(funct) {
        				ti = TokenType.FUNCTION;
        				funct = false;
        			}
        			this.currentIndex = tmpInd;
        			t = new Token(ti, sb.toString());
            		this.token = t;
            		return t;
        		}
        		
        		if(negNumber || this.isDigit(data[currentIndex])) {
        			boolean isDouble = false;
        			long l = Long.parseLong(String.valueOf(data[currentIndex]));
        			int tmpInd = Integer.valueOf(currentIndex);
        			StringBuilder sb = new StringBuilder();
        			if(negNumber) {
        				sb.append('-');
        				negNumber = false;
        			}
        			while((tmpInd < dataLen) &&
        					this.isDigit(data[tmpInd])) {
        				    if(tmpInd + 1 < dataLen &&
        				    		data[tmpInd + 1] == '.') {
        				    	sb.append(data[tmpInd]);
        				    	sb.append('.');
        				    	tmpInd = tmpInd + 2;
        				    	isDouble = true;
        				    }
        				    else {
            					sb.append(data[tmpInd]);
            					tmpInd++;
        				    }
        				
        			}
        			this.currentIndex = tmpInd;
        			if(isDouble) {
        				 t = new Token(TokenType.DOUBLE, sb.toString());
        			}
        			else {
        				 t = new Token(TokenType.INTEGER, sb.toString());
        			}
            		this.token = t;
            		return t;
        		}
        		
        		if(!this.isInteger(data[currentIndex])) {
        			
        			if(data[currentIndex] == '+' ||
        					data[currentIndex] == '-' ||
        					data[currentIndex] == '*' ||
        					data[currentIndex] == '/' ||
        					data[currentIndex] == '^') { // prebaci u drugo stanje
        				t = new Token(TokenType.OPERATOR, data[currentIndex]);
            			this.token = t;
            			currentIndex++;
            			return t;
        	    	}
        			
        			
        		}
        		
        		try {
        			int i = Integer.parseInt(String.valueOf(data[currentIndex]));
        			throw new LexerException();
        		} catch(NullPointerException e) {
        		}
        		
        	} catch(Exception e) {
        		throw new LexerException();
        	}
    	}
    	
    	if(this.state == LexerState.OUTOFTAG) {
    		if(!(this.token == null) && this.token.tokenType.equals(TokenType.EOF)) {
        		throw new LexerException();
        	}
        	
        	if(this.endOfFile()) {
        		t = new Token(TokenType.EOF,null);
        		this.token = t;
        		return t;
        	}
    		StringBuilder sb = new StringBuilder();
    		while(!this.endOfFile()) {
    			if(data[currentIndex] == '{') {
    	    		if(currentIndex + 1 < dataLen &&
    	    				data[currentIndex + 1] == '$') {
    	    			this.setState(LexerState.INTAG) ;
    	    			t = new Token(TokenType.TXT,sb.toString());
            			this.token = t;
            			return 	t;
    	    		}
    	    	}
    			if(data[currentIndex] == '\\') {
    				currentIndex++;
    				if(currentIndex < dataLen
            				&& data[currentIndex] == '\\') {
            			sb.append(data[currentIndex]);
            			sb.append(data[currentIndex]);
            			currentIndex++;
            		}
            		else {
            			if(currentIndex < dataLen
            				&& data[currentIndex] == '{'){
            				sb.append('\\');
            				sb.append(data[currentIndex]);
                			currentIndex++;
            			}
            			else {
            				throw new LexerException();
            			}
            		}
            	}
    			else {
    				sb.append(data[currentIndex]);
    				currentIndex++;
    			}
    		}
    		t = new Token(TokenType.TXT,sb.toString());
			this.token = t;
			return 	t;
			
    	}	
    	return new Token(TokenType.FAIL,"NE");
    }
    
    /**
     * Vraca zadnji generirani token. Moze se pozivati
     * vise puta. Ne pokrece generiranje slijedeceg tokena.
     * @return zadnji generirani token.
     */
    public Token getToken() {
    	return token;
    }
    
    /**
     * Provjerava je li predani parametar
     * moguce pohraniti u varijablu tipa long.
     * @param c predani znak
     * @return je li c long
     */
    private boolean isDigit(char c) {
    	try {
			long dat = Long.parseLong(String.valueOf(c));
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
    }
    
    /**
     * Provjerava je li predani parametar
     * moguce pohraniti u varijablu tipa integer.
     * @param c predani znak
     * @return je li c integer
     */
    private boolean isInteger(char c) {
    	try {
			long dat = Integer.parseInt(String.valueOf(c));
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
    }
    
    /**
     * Postavljanje stanja u kojem se lexer nalazi.
     * @param state INTAG ili OUTOFTAG
     */
    public void setState(LexerState state) {
    	if(state == null) {
    		throw new NullPointerException();
    	}
    	this.state = state;
    }
    
    /**
     * Nalazi li se lexer na kraju filea. 
     */
    private boolean endOfFile() {
    	return currentIndex >= data.length;
    }
    

}
