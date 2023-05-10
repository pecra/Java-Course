
package hr.fer.oprpp1.hw04.db;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Tester {
	//List<String> lines;
	public List<String> ca() {
		try {
			return Files.readAllLines(Paths.get("./database.txt"),
					 StandardCharsets.UTF_8
					);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	List<String> lines = this.ca();	 
    StudentDatabase db = new StudentDatabase(lines);
	
	@Disabled
	@Test
	public void testForJMBAG() {
		StudentRecord stud;
		stud = db.forJMBAG("0000000019");
		assertEquals(stud.firstName, "Slaven" );
	}

	IFilter a = (StudentRecord n) -> true;
	IFilter b = (StudentRecord n) -> false;
	
	@Test
	public void testFilters() {
			assertEquals(true,a.accepts(null) );
			assertEquals(false,b.accepts(null) );
	}
	
	@Test
	public void testFilterMEthod() {
		List<StudentRecord> prva = db.filter(a);
		List<StudentRecord> druga = db.filter(b);
		assertEquals(prva.size(),63);
		assertEquals(druga.size(),0);
	}
	

	@Test
	public void testOperators() {
	     	assertEquals(ComparisonOperators.NOT_EQUALS.satisfied("AAA","AAA"),false);
	    	assertEquals(ComparisonOperators.NOT_EQUALS.satisfied("AAA","B"),true);
			assertEquals(ComparisonOperators.EQUALS.satisfied("AAA","AAA"),true);
			assertEquals(ComparisonOperators.EQUALS.satisfied("AAA","B"),false);
			assertEquals(ComparisonOperators.LESS_OR_EQUALS.satisfied("BBB","AAA"),false);
			assertEquals(ComparisonOperators.LESS_OR_EQUALS.satisfied("AAA","B"),true);
			assertEquals(ComparisonOperators.GREATER.satisfied("G","AAA"),true);
			assertEquals(ComparisonOperators.GREATER.satisfied("AAA","B"),false);
			assertEquals(ComparisonOperators.GREATER_OR_EQUALS.satisfied("BBB","AAA"),true);
			assertEquals(ComparisonOperators.GREATER_OR_EQUALS.satisfied("B","B"),true);
			assertEquals(ComparisonOperators.LIKE.satisfied("BBBwecwe","B*"),true);
			assertEquals(ComparisonOperators.LIKE.satisfied("AaaaA","A*A"),true);
	}
	
	@Test
	public void testGetter() {
		StudentRecord stud;
		stud = db.forJMBAG("0000000019");
		assertEquals(FieldValueGetters.FIRST_NAME.get(stud),"Slaven");
		assertEquals(FieldValueGetters.LAST_NAME.get(stud),"Gvardijan");
		assertEquals(FieldValueGetters.JMBAG.get(stud),"0000000019");
	}
	
	@Test
	public void testC() {
		ConditionalExpression expr = new ConditionalExpression(
				 FieldValueGetters.LAST_NAME,
				 "Bos*",
				 ComparisonOperators.LIKE
				);
		StudentRecord stud;
		stud = db.forJMBAG("0000000019");
		StudentRecord stud2;
		stud2 = db.forJMBAG("0000000003");
		boolean recordSatisfies = expr.getComparisonOperator().satisfied(
		 expr.getFieldGetter().get(stud), // returns lastName from given record
		 expr.getStringLiteral() // returns "Bos*"
		);
		boolean recordSatisfies2 = expr.getComparisonOperator().satisfied(
				 expr.getFieldGetter().get(stud2), // returns lastName from given record
				 expr.getStringLiteral() // returns "Bos*"
				);
		assertEquals(recordSatisfies,false);
		assertEquals(recordSatisfies2,true);
	}
	
	@Test
	public void testParser() {
		QueryParser qp1 = new QueryParser(" jmbag =\"0123456789\" ");
		assertEquals(qp1.isDirectQuery(),true);
		assertEquals(qp1.getQueriedJMBAG(),"0123456789");
		assertEquals(qp1.getQuery().size(),1);
		QueryParser qp2 = new QueryParser("jmbag=\"0123456789\" and lastName>\"J\"");
		assertEquals(qp2.isDirectQuery(),false);
		assertThrows(Exception.class,() -> qp2.getQueriedJMBAG());
		assertEquals(qp2.getQuery().size(),2);
	}
	
	@Test
	public void testFilter() {
		QueryParser parser = new QueryParser(" jmbag =\"0000000003\" ");
		for(StudentRecord r : db.filter(new QueryFilter(parser.getQuery()))) {
			assertEquals(r.firstName,"Andrea");
			 }
	}
	
	
	
}
