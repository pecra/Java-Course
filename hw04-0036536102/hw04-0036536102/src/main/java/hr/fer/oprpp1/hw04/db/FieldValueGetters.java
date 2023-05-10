package hr.fer.oprpp1.hw04.db;

/**
 * Razred u kojem su implementirani razredi za dohvaćanje podataka o studentu.
 * 
 * @author Petra
 *
 */
public class FieldValueGetters {

	public static final IFieldValueGetter FIRST_NAME = (record) -> record.firstName;
	public static final IFieldValueGetter LAST_NAME = (record) -> record.lastName;
	public static final IFieldValueGetter JMBAG = (record) -> record.jmbag;
	
	
}
