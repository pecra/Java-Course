package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;

/** 
 * Razred predstavlja kolekciju u obliku polja. 
 * 
 * @author Petra Renic 
 */

public class ArrayIndexedCollection extends Collection{

	/**
	 * Broj objekata u polju i polje.
	 */
	private int size;
	private Object[] elements;

	/**
	 * Konstruktor koji inicijalizira polje za 16 objekata.
	 */
	public ArrayIndexedCollection() {
		this.elements = new Object[16];
	}

	/**
	 * Konstruktor koji inicijalizira polje velicine parametra.
	 * @param initialCapacity velicina polja
	 * @throws IllegalArgumentException ako je zadana velicina manja od 1
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		if(initialCapacity < 1) {
			throw new IllegalArgumentException();
		}
		this.elements = new Object[initialCapacity];
	}
	
	/**
	 * Konstruktor koji inicijalizira polje u koje kopira 
	 * objekte iz neke druge kolekcije.
	 * @param other kolekcija koju kopiramo
	 * @throws NullPointerException ako je predana null kolekcija
	 */
	public ArrayIndexedCollection(Collection other) {
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
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		if(initialCapacity < 1) {
			throw new IllegalArgumentException();
		}
		if(other == null) {
			throw new NullPointerException();
		}
		initialCapacity = Math.max(other.size(), initialCapacity);
		this.elements = new Object[initialCapacity];
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
	public void add(Object value) {
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
		   Object[] temp = new Object[newSize];
		   for(int i = 0, j = oldSize; i < j; i++) {
		      temp[i] = this.elements[i];
		   }
		   temp[oldSize] = value;
           this.clear();
           this.elements = temp;
		   this.size++;
		}
		
		
	}
	
	@Override
	public void forEach(Processor processor) {
		for(int i = 0, j = this.size; i < j; i++) {
			processor.process(this.elements[i]);
		}
	}
   
	@Override
	public void clear() {
		for(int i = 0, j = this.size(); i < j; i++) {
			this.elements[i] = null;
		}
	}
	
	/**
	 * Dohvaca objekt na predanom indeksu.
	 * @param index index objekta koji dohvacamo
	 * @throws IndexOutOfBoundsException ako je index manji od 0
	 * ili veci od najveceg indeksiranog objekta
	 */
	public Object get(int index) {
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
	public void insert(Object value, int position) {
		if(value == null) {
			throw new NullPointerException();
		}
		if(position < 0 || position > this.size) {
			throw new IndexOutOfBoundsException();
		}
		if(this.elements[position] == null) {
			this.elements[position] = value;
		} 
		else {
			int oldSize = this.size();
			
			Object[] temp = new Object[this.size() + 1];
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
		}
	}

	/**
	 * Metodom equals trazi index prve pojave predanog objekta u polju.
	 * @param value objekt koji trazimo
	 * @return index trazenog objekta ako postoji u polju,
	 * -1 ako ne postoji
	 */
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
		return true;
	}
	@Override
	public Object[] toArray() {
		return this.elements;
	}
}
