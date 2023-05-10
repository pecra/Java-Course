package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Razred varijable funkcija.
 * @author Petra
 *
 */
public class ElementFunction extends Element{
    private String name;
	
    /**
     * Konstruktor varijable funkcija.
     * @param name vrijednost kojom se inicializira 
     * clanska varijabla
     */
	public ElementFunction(String name) {
		super();
		this.name = name;
	}
	
	/**
	 * Vraca clansku varijablu name.
	 * @return clanska varijabla name
	 */
	private String getProp() {
		return name;
	}


	@Override
	public String asText() {
		return this.getProp();
	}
}
