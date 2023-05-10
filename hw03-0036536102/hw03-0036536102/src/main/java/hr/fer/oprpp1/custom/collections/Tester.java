package hr.fer.oprpp1.custom.collections;

/**
 * Sucelje za modeliranje objekata koji ispituju
 * je li primljeni objekt prihvatljiv.
 * @author Petra
 *
 */
public interface Tester<T> {
	
	/**
	 * Metoda koja ispituje je li primljeni
	 * objekt prihvatljiv.
	 * @return je li objekt prihvatljiv
	 */
    boolean test(T obj);
}
