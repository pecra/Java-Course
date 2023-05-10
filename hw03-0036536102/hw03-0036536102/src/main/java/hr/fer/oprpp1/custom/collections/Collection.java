package hr.fer.oprpp1.custom.collections;


/** 
 * Sucelje predstavlja osnovu za izvođenje kolekcija. 
 * 
 * @author Petra Renic 
 */


public interface Collection <T>{

    /**
	 * Metoda za provjeru je li kolekcija prazna.
	 */
	public default boolean isEmpty() {
		if(this.size() == 0) {
			return true;
		} 
		return false;
    }
	/**
	 * Vraca broj objekata u kolekciji
	 * @return velicina
	 */	
	
	public abstract int size();

	/**
	 * Dodaje objekt u kolekciju
	 * @param value objekt koji dodajemo
	 */
	public abstract  void add(T value);
	/**
	 * Metodom equals provjerava sadrzi li kolekcija objekt
	 * @param value objekt za koji provjeravamo
	 * @return sadrzi li kolekcija objekt
	 */
	public abstract boolean contains(Object value);

	/**
	 * Metodom equals provjerava sadrzi li kolekcija objekt
	 * i uklanja jedan njegov primjerak
	 * @param value objekt koji uklanjamo
	 * @return sadrzi li kolekcija objekt
	 */
	public abstract boolean remove(Object value);

	/**
	 * Stvara novo polje velicine kolekcije, popunjeno objektima iz kolekcije
	 * @return novonastalo polje
	 */
	public abstract T[] toArray();

	/**
	 * Poziva processor.process(.) za sve elemente kolekcije.
	 * @param processor predani procesor
	 */
	public default void forEach(Processor<? super T> processor) {
		ElementsGetter<T> e = this.createElementsGetter();
		while(e.hasNextElement()) {
			T o = e.getNextElement();
			processor.process(o);
		}
	}

	/**
	 * U ovu kolekciju dodaje sve elemente other kolekcije
	 * @param other kolekcija cije elemente dodajemo
	 */
	public default void addAll(Collection<? extends T> other) {
		class AddProcessor implements Processor<T> {
            @Override
			public void process(T value) {
				Collection.this.add(value);
			}	
		}
		other.forEach(new AddProcessor());
	}

	/**
	 * Uklanja sve objekte iz kolekcije.
	 */
	public abstract void clear();
    
	/**
	 * Stvara novi objekt ElementsGetter za klasu iz koje je pozvan.
	 * @return referencu na novostvoreni objekt
	 */
	public abstract ElementsGetter<T> createElementsGetter();

	/**
	 * Metoda u kolekciju dodaje sve elemente iz predane kolekcije
	 * za koje tester javi da zadovoljavaju uvjet iz predanog testera.
	 * @param col predana kolekcija
	 * @param tester predani tester
	 */
	default void addAllSatisfying(Collection<? extends T> col, Tester<? super T> tester) {
		ElementsGetter<? extends T> e =  col.createElementsGetter();
		while(e.hasNextElement()) {
			T o = e.getNextElement();
			if(tester.test(o)) {
				this.add(o);
			}
		}
	}

}
