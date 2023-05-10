package hr.fer.oprpp1.custom.collections.demo;
import hr.fer.oprpp1.custom.collections.*;

/**
 * Klasa koja implementira tester.
 * @author Petra
 *
 */
public class EvenIntegerTester implements Tester {
	
	/**
	 * Implementacija metode test
	 * kojom se provjerava je li predani objekt parni broj;
	 * @return je li objekt paran broj
	 */
    public boolean test(Object obj) {
    if(!(obj instanceof Integer)) return false;
    Integer i = (Integer)obj;
    return i % 2 == 0;
    }

   public static void main(String[] args) {
   Tester t = new EvenIntegerTester();
   System.out.println(t.test("Ivo"));
   System.out.println(t.test(22));
   System.out.println(t.test(3));
   }
}
