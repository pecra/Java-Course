package hr.fer.oprpp1.custom.scripting.nodes;

/**
 * Predstavlja pocetni cvor.
 * @author Petra
 *
 */
public class DocumentNode extends Node{
	
	/**
	 * Vraca povezane sve stringove koji
	 * predstavljaju djecu.
	 */
    @Override
	public String toString() {
        StringBuilder sb = new StringBuilder();
		
		int j = this.numberOfChildren();
		for(int i = 0; i < j; i++) {
			sb.append(this.getChild(i).toString());
            sb.append(" ");
		}
		return sb.toString();
		
	}
    
    /**
     * Provjerava je li stablo cvorova jednako.
     */
    @Override
	public boolean equals(Object obj) {
		DocumentNode node = (DocumentNode)obj;
		int j = this.numberOfChildren();
		for(int i = 0; i < j; i++) {
			if(!(this.getClass().equals(node.getClass()))) {
				return false;
			}
		}
		return true;
	}

}
