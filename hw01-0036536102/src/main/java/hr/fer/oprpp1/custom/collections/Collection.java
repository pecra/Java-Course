package hr.fer.oprpp1.custom.collections;

/** 
 * Razred predstavlja osnovu za izvođenje kolekcija. 
 * 
 * @author Petra Renic 
 */


public class Collection {
	
	Object[] col;

    /**
	 * Metoda za provjeru je li kolekcija prazna
	 */
	public boolean isEmpty() {
		if(this.size() == 0) {
			return true;
		} 
		return false;

	/**
	 * Vraca broj objekata u kolekciji
	 * @return velicina
	 */	
	}
	public int size(){
		return 0;
	}

	/**
	 * Dodaje objekt u kolekciju
	 * @param value objekt koji dodajemo
	 */
	public void add(Object value) {
	}

	/**
	 * Metodom equals provjerava sadrzi li kolekcija objekt
	 * @param value objekt za koji provjeravamo
	 * @return sadrzi li kolekcija objekt
	 */
	public boolean contains(Object value) {
		return false;
	}

	/**
	 * Metodom equals provjerava sadrzi li kolekcija objekt
	 * i uklanja jedan njegov primjerak
	 * @param value objekt koji uklanjamo
	 * @return sadrzi li kolekcija objekt
	 */
	public boolean remove(Object value) {
		return false;
	}

	/**
	 * Stvara novo polje velicine kolekcije, popunjeno objektima iz kolekcije
	 * @return novonastalo polje
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Poziva processor.process(.) za sve elemente kolekcije
	 */
	public void forEach(Processor processor) {
		
	}

	/**
	 * U ovu kolekciju dodaje sve elemente other kolekcije
	 * @param other kolekcija cije elemente dodajemo
	 */
	public void addAll(Collection other) {
		class AddProcessor extends Processor {
            @Override
			public void process(Object value) {
				Collection.this.add(value);
			}	
		}
		other.forEach(new AddProcessor());
	}

	/**
	 * Uklanja sve objekte iz kolekcije
	 */
	public void clear() {
		
	}
}
