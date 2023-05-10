package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;


/** 
 * Razred predstavlja kolekciju u obliku polja. 
 * 
 * @author Petra Renic 
 */

public class ArrayIndexedCollection<T> implements List<T>{

	/**
	 * Broj objekata u polju i polje.
	 */
	private int size;
	private T[] elements;
	private static long modificationCount = 0;

	/**
	 * Konstruktor koji inicijalizira polje za 16 objekata.
	 */
	@SuppressWarnings("unchecked")
	public ArrayIndexedCollection() {
		this.elements = (T[]) new Object[16];
	}

	/**
	 * Konstruktor koji inicijalizira polje velicine parametra.
	 * @param initialCapacity velicina polja
	 * @throws IllegalArgumentException ako je zadana velicina manja od 1
	 */
	@SuppressWarnings("unchecked")
	public ArrayIndexedCollection(int initialCapacity) {
		if(initialCapacity < 1) {
			throw new IllegalArgumentException();
		}
		this.elements = (T[]) new Object[initialCapacity];
	}
	
	/**
	 * Konstruktor koji inicijalizira polje u koje kopira 
	 * objekte iz neke druge kolekcije.
	 * @param other kolekcija koju kopiramo
	 * @throws NullPointerException ako je predana null kolekcija
	 */
	public ArrayIndexedCollection(Collection<? extends T> other) {
		this(other.size());
		if(other == null) {
			throw new NullPointerException();
		}
		this.addAll(other);
	}

	/**
	 * Konstruktor koji inicijalizira polje zadane velicine
	 * u koje kopira objekte iz neke druge kolekcije.
	 * @param other kolekcija koju kopiramo
	 * @param initialCapacity velicina polja
	 * @throws NullPointerException ako je predana null kolekcija
	 * @throws IllegalArgumentException ako je zadana velicina manja od 1
	 */
	@SuppressWarnings("unchecked")
	public ArrayIndexedCollection(Collection<? extends T> other, int initialCapacity) {
		if(initialCapacity < 1) {
			throw new IllegalArgumentException();
		}
		if(other == null) {
			throw new NullPointerException();
		}
		initialCapacity = Math.max(other.size(), initialCapacity);
		this.elements = (T[]) new Object[initialCapacity];
		this.addAll(other);
	}

	@Override
	public int size() {
		return this.size;
	}

	/**
	 * Dodaje objekt u polje
	 * @param value objekt koji dodajemo
	 * @throws NullPointerException ako je predan null objekt
	 */
	@Override
	public void add(T value) {
		if(value == null) {
			throw new NullPointerException();
		}

		if(this.elements[this.elements.length - 1] == null) {
           for(int i = 0, j = this.elements.length; i < j; i++) {
			if(this.elements[i] == null) {
				this.elements[i] = value;
			    this.size++;
				break;
			}
		   }
		}
		else {
		   int oldSize = this.size;
    	   int newSize = oldSize * 2;
		   @SuppressWarnings("unchecked")
		T[] temp = (T[]) new Object[newSize];
		   for(int i = 0, j = oldSize; i < j; i++) {
		      temp[i] = this.elements[i];
		   }
		   temp[oldSize] = value;
           this.clear();
           this.elements = temp;
		   this.size++;
		   ArrayIndexedCollection.modificationCount++;
		}
		
		
	}
   
	@Override
	public void clear() {
		for(int i = 0, j = this.size(); i < j; i++) {
			this.elements[i] = null;
		}
		ArrayIndexedCollection.modificationCount++;
	}
	
	/**
	 * Dohvaca objekt na predanom indeksu.
	 * @param index index objekta koji dohvacamo
	 * @throws IndexOutOfBoundsException ako je index manji od 0
	 * ili veci od najveceg indeksiranog objekta
	 */
	@Override
	public T get(int index) {
		if(index < 0 || index > (this.size - 1)) {
			throw new IndexOutOfBoundsException();
		}
		return this.elements[index];
	}
	
