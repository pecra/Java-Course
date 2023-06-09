package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Razred varijable operator.
 * @author Petra
 *
 */
public class ElementOperator extends Element{
    private String symbol;
	
    /**
     * Konstruktor varijable operator.
     * @param symbol vrijednost kojom se inicializira 
     * clanska varijabla
     */
	public ElementOperator(String symbol) {
		super();
		this.symbol = symbol;
	}
	
	/**
	 * Vraca clansku varijablu symbol.
	 * @return clanska varijabla simbol
	 */
	private String getProp() {
		return symbol;
	}

	@Override
	public String asText() {
		return this.getProp();
	}

}
