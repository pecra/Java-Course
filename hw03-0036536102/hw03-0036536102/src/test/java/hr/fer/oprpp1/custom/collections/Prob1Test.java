package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Prob1Test {

	Dictionary<Integer,String> dict = new Dictionary<>();
	
	//@Disabled
	@Test
	public void testNullKey() {
		assertThrows(NullPointerException.class, () -> dict.put(null,null));
	}
	//@Disabled
	@Test
	public void testNullDictLength() {
		assertEquals(dict.size(), 0);
	}
	//@Disabled
	@Test
	public void testEmptyDict() {
		assertEquals(dict.isEmpty(), true);
	}

	//@Disabled
	@Test
	public void testPut() {
		dict.put(5, "Lopta");
		assertEquals("Lopta", dict.get(5));
	}

	//@Disabled
	@Test
	public void testGet() {
		dict.put(5, "Lopta");
		dict.put(2, "Auto");
		assertEquals("Lopta", dict.get(5));
	}
	
	//@Disabled
	@Test
	public void testSize() {
		dict.put(2, "Auto");
		dict.put(2, "Autto");
		assertEquals(1, dict.size());
	}
	//@Disabled
	@Test
	public void testGet3() {
		dict.put(5, "Lopta");
		dict.put(2, "Auto");
		String s = dict.put(2, "Pas");
		
		assertEquals("Pas", dict.get(2));
		assertEquals("Auto", s);
	}
	//@Disabled
	@Test
	public void testGet4() {
		dict.put(5, "Lopta");
		dict.put(2, "Auto");
		String s = dict.remove(5);
		assertEquals(1, dict.size());
		assertEquals("Lopta", s);
	}
	
	SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
	SimpleHashtable<String, Integer> examMarks2 = new SimpleHashtable<>(2);
	
	//@Disabled
			@Test
			public void testNull() {
				assertThrows(NullPointerException.class, () -> examMarks.put(null, 2));

			}
	//@Disabled
		@Test
		public void testOverwrite() {
			examMarks.put("Ivana", 2);
			examMarks.put("Ante", 2);
			examMarks.put("Jasna", 2);
			examMarks.put("Kristina", 5);
			examMarks.put("Ivana", 5); // overwrites old grade for Ivana
			assertEquals(4, examMarks.size());
			assertEquals(5, examMarks.get("Kristina"));
		}
		
	//@Disabled
	@Test
	public void testClear() {
		examMarks.clear();
		assertEquals(0, examMarks.size());
	}	
	
	//@Disabled
	@Test
	public void testIllegalState() {
        examMarks2.put("Ivana", 2);
		examMarks2.put("Ante", 2);
		examMarks2.put("Jasna", 2);
		examMarks2.put("Kristina", 5);
		examMarks2.put("Ivana", 5);
		Iterator<SimpleHashtable.TableEntry<String,Integer>> iter2 = examMarks2.iterator();
		while(iter2.hasNext()) {
		SimpleHashtable.TableEntry<String,Integer> pair = iter2.next();
		System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
		iter2.remove();
		assertThrows(IllegalStateException.class,
				() -> iter2.remove());
		}
		
		
	}
	//@Disabled
		@Test
		public void testModificationExcept() {
			examMarks.put("Ivana", 2);
			examMarks.put("Ante", 2);
			examMarks.put("Jasna", 2);
			examMarks.put("Kristina", 5);
			examMarks.put("Ivana", 5); // overwrites old grade for Ivana
			Iterator<SimpleHashtable.TableEntry<String,Integer>> iter = examMarks.iterator();
			examMarks.remove("Ante");
			assertThrows(ConcurrentModificationException.class,
					() -> iter.remove());
			
		}
	
	
}