package hr.fer.zemris.java.custom.scripting.elems;

/**
 * Razred lementa varijabla.
 * @author Petra
 *
 */
public class ElementVariable extends Element{
    
	private String name;
	
	/**
	 * Konstruktor elementa varijabla
	 * @param name
	 */
	public ElementVariable(String name) {
		super();
		this.name = name;
	}
	
	/**
	 * Vraca clanske varijable elementa.
	 * @return String ime
	 */
	public String getProp() {
		return name;
	}

   
	@Override
	public String asText() {
		return this.getProp();
	}

}
