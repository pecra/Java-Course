package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Razred varijable string.
 * @author Petra
 *
 */
public class ElementString extends Element{
    private String value;
	
    /**
     * Konstruktor varijable string.
     * @param value vrijednost kojom se inicializira 
     * clanska varijabla
     */
	public ElementString(String value) {
		super();
		this.value = value;
	}
	
	/**
	 * Vraca clansku varijablu value.
	 * @return clanska varijabla value
	 */
	public String getProp() {
		return value;
	}


	@Override
	public String asText() {
		StringBuilder sb = new StringBuilder();
		sb.append('\"');
		sb.append(this.getProp());
		sb.append('\"');
		return sb.toString();
	}

}
