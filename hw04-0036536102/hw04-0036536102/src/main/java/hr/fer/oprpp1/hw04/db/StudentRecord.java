package hr.fer.oprpp1.hw04.db;

import java.util.Objects;

/**
 * Klasa predstavlja zapis podataka za jednog studenta.
 * @author Petra
 *
 */
public class StudentRecord {
	String jmbag;
	String lastName;
	String firstName;
	String finalGrade;
	
	/**
	 * Javni konstruktor
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, String finalGrade) {
		super();
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.finalGrade = finalGrade;
	}

	/**
	 * Računanje hashCode-a na temelju jmbag-a.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(jmbag);
	}

	/**
	 * Jesu li dva objekta jednaka usporedbom jbag-a.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentRecord other = (StudentRecord) obj;
		return Objects.equals(jmbag, other.jmbag);
	}

	
	
	
	
	

}
