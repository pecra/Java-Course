package hr.fer.oprpp1.hw02.prob1;
/**
 * Jezicni procesor za leksicku analizu teksta.
 * Pretvara dijelove teksta u tokene navedene u 
 * TokenType. Postoje 2 stanje u kojima se moze nalaziti.
 * @author Petra
 * @version 1.0
 *
 */

public class Lexer {
    
	private LexerState state = LexerState.BASIC;//Stanje u kojem se lekser nalazi.
    private char[] data; // ulazni tekst
    private Token token; // trenutni token
    private int currentIndex; // indeks prvog neobrađenog znaka
    // konstruktor prima ulazni tekst koji se tokenizira
    
    /**
     * Konstruktor leksera stvara novi lekser
     * za predani tekst.
     * @param text String kojeg pohranjuje kao CharArray
     */
    public Lexer(String text) {
    	data = text.toCharArray(); 
    	currentIndex = 0; 
    }
    // generira i vraća sljedeći token
    // baca LexerException ako dođe do pogreške
    /**
     * Vraca iduci token iz teksta. Ako je u 
     * BASIC nacinu rada koriste se svi tokeni iz enumeracije
     * TokenType, ne moraju biti odvojeni razmakom
     *  i postoji mogucnost escapeanja,
     * ako je u EXTENDED nacinu rada nema escapeanja,
     * a svi tokeni su odvojeni razmakom i to je jedino sto 
     * cini granicu izmedu tokena. Znakom '#' mijenja se nacin rada.
     * @return iduci token
     * @throws LexerException u slucaju pogreske prilikom tokeniziranja
     * @throws LexerException ako je zadnji token prije ponovnog poziva bio EOF
     */
    public Token nextToken() {
    	
    	try {
			if(this.state == LexerState.BASIC) {
				
				boolean escaping = false;
				
				if((currentIndex < data.length)  && 
						(data[currentIndex] == '\r' ||
						data[currentIndex] == '\n' ||
						data[currentIndex] == '\t' ||
						data[currentIndex] == ' ')) {
					while((currentIndex < data.length)  &&
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
				
				if(currentIndex >= data.length) {
					Token t = new Token(TokenType.EOF,null);
					this.token = t;
					return t;
				}
				
				if(data[currentIndex] == '\\'){
						if(currentIndex < (data.length - 1) && 
							(!Character.isLetter(data[currentIndex + 1]))) {
							escaping = true;
							currentIndex = currentIndex + 1;;
						}
						else {
							throw new LexerException("Neispravan unos!");
						}
				}		
				try {
					if(Character.isLetter(data[currentIndex]) || escaping) {
						int tmpInd = Integer.valueOf(currentIndex);
						StringBuilder sb = new StringBuilder();
						while((tmpInd < data.length) &&
								(Character.isLetter(data[tmpInd]) || escaping)) {
							sb.append(data[tmpInd]);
							tmpInd++;
							escaping = false;
							if((tmpInd < data.length) && data[tmpInd] == '\\'){
				    			if(!Character.isLetter(data[tmpInd + 1])) {
				    				escaping = true;
				    				tmpInd = tmpInd + 1;;
				    			}
				    			else {
				    				throw new LexerException();
				    			}
							}	
						}
						this.currentIndex = tmpInd;
						Token t = new Token(TokenType.WORD, sb.toString());
			    		this.token = t;
			    		return t;
					}
					if(this.isDigit(data[currentIndex])) {
						long l = Long.parseLong(String.valueOf(data[currentIndex]));
						int tmpInd = Integer.valueOf(currentIndex);
						StringBuilder sb = new StringBuilder();
						while((tmpInd < data.length) &&
								this.isDigit(data[tmpInd])) {
								long dat = Long.parseLong(String.valueOf(data[tmpInd]));
								sb.append(data[tmpInd]);
								tmpInd++;
							
						}
						this.currentIndex = tmpInd;
						Token t = new Token(TokenType.NUMBER, Long.parseLong(sb.toString()));
			    		this.token = t;
			    		return t;
					}
					if(!this.isInteger(data[currentIndex])) {
						
						if(data[currentIndex] == '#') { // prebaci u drugo stanje
							this.setState(LexerState.EXTENDED);
				    	}
						
						Token t = new Token(TokenType.SYMBOL, data[currentIndex]);
						this.token = t;
						currentIndex++;
						return t;
					}
					try {
						int i = Integer.parseInt(String.valueOf(data[currentIndex]));
						throw new Exception("ulaz ne valja!");
					} catch(NullPointerException e) {
					}
					
				} catch(Exception e) {
					throw new LexerException(e.getMessage());
				}
				return null;
				
			}
			else {
				
				if(!(this.token == null) && this.token.tokenType.equals(TokenType.EOF)) {
					throw new LexerException("Kraj filea!");
				}
				
				if(currentIndex >= data.length) {
					Token t = new Token(TokenType.EOF,null);
					this.token = t;
					return t;
				}
				
				if(data[currentIndex] == '#') { // prebaci u drugo stanje
					
					this.setState(LexerState.BASIC);

					Token t = new Token(TokenType.SYMBOL, data[currentIndex]);
					this.token = t;
					currentIndex++;
					return t; // zavrsi sa ovim stanjem
				}
				
				if((currentIndex < data.length)  && 
						(data[currentIndex] == '\r' ||
						data[currentIndex] == '\n' ||
						data[currentIndex] == '\t' ||
						data[currentIndex] == ' ')) {
					while((currentIndex < data.length)  &&
							(data[currentIndex] == '\r' ||
							data[currentIndex] == '\n' ||
							data[currentIndex] == '\t' ||
							data[currentIndex] == ' ')) {
						currentIndex++;
					}
				}
				StringBuilder sb = new StringBuilder();
				while((currentIndex < data.length)  && 
						data[currentIndex] != '\r' &&
						data[currentIndex] != '\n' &&
						data[currentIndex] != '\t' &&
						data[currentIndex] != ' ' &&
						data[currentIndex] != '#') {		
					sb.append(data[currentIndex]);
					currentIndex++;
				}
				
				if(sb.length() == 0) {
					return new Token(TokenType.EOF, null); // ako su samo praznine ub tekstu
				}
				return new Token(TokenType.WORD, sb.toString());
			}
		} catch (Exception e) {
			throw new LexerException(e.getMessage());
		}
    	
    	
    }
    // vraća zadnji generirani token; može se pozivati
    // više puta; ne pokreće generiranje sljedećeg tokena
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
    public boolean isDigit(char c) {
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
    public boolean isInteger(char c) {
    	try {
			long dat = Integer.parseInt(String.valueOf(c));
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
    }
    
    /**
     * Postavljanje stanja u kojem se lexer nalazi.
     * @param state BASIC ili EXTENDED
     */
    public void setState(LexerState state) {
    	if(state == null) {
    		throw new NullPointerException();
    	}
    	this.state = state;
    }
}
