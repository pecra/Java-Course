package hr.fer.oprpp1.hw04.db;

import java.util.LinkedList;
import java.util.List;
/**
 * Klasa predstavlja parser queryja.
 * Zapisuje elemente iz queryja u listu ConditionalExpressions-a.
 * @author Petra
 *
 */
public class QueryParser {

	List<ConditionalExpression> expressions;
	
	public QueryParser(String quer) {
		Parser pars = new Parser(quer);
		pars.parse();
		this.expressions = pars.getList();	
	}
	
	/**
	 * Sadrži li query samo jedan izraz.
	 * @return
	 */
	boolean isDirectQuery() {
		return this.expressions.size() == 1 && this.expressions.get(0).getFieldGetter() == FieldValueGetters.JMBAG;
	}
	
	/**
	 * Vraća query ako je zadovoljen uvjet isDirectQuery.
	 * @return
	 * @throws IllegalStateException ako nije direct query
	 */
	String getQueriedJMBAG() {
		if(!this.isDirectQuery()) {
			throw new IllegalStateException("Not direct Query.");
		} 
		return this.expressions.get(0).getStringLiteral();
	}
	
	/**
	 * Vraća listu svih izraza iz queryja.
	 * @return
	 */
	List<ConditionalExpression> getQuery() {
		return this.expressions;
	}
	/**
	 * Klasa koja tekst iz queryja pretvara u tokene za lakše parsiranje teksta.
	 * @author Petra
	 *
	 */
	private class Lekser {
		
		String inp;
		String lastString = " ";
		
		Lekser(String inp) {
			this.inp = inp;
		}
		
		String getLastString() {
			return this.lastString;
		}
		
		/**
		 * vraća idući token iz teksta.
		 * @return
		 */
		Token lekser() {
			if(inp.length() <= 0) {
				return Token.STOP;
			}
			inp = inp.trim(); // makni praznine s pocetka i kraja
			if(inp.startsWith("jmbag")) {
				inp = inp.substring(5);
				inp = inp.stripLeading();
				return Token.JMBAG;
			}
			if(inp.startsWith("firstName")) {
				inp = inp.substring(9);
				inp = inp.stripLeading();
				return Token.FIRST_NAME;
			}
			if(inp.startsWith("lastName")) {
				inp = inp.substring(8);
				inp = inp.stripLeading();
				return Token.LAST_NAME;
			}
			if(inp.startsWith("!=")) {
				inp = inp.substring(2);
				inp = inp.stripLeading();
				return Token.NOT_EQUALS;
			}
			if(inp.startsWith(">=")) {
				inp = inp.substring(2);
				inp = inp.stripLeading();
				return Token.GREATER_OR_EQUALS;
			}
			if(inp.startsWith("<=")) {
				inp = inp.substring(2);
				inp = inp.stripLeading();
				return Token.LESS_OR_EQUALS;
			}
			if(inp.startsWith("=")) {
				inp = inp.substring(1);
				inp = inp.stripLeading();
				return Token.EQUALS;
			}
			if(inp.startsWith(">")) {
				inp = inp.substring(1);
				inp = inp.stripLeading();
				return Token.GREATER;
			}
			
			if(inp.startsWith("<")) {
				inp = inp.substring(1);
				inp = inp.stripLeading();
				return Token.LESS;
			}
			if(inp.startsWith("LIKE")) {
				inp = inp.substring(4);
				inp = inp.stripLeading();
				return Token.LIKE;
			}
			if(inp.substring(0,3).toUpperCase().startsWith("AND")) {
				inp = inp.substring(3);
				inp = inp.stripLeading();
				return Token.AND;
			}
			if(inp.startsWith("\"")) {
				inp = inp.substring(1); //ukloni "
				int index = inp.indexOf('\"');
				lastString = inp.substring(0,index);
				inp = inp.substring(index + 1);
				inp = inp.stripLeading();
				return Token.STRING;
			}
			throw new IllegalArgumentException("Error in query");	
		}
	}
	
	/**
	 * Klasa predstavlja parser koji pomoću tokena iz leksera
	 * parsira izraze i slaže ih u  listu.
	 * @author Petra
	 *
	 */
	private class Parser {
		
		Token token = Token.BEGIN;
		Lekser lekser;
		List<ConditionalExpression> expressions = new LinkedList<>();
		
		Parser(String str) {
			this.lekser = new Lekser(str);
		}
		
		/**
		 * Vraća listu izraza.
		 * @return
		 */
		public List<ConditionalExpression> getList() {
			return this.expressions;
		}
		private IFieldValueGetter getGetter(Token tok) {
			switch(tok) {
			case LAST_NAME:
				return FieldValueGetters.LAST_NAME;
			case FIRST_NAME:
				return FieldValueGetters.FIRST_NAME;
			default:
				return FieldValueGetters.JMBAG;
			}
		}
		/**
		 * Pretvara token u operator.
		 * @param tok
		 * @return
		 */
		private IComparisonOperator getOperator(Token tok) {
			switch(tok) {
			case LESS:
				return ComparisonOperators.LESS;
			case LESS_OR_EQUALS:
				return ComparisonOperators.LESS_OR_EQUALS;
			case GREATER:
				return ComparisonOperators.GREATER;
			case GREATER_OR_EQUALS:
				return ComparisonOperators.GREATER_OR_EQUALS;
			case EQUALS:
				return ComparisonOperators.EQUALS;
			case NOT_EQUALS:
				return ComparisonOperators.NOT_EQUALS;
			case LIKE:
				return ComparisonOperators.LIKE;
			default:
				return null;
			}
			
		}
		/**
		 * Parsira tekst dok god ima još tokena u tekstu.
		 */
		public void parse() {
			token = lekser.lekser();
			if(token == Token.STOP) {
				throw new IllegalArgumentException("Empty query!");
			}
			while(this.token != Token.STOP) {
				if(token != Token.FIRST_NAME &&
				   token != Token.LAST_NAME &&	
				   token != Token.JMBAG) {
					throw new IllegalArgumentException("Wrong attribute!");
				}
				IFieldValueGetter getter = getGetter(token);
				token = lekser.lekser();
				if(token == Token.FIRST_NAME ||
				   token == Token.LAST_NAME ||
				   token == Token.STRING ||
				   token == Token.AND ||
				   token == Token.JMBAG) {
							throw new IllegalArgumentException("Wrong operator!");
						}
				IComparisonOperator operator = getOperator(token);
				token = lekser.lekser();
				if(token != Token.STRING){
					throw new IllegalArgumentException("Wrong string!");
				}
				String str = lekser.lastString;
                expressions.add(new ConditionalExpression(getter,str,operator));
			    token = lekser.lekser();
			    if(token == Token.AND) {
					token = lekser.lekser();
				}
			    else {
			    	if(token == Token.STOP) {
						break;
					}
			    	else {
			    		throw new IllegalArgumentException("Illegal arguments in query!");
			    	}
			    	
			    }
			}
		}
		
	}
	
	
}
