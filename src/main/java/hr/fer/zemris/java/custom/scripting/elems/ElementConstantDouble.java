package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Razred varijable double.
 * @author Petra
 *
 */
public class ElementConstantDouble extends Element{
	
    private double value;
	
    /**
     * Konstruktor varijable double.
     * @param value vrijednost kojom se inicializira 
     * clanska varijabla
     */
	public ElementConstantDouble(double value) {
		super();
		this.value = value;
	}
	
	/**
	 * Vraca clansku varijablu value.
	 * @return clanska varijabla value
	 */
	public String getProp() {
		return Double.toString(value);
	}


	@Override
	public String asText() {
		return this.getProp();
	}
}
