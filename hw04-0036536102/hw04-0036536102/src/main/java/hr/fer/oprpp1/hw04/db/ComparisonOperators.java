package hr.fer.oprpp1.hw04.db;

/**
 * Klasa koja predstavlja implementaciju svih ponuđenih operatora.
 * @author Petra
 *
 */
public class ComparisonOperators {
	/**
	 * Je li prva vrijednost leksikografski manja od druge.
	 * 
	 */
	public static final IComparisonOperator LESS = (value1,value2) -> value1.compareTo(value2) < 0;
	/**
	 * Je li prva vrijednost leksikografski manja ili jednaka drugoj.
	 */
	public static final IComparisonOperator LESS_OR_EQUALS = (value1,value2) -> value1.compareTo(value2) <= 0;
	/**
	 * Je li prva vrijednost leksikografski veća od druge.
	 */
	public static final IComparisonOperator GREATER = (value1,value2) -> value1.compareTo(value2) > 0;
	/**
	 * Je li prva vrijednost leksikografski veća ili jednaka drugoj.
	 */
	public static final IComparisonOperator GREATER_OR_EQUALS = (value1,value2) -> value1.compareTo(value2) >= 0;
	/**
	 * Je li prva vrijednost leksikografski jednaka drugoj.
	 */
	public static final IComparisonOperator EQUALS = (value1,value2) -> value1.compareTo(value2) == 0;
	/**
	 * Je li prva vrijednost leksikografski različita od druge.
	 */
	public static final IComparisonOperator NOT_EQUALS = (value1,value2) -> value1.compareTo(value2) != 0;
	/**
	 *Odgovara li prva vrijednost obliku zadanom drugom vrijednošću.
	 */
	public static final IComparisonOperator LIKE = new IComparisonOperator() {
        @Override
		public boolean satisfied(String value1, String value2) {
        	if(!value2.contains("*")) {
        		return value1.compareTo(value2) == 0;
        	}
        	String other = value2;
        	other = other.replaceFirst("\\*", "");
        	if(other.contains("*")) {
        		throw new IllegalArgumentException("Vise od jedne *!!");
        	}
			if(value2.startsWith("*")) { //zavrsava li sa predanim value2
				value2 = value2.substring(1, value2.length());
				if(value1.endsWith(value2)) {
					return true;
				}
				else {
					return false;
				}
			}
			if(value2.endsWith("*")) { //pocinje li sa predanim value1
				value2 = value2.substring(0, value2.length() - 1);
				if(value1.startsWith(value2)) {
					return true;
				}
				else {
					return false;
				}
			}
			int index = value2.indexOf("*");
			if(value1.substring(0,index).startsWith(value2.substring(0,index))){
				if((value1.length()-index >= value2.length()-1-index) && value1.endsWith(value2.substring(index + 1, value2.length()))) {
					return true;
				}
				return false;
			}
			else {
				return false;
			}
			
		}
		
	};
	
	

}
