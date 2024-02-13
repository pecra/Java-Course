package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.tokens.INodeVisitor;

/**
 * Predstavlja cvor taga jednakosti.
 * @author Petra
 *
 */
public class EchoNode extends Node {
	
	private Element[] elements;
	
	/**
	 * Konstruktor, inicijalizira clasnku varijablu
	 * na predano polje elemenata.
	 * @param elements predano polje
	 */
	public EchoNode(Element[] elements) {
		super();
		this.elements = elements;
	}
	
	/**
	 * Vraca polje elemenata.
	 * @return polje elemenata
	 */
	public Element[] getProp() {
		return elements;
	}
	
	/**
	 * Vraca sve elemente
	 * polja povezane u string.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{$=");
		for(int i = 0, j = elements.length; i < j && elements[i] != null; i++) {
				sb.append(elements[i].asText());
				sb.append(" ");
		}
		sb.append("$}");
		return sb.toString();
	}
	
	/**
     * Provjerava jesu li cvorovi 
     * iste vrste.
     */
	@Override
	public boolean equals(Object obj) {
		EchoNode node = (EchoNode)obj;
		return(this.getClass().equals(node.getClass()));
	}
	
	@Override
	public void accept(INodeVisitor v) {
		v.visitEchoNode(this);
	}
}