	/**
	 * Umece predani objekt na zadnu poziciju u polje, 
	 * ne brise ostale objekte nego ih pomice za jedno mjesto ako je potrebno.
	 * @param value objekt koji umecemo
	 * @param position pozicija na koju umecemo novi objekt
	 * @throws IndexOutOfBoundsException ako je index manji od 0
	 * ili veci od velicine polja
	 * @throws NullPointerException ako je predan null objekt
	 */
	@Override
	public void insert(T value, int position) {
		if(value == null) {
			throw new NullPointerException();
		}
		if(position < 0 || position > this.size) {
			throw new IndexOutOfBoundsException();
		}
		if(this.elements[position] == null) {
			this.elements[position] = value;
			ArrayIndexedCollection.modificationCount++;
		} 
		else {
			int oldSize = this.size();
			
			@SuppressWarnings("unchecked")
			T[] temp = (T[]) new Object[this.size() + 1];
			for(int i = 0, j = position; i < j; i++) {
				temp[i] = this.elements[i];
			}
			temp[position] = value;
			for(int i = position + 1, j = oldSize + 1; i < j; i++) {
				temp[i] = this.elements[i - 1];
			}
	        this.clear();
			this.size = oldSize + 1;
	        this.elements = temp;
			ArrayIndexedCollection.modificationCount++;
		}
	}

	/**
	 * Metodom equals trazi index prve pojave predanog objekta u polju.
	 * @param value objekt koji trazimo
	 * @return index trazenog objekta ako postoji u polju,
	 * -1 ako ne postoji
	 */
	@Override
	public int indexOf(Object value) {
		if(value == null) {
			return -1;
		}
		for(int i = 0,j = this.size; i < j; i++) {
			if(this.elements[i].equals(value)) {
				return i;
			}	
		}
		return -1;
	}

	/**
	 * Uklanja objekt sa zadane pozicije.
	 * @param index pozicija sa koje uklanjamo objekt
	 * @throws IndexOutOfBoundsException ako je index manji od 0
	 * ili veci od indeksa najveceg indeksiranog objekta u polju
	 */
	@Override
	public void remove(int index) {
		if(index < 0 || index > (this.size - 1)) {
			throw new IndexOutOfBoundsException();
		}
		if(this.elements[index] != null) {
			for(int i = (this.size - 1),j = index; j < i; j++) {
				this.elements[j] = this.elements[j + 1];
			}
			this.elements[this.size - 1] = null;
		}
		ArrayIndexedCollection.modificationCount++;
		this.size--;
	}
	@Override
	public boolean isEmpty() {
		return (this.size == 0) ? true : false;
	}
	@Override
	public boolean contains(Object value) {
		return !(this.indexOf(value) == -1);
	}
	@Override
	public boolean remove(Object value) {
		int i = this.indexOf(value);
		if(i == -1) {
			return false;
		}
		this.remove(i);
		ArrayIndexedCollection.modificationCount++;
		return true;
	}
	@Override
	public T[] toArray() {
		return this.elements;
	}

	@Override
	public ElementsGetter<T> createElementsGetter(){
        return new ElementsGetterArray<>(this.size(),this.elements, ArrayIndexedCollection.modificationCount);
	}

	private static class ElementsGetterArray<T> implements ElementsGetter<T> {
        private int counter;
		private int sizeS;
		private T[] objs;
		private long savedModificationCount;

		/**
	     * Konstruktor koji inicijalizira novi ElementsGetter
	     * za indeksirano polje.
	     */
		public ElementsGetterArray(int s, T[] e, long cn) {
			this.counter = 0;
			this.sizeS = s;
            this.objs = e;
			this.savedModificationCount = Long.valueOf(cn);
		}
		
		/**
	     * Postoji li u kolekciji element iza trenutnog.
	     * @return postoji li u kolekciji slijedeci element,
	     * ispitujemo za isti element sve dok se ne pozove
	     * hasNextElement()
	     * @throws ConcurrentModificationException() ako 
	     * su radene izmjene nad poljem izmedu 2 poziva metoda ElementsGettera
	     */
		@Override
		public boolean hasNextElement() {
			if(this.savedModificationCount != ArrayIndexedCollection.modificationCount) {
				throw new ConcurrentModificationException();
			}
			if(this.counter > (this.sizeS - 1)){
				return false;
			}
			else {
				return true;
			}
		}
        
		/**
	     * Dohvaca iduci element u polju ako on postoji.
	     * @return slijedeci element u polju
	     * @throws ConcurrentModificationException() ako 
	     * su radene izmjene nad poljem izmedu 2 poziva metoda ElementsGettera
	     * @throws NullPointerException() ako 
	     * ne postoji iduci element
	     */
		@Override
		public T getNextElement() {
			if(this.savedModificationCount != ArrayIndexedCollection.modificationCount) {
				throw new ConcurrentModificationException();
			}
            if(!this.hasNextElement()) {
				throw new NullPointerException();
			}
			else {
				int old = this.counter;
				this.counter = this.counter + 1;
                return this.objs[old];  
			}
		}

		@Override
		public void processRemaining(Processor<T> p) {
			while(this.hasNextElement()) {
				p.process(this.getNextElement());
			}
		}
	}
}
