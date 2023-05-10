package hr.fer.oprpp1.hw04.db;
/**
 * Sučelje koje služi za implementaciju klasa filtera.
 * @author Petra
 *
 */
public interface IFilter {
	public boolean accepts(StudentRecord record);
}
