package hr.fer.oprpp1.hw04.db;

/**
 * Sučelje koje služi za izvođenje klasa koje dohvaćaju neki od podataka o studentu.
 * @author Petra
 *
 */
public interface IFieldValueGetter {

	public String get(StudentRecord record);
}
