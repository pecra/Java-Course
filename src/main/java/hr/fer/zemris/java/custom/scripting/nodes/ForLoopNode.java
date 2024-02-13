package hr.fer.zemris.java.custom.scripting.nodes;

import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.tokens.INodeVisitor;

/**
 * Predstavlja cvor for petlje.
 * @author Petra
 *
 */
public class ForLoopNode extends Node{
	
	private ElementVariable variable;
	private Element startExpression;
	private Element endExpression;
	private Element stepExpression; // can be null

	/**
	 * Konstruktor, inicijalizira varijable
	 * variable, startExpression, endExpression,
	 * stepExpression.
	 * @param variable
	 * @param startExpression
	 * @param endExpression
	 * @param stepExpression
	 */
	public ForLoopNode(ElementVariable variable,
			Element startExpression,
			Element endExpression,
			Element stepExpression) {
		super();
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
	}

	/**
	 * Vraca varijablu variable.
	 * @return  varijabla variable
	 */
	public ElementVariable getVariable() {
		return variable;
	}
    
	/**
	 * Vraca varijablu startExpression.
	 * @return  varijabla startExpression
	 */
	public Element getStartExpression() {
		return startExpression;
	}
	
	/**
	 * Vraca varijablu EndExpression.
	 * @return  varijabla EndExpression
	 */
	public Element getEndExpression() {
		return endExpression;
	}
	
	/**
	 * Vraca varijablu StepExpression.
	 * @return  varijabla StepExpression
	 */
	public Element getStepExpression() {
		return stepExpression;
	}
	
	@Override
	public void accept(INodeVisitor v) {
		v.visitForLoopNode(this);
	}
	
	/**
	 * Vraca clanske varijable i djecu
	 * spojene u String.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{$FOR");
		sb.append(variable.asText());
		sb.append(" ");
		sb.append(startExpression.asText());
		sb.append(" ");
		sb.append(endExpression.asText());
		sb.append(" ");
		if(stepExpression != null) {
			sb.append(stepExpression.asText());
			sb.append(" ");
		}
		sb.append("$}");
		
		int j = this.numberOfChildren();
		for(int i = 0; i < j; i++) {
			sb.append(this.getChild(i).toString());
            sb.append(" ");
		}
		
		sb.append("{$END$}");
		return sb.toString();
	}
	
	/**
     * Provjerava jesu li nodeovi 
     * iste vrste.
     */
	@Override
	public boolean equals(Object obj) {
		ForLoopNode node = (ForLoopNode)obj;
		int j = this.numberOfChildren();
		for(int i = 0; i < j; i++) {
			if(!(this.getClass().equals(node.getClass()))) {
				return false;
			}
		}
		return true;
	}
	
	

}
