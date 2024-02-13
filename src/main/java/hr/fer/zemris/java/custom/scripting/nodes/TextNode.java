package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.tokens.INodeVisitor;

/**
 * Predstavlja cvor u kojem se nalazi samo tekst.
 * @author Petra
 *
 */
public class TextNode extends Node{
	
	private String text;
    
	/**
	 * Konstruktor, inicijalizira clasnku varijablu
	 * na predani string.
	 * @param text
	 */
	public TextNode(String text) {
		super();
		this.text = text;
	}
	
	/**
	 * Vraca clansku varijablu.
	 * @return clanska varijabla
	 */
	public String getProp() {
		return text;
	}
	
	/**
	 * Vraca clansku varijablu 
	 * u obliku Stringa.
	 */
	@Override
	public String toString() {
		return this.getProp();
	}

    /**
     * Provjerava jesu li cvorovi 
     * iste vrste.
     */
	@Override
	public boolean equals(Object obj) {
		TextNode node = (TextNode)obj;
		return(this.getClass().equals(node.getClass()));
	}
	
	@Override
	public void accept(INodeVisitor v) {
		v.visitTextNode(this);
	}

   

}
