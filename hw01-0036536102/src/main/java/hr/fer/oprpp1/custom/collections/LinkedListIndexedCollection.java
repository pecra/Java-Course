package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;

/** 
 * Razred predstavlja kolekciju u obliku liste. 
 * 
 * @author Petra Renic 
 */
public class LinkedListIndexedCollection extends Collection {

	/**
	 * Prvi cvor, zadnji cvor i velicina liste.
	 */
	ListNode first;
	ListNode last;
	int size;
	
	/**
	 * Privatna klasa za definiciju svakog cvora.
	 */
	private static class ListNode {

		/**
	    * Prethodni cvor, slijedeci cvor i objekt vrijednost cvora.
	    */
		ListNode previous;
		ListNode next;
		Object value;
		
		/**
	    * Konstruktor koji stvara novi cvor.
	    * @param d vrijednost stvorenog cvora
	    */
		ListNode(Object d) 
        { 
            value = d; 
        } 
	}
	
	/**
	 * Konstruktor koji inicijalizira novu praznu listu.
	 */
	public LinkedListIndexedCollection() {
		this.first = this.last = null;
	}
	
	/**
	 * Konstruktor koji inicijalizira novu listu i puni ju objektima iz predane kolekcije.
	 * @param col predana kolekcija za punjenje liste
	 * @throws NullPointerException ako je predana null kolekcija
	 */
	public LinkedListIndexedCollection(Collection col) {
		if(col == null) {
			throw new NullPointerException();
		}
		this.addAll(col);	
	}

	@Override
	public boolean isEmpty() {
		return this.size == 0 ? true : false;
	}

	@Override
	public int size() {
		return this.size;
	}
    
	/**
	 * Dodaje objekt u listu
	 * @param value objekt koji dodajemo
	 * @throws NullPointerException ako je predan null objekt
	 */
	@Override
	public void add(Object value) {
		
		if(value == null) {
			throw new NullPointerException(); 
		}
		
		ListNode newNode = new ListNode(value);
		
		if(first == null) {
			first = newNode;
			first.previous = null;
			last = newNode;
			last.next = null;
			
		}
		else {
			last.next = newNode;
			newNode.previous = last;
			last = newNode;
			last.next = null;
		}
		this.size++;
	}

	@Override
	public boolean contains(Object value) {
		return !(this.indexOf(value) == -1);
	}
    
	/**
	 * Uklanja objekt sa zadane pozicije.
	 * @param index pozicija sa koje uklanjamo objekt
	 * @throws IndexOutOfBoundsException ako je index manji od 0
	 * ili veci od indeksa najveceg indeksiranog objekta u polju
	 */
	public void remove(int index) {
		if(index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		if(index == 0) {
			first = first.next;
			this.size--;
			return;
		}
		if(index == (this.size-1)) {
			last = last.previous;
			last.next = null;
			this.size--;
			return;
		}
		ListNode ptr = first;
		for(int i = 0; i < index; i++) {
			ptr = ptr.next;
		}
		ptr.next.previous = ptr.previous;
		ptr.previous.next = ptr.next;
		this.size--;
		return;
		
	}

	@Override
	public Object[] toArray() {
		if(this.size == 0) {
			super.toArray();
		}
		Object[] temp = new Object[this.size];
		ListNode curr = first;
		for(int i = 0, j = this.size;i < j; i++) {
			temp[i] = curr.value;
			curr = curr.next;
		}
		return temp;
		
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
	public void forEach(Processor processor) {
		ListNode tmp = first;
		while(tmp != null) {
			processor.process(tmp.value);
			tmp = tmp.next;
		}
	}

	@Override
	public void clear() {
		this.first = this.last = null;
		this.size = 0;
	}

	/**
	 * Dohvaca objekt na predanom indeksu.
	 * @param index index objekta koji dohvacamo
	 * @throws IndexOutOfBoundsException ako je index manji od 0
	 * ili veci od najveceg indeksiranog objekta
	 */
	public Object get(int index) {
		if(index < 0 || index >= this.size()) {
			throw new IndexOutOfBoundsException();
		}
		if(index < (this.size/2)) {
			int position = 0;
			ListNode curr = this.first;
			while(curr != null) {
				if(position == index) {
					return curr.value;
				}
				curr = curr.next;
				position++;
			}
		}
		else {
			int position = this.size;
			ListNode curr = this.last;
			while(curr != null) {
				if(position == index) {
					return curr.value;
				}
				curr = curr.previous;
				position--;
			}
		}
		return new Object();
	}
	
	/**
	 * Umece predani objekt na zadnu poziciju u listu.
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
		if(position == (this.size + 1)) {
			this.add((value));
			return;
		}
		
		ListNode newNode = new ListNode(value);
		ListNode curr = this.first;
		ListNode tmp = null;
		
		for(int i = 0; i < position - 1;i++) {
			curr = curr.next;
		}
		
		tmp = curr.next;
		tmp.previous = curr;
		curr.next = newNode;
		newNode.next = tmp;
		newNode.previous = curr;
		tmp.previous = newNode;
		this.size++;
		return;
	}
	
	/**
	 * Metodom equals trazi index prve pojave predanog objekta u listi.
	 * @param value objekt koji trazimo
	 * @return index trazenog objekta ako postoji u polju,
	 * -1 ako ne postoji
	 */
	public int indexOf(Object value) {
		if(first == null) {
			if(value == null) {
				return 0;
			}
			else {
				return -1;
			}
		}
		ListNode ptr = first;
		int position = 0;
		while(ptr != null) {
			if(ptr.value.equals(value)) {
				return position;
			}
			ptr = ptr.next;
			position++;
		}
		return -1;
	}
}	
