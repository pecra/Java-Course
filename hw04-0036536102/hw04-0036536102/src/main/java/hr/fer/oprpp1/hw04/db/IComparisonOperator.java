package hr.fer.oprpp1.hw04.db;
/**
 * Sučelje koje predstavlja osnvu za izvođenje klasa komparatora.
 * @author Petra
 *
 */
public interface IComparisonOperator {
	
	public boolean satisfied(String value1, String value2);

}
