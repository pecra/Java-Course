package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;

/** 
 * Razred predstavlja kolekciju u obliku liste. 
 * 
 * @author Petra Renic 
 */
public class LinkedListIndexedCollection<T> implements List<T> {

	/**
	 * Prvi cvor, zadnji cvor i velicina liste.
	 */
	ListNode<T> first;
	ListNode<T> last;
	int size;
	private static long modificationCount = 0;
	
	/**
	 * Privatna klasa za definiciju svakog cvora.
	 */
	private static class ListNode<K> {

		/**
	    * Prethodni cvor, slijedeci cvor i objekt vrijednost cvora.
	    */
		ListNode<K> previous;
		ListNode<K> next;
		K value;
		
		/**
	    * Konstruktor koji stvara novi cvor.
	    * @param d vrijednost stvorenog cvora
	    */
		ListNode(K d) 
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
	public LinkedListIndexedCollection(Collection<T> col) {
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
	public void add(T value) {
		
		if(value == null) {
			throw new NullPointerException(); 
		}
		
		ListNode<T> newNode = new ListNode<>(value);
		
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
		LinkedListIndexedCollection.modificationCount++;
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

		LinkedListIndexedCollection.modificationCount++;

		if(index == 0) {
			first = first.next;
			this.size--;
			return;
		}
		if(index == (this.size-1)) {
			last = last.previous;
			this.size--;
			return;
		}
		ListNode<T> ptr = first;
		for(int i = 0; i < index; i++) {
			ptr = ptr.next;
		}
		ptr.next.previous = ptr.previous;
		ptr.previous.next = ptr.next;
		
	}

	@Override
	public T[] toArray() {
		if(this.size == 0) {
			throw new UnsupportedOperationException();
		}
		@SuppressWarnings("unchecked")
		T[] temp = (T[]) new Object[this.size];
		ListNode<T> curr = first;
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
		LinkedListIndexedCollection.modificationCount++;
		this.remove(i);
		return true;
	}

	@Override
	public void clear() {
		this.first = this.last = null;
		this.size = 0;
		LinkedListIndexedCollection.modificationCount++;
	}

	/**
	 * Dohvaca objekt na predanom indeksu.
	 * @param index index objekta koji dohvacamo
	 * @throws IndexOutOfBoundsException ako je index manji od 0
	 * ili veci od najveceg indeksiranog objekta
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T get(int index) {
		if(index < 0 || index >= this.size()) {
			throw new IndexOutOfBoundsException();
		}
		if(index < (this.size/2)) {
			int position = 0;
			ListNode<T> curr = this.first;
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
			ListNode<T> curr = this.last;
			while(curr != null) {
				if(position == index) {
					return curr.value;
				}
				curr = curr.previous;
				position--;
			}
		}
		return (T) new Object();
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
	public void insert(T value, int position) {
		if(value == null) {
			throw new NullPointerException();
		}
		if(position < 0 || position > this.size) {
			throw new IndexOutOfBoundsException();
		}

		LinkedListIndexedCollection.modificationCount++;

		if(position == (this.size + 1)) {
			this.add((value));
			return;
		}
		
		ListNode<T> newNode = new ListNode<>(value);
		ListNode<T> curr = this.first;
		ListNode<T> tmp = null;
		
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
		ListNode<T> ptr = first;
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
	public ElementsGetter<T> createElementsGetter(){
        return new ElementsGetterList<>(this.first, LinkedListIndexedCollection.modificationCount);
	}

	private static class ElementsGetterList<T> implements ElementsGetter<T> {
        private ListNode<T> curr;
		private long savedModificationCount = 0;
		
		/**
	     * Konstruktor koji inicijalizira novi ElementsGetter
	     * za indeksiranu listu.
	     */
		private ElementsGetterList(ListNode<T> first, long cn) {
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
		public T getNextElement() {
			if(this.savedModificationCount != LinkedListIndexedCollection.modificationCount) {
				throw new ConcurrentModificationException();
			}
            if(!this.hasNextElement()) {
				throw new NullPointerException();
			}
			else {
			    T o = curr.value;
				curr = curr.next;  
				return o;
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
