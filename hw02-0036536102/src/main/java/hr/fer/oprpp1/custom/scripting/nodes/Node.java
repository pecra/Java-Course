package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;

/**
 * Razred predstavlja osnovu za izvodenje
 * razreda razlicitih vrsta cvorova.
 * @author Petra
 *
 */
public class Node {

	ArrayIndexedCollection nodes;
	
	/**
	 * Dodaje predani node u array
	 * koji predstavlja djecu ovog nodea.
	 * @param child
	 */
	public void addChildNode(Node child) {
		if(this.nodes == null) {
			this.nodes = new ArrayIndexedCollection();
		}

		this.nodes.add(child);
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
