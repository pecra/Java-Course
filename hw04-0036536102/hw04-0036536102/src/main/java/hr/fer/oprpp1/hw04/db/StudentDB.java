package hr.fer.oprpp1.hw04.db;


import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Klasa predstavlja program koji prima query i 
 * ispisuje studente iz datoteke koja predstavlja bazu podataka 
 * koji odgovaraju zadanom queryju.
 * @author Petra
 *
 */
public class StudentDB {
	
     final static int jmbgLen = 10;
	 static int maxName = 0;
	 static int maxLastName = 0;
	 static boolean direct = false;
	 
	public static void main(String[] args) throws IOException {
		 try {
			List<String> lines = Files.readAllLines(
						 Paths.get("./database.txt"),
						 StandardCharsets.UTF_8
						);
			 StudentDatabase db = new StudentDatabase(lines);
			 
			 Scanner scanner = new Scanner(new InputStreamReader(System.in));
			 while(true) {
				 System.out.println("Please enter your query: ");
				 String input = scanner.nextLine();
				 if(input.startsWith("exit")) {
					 System.out.println("Goodbye!");
					 System.exit(0);
				 }
				 input = input.substring(5);
				 QueryParser parser = new QueryParser(input);
				 List<StudentRecord> lista = getQuery(parser,db);//db.filter(new QueryFilter(parser.getQuery()));
				 for(StudentRecord r : lista) {
					 if(r.firstName.length() > maxName) {
						 maxName = r.firstName.length();
					 }
					 if(r.lastName.length() > maxLastName) {
						 maxLastName = r.lastName.length();
					 }
				 }
				 System.out.println(queryPrint(lista));
			 }
		} catch (IOException e) {
			System.out.println("Neispravna datoteka");
		}
		 catch (Exception e) {
				System.out.println("Krivo unesen query!");
			}
	}
	
	/**
	 * 
	 * Vraća listu studenata koji odgovara uvjetima iz queryja.
	 * @return lista StudentRecord
	 */
	private static List<StudentRecord> getQuery(QueryParser parser, StudentDatabase db) {
		List<StudentRecord> rec = new ArrayList<>();
		if(parser.isDirectQuery()) {
			  StudentRecord r = db.forJMBAG(parser.getQueriedJMBAG());
			  rec.add(r);
			  direct = true;
			  return rec;
			 } 
		 else {
			 direct = false;
			 return db.filter(new QueryFilter(parser.getQuery()));
		 }
			  
	}
    /**
     * Vraca string u kojem su zadanim formatom zapisani
     * svi StudentRecord-i iz predane liste.
     */
	public static String queryPrint(List<StudentRecord> student) {
		StringBuilder sb = new StringBuilder();
		if(direct) {
			sb.append("Using index for record retrieval.");
			sb.append('\n');
		}
		if(student.size() != 0) {
			sb.append('+');
			for(int i = 0; i < jmbgLen + 2;i++) {
				sb.append('=');
			}
			sb.append('+');
			for(int i = 0; i < maxLastName + 2;i++) {
				sb.append('=');
			}
			sb.append('+');
			for(int i = 0; i < maxName + 2;i++) {
				sb.append('=');
			}
			sb.append('+');
			for(int i = 0; i < 3;i++) {
				sb.append('=');
			}
			sb.append('+');
			sb.append('\n');
			for(StudentRecord r : student) {
				  sb.append('|');
				  sb.append(' ');
				  sb.append(r.jmbag);
				  sb.append(' ');
				  sb.append('|');
				  sb.append(' ');
				  sb.append(r.lastName);
				  for(int j = 0; j < maxLastName - r.lastName.length();j++) {
					  sb.append(' '); 
				  }
				  sb.append(' ');
				  sb.append('|');
				  sb.append(' ');
				  sb.append(r.firstName);
				  for(int j = 0; j < maxName - r.firstName.length();j++) {
					  sb.append(' '); 
				  }
				  sb.append(' ');
				  sb.append('|');
				  sb.append(' ');
				  sb.append(r.finalGrade);
				  sb.append(' ');
				  sb.append('|');
				  sb.append(' ');
				  sb.append('\n');
			  }
			sb.append('+');
			for(int i = 0; i < jmbgLen + 2;i++) {
				sb.append('=');
			}
			sb.append('+');
			for(int i = 0; i < maxLastName + 2;i++) {
				sb.append('=');
			}
			sb.append('+');
			for(int i = 0; i < maxName + 2;i++) {
				sb.append('=');
			}
			sb.append('+');
			for(int i = 0; i < 3;i++) {
				sb.append('=');
			}
			sb.append('+');
			sb.append('\n');
		}
		
		sb.append("Records selected:" + student.size());
		sb.append('\n');
		return sb.toString();
	}
}