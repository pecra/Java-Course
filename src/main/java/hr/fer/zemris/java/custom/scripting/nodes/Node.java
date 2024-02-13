package hr.fer.zemris.java.custom.scripting.nodes;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.custom.scripting.tokens.INodeVisitor;


/**
 * Razred predstavlja osnovu za izvodenje
 * razreda razlicitih vrsta cvorova.
 * @author Petra
 *
 */
public class Node {

	List<Node> nodes;
	
	/**
	 * Dodaje predani node u array
	 * koji predstavlja djecu ovog nodea.
	 * @param child
	 */
	public void addChildNode(Node child) {
		if(this.nodes == null) {
			this.nodes = new LinkedList();
		}

		this.nodes.add(child);
	}
	
	public void accept(INodeVisitor v) {
		
	}
	
	/**
	 * Vraca broj djece.
	 * @return broj djece
	 */
	public int numberOfChildren() {
		if(this.nodes == null) {
			return 0;
		}
		return this.nodes.size();
	}
	
	/**
	 * Vraca dijete na predanom indexu.
	 * @param index
	 * @return Node dijete
	 */
	public Node getChild(int index) {
		return (Node) this.nodes.get(index);
	}

}
