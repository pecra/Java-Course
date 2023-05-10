package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;

/** 
 * Razred predstavlja kolekciju u obliku liste. 
 * 
 * @author Petra Renic 
 */
public class LinkedListIndexedCollection implements List {

	/**
	 * Prvi cvor, zadnji cvor i velicina liste.
	 */
	ListNode first;
	ListNode last;
	int size;
	private static long modificationCount = 0;
	
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
		this.modificationCount++;
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
	@Override
	public void remove(int index) {
		if(index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}

		this.modificationCount++;

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
			throw new UnsupportedOperationException();
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
		this.modificationCount++;
		this.remove(i);
		return true;
	}

	@Override
	public void clear() {
		this.first = this.last = null;
		this.size = 0;
		this.modificationCount++;
	}

	/**
	 * Dohvaca objekt na predanom indeksu.
	 * @param index index objekta koji dohvacamo
	 * @throws IndexOutOfBoundsException ako je index manji od 0
	 * ili veci od najveceg indeksiranog objekta
	 */
	@Override
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
	@Override
	public void insert(Object value, int position) {
		if(value == null) {
			throw new NullPointerException();
		}
		if(position < 0 || position > this.size) {
			throw new IndexOutOfBoundsException();
		}

		this.modificationCount++;

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
	@Override
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

	@Override
	public ElementsGetter createElementsGetter(){
        return new ElementsGetterList(this.first, this.modificationCount);
	}

	private static class ElementsGetterList implements ElementsGetter {
        private ListNode curr;
		private long savedModificationCount = 0;
		
		/**
	     * Konstruktor koji inicijalizira novi ElementsGetter
	     * za indeksiranu listu.
	     */
		private ElementsGetterList(ListNode first, long cn) {
			this.curr = first;
			this.savedModificationCount = Long.valueOf(cn);
		}
		
		/**
	     * Postoji li u kolekciji element iza trenutnog.
	     * @return postoji li u kolekciji slijedeci element,
	     * ispitujemo za isti element sve dok se ne pozove
	     * hasNextElement()
	     * @throws ConcurrentModificationException() ako 
	     * su radene izmjene nad listom izmedu 2 poziva metoda ElementsGettera
	     */
		@Override
		public boolean hasNextElement() {
			if(this.savedModificationCount != LinkedListIndexedCollection.modificationCount) {
				throw new ConcurrentModificationException();
			}
			return !(this.curr== null);
		}
        
		/**
	     * Dohvaca iduci element u listi ako on postoji.
	     * @return slijedeci element u listi
	     * @throws ConcurrentModificationException() ako 
	     * su radene izmjene nad listom izmedu 2 poziva metoda ElementsGettera
	     * @throws NullPointerException() ako 
	     * ne postoji iduci element
	     */
		@Override
		public Object getNextElement() {
			if(this.savedModificationCount != LinkedListIndexedCollection.modificationCount) {
				throw new ConcurrentModificationException();
			}
            if(!this.hasNextElement()) {
				throw new NullPointerException();
			}
			else {
			    Object o = curr.value;
				curr = curr.next;  
				return o;
			}
		}

		@Override
		public void processRemaining(Processor p) {
			while(this.hasNextElement()) {
				p.process(this.getNextElement());
			}
		}
	}
}	
