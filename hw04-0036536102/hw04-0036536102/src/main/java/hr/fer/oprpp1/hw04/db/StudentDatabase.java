package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Klasa predstavlja bazu podataka studenata.
 * @author Petra
 *
 */
public class StudentDatabase {
	
	private List<StudentRecord> list = new ArrayList<>();
	private Map<String,StudentRecord> map = new HashMap<>();
	
	public StudentDatabase(List<String> lista) {
		for(String s : lista) {
			String[] inputs = s.split("\\s+");
			int len = inputs.length;
			String finalGrade = inputs[len - 1];
			int grade = Integer.valueOf(finalGrade);
			if(grade > 5 || grade < 1) {
				System.out.println("Upisana neispravna ocjena!");
				System.exit(0);
			}
			
			String jmbag = inputs[0];
			String firstName = inputs[len - 2];
			String lastName;
			if(len != 4) {
				StringBuilder sb = new StringBuilder();
				sb.append(inputs[1]);
				sb.append(" ");
				sb.append(inputs[2]);
				lastName = sb.toString();
			}
			else {
				lastName = inputs[1];
			}
			
			StudentRecord stud = new StudentRecord(jmbag, lastName, firstName, finalGrade);
			
			StudentRecord old = map.put(jmbag, stud);
			if(old != null) {
				System.out.println("Dupli podaci!");
				System.exit(0);
			}
			list.add(stud);
		}
	}
	
	/**
	 * Vraća studenta koji ima predani jmbag.
	 * @param jmbag
	 * @return
	 */
	public StudentRecord forJMBAG(String jmbag) {
		return map.get(jmbag);
	}
	/**
	 * Vraca Listu studenata koje prihvaca predani filter.
	 * @param filter
	 * @return
	 */
	public List<StudentRecord> filter(IFilter filter){
		List<StudentRecord> temp = new ArrayList<>();
		for(StudentRecord rec : list) {
			if(filter.accepts(rec)) {
				temp.add(rec);
			}
		}
		return temp;
	}
	
}
