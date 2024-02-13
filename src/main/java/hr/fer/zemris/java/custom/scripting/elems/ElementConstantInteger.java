package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Razred varijable integer.
 * @author Petra
 *
 */
public class ElementConstantInteger extends Element{
	
    private int value;
	
    /**
     * Konstruktor varijable integer.
     * @param value vrijednost kojom se inicializira 
     * clanska varijabla
     */
	public ElementConstantInteger(int value) {
		super();
		this.value = value;
	}
	
	/**
	 * Vraca clansku varijablu value.
	 * @return clanska varijabla value
	 */
	public String getProp() {
		return Integer.toString(value);
	}


	@Override
	public String asText() {
		return this.getProp();
	}

}
