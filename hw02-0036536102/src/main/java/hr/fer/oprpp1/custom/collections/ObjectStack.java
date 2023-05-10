package hr.fer.oprpp1.custom.collections;

/** 
 * Razred predstavlja stog. 
 * 
 * @author Petra Renic 
 */

public class ObjectStack {
	
	private ArrayIndexedCollection arg;
	
	public ObjectStack() {
		this.arg = new ArrayIndexedCollection();
	}
	
	public boolean isEmpty() {
		return this.arg.isEmpty();
	}
	
	public int size() {
		return this.arg.size();
	}
	
	/**
	 * Dodaje objekt na vrh stoga.
	 * @param value objekt koji dodajemo
	 * @throws NullPointerException ako je predan null objekt
	 */
	public void push(Object value) {
		this.arg.add(value);
	}
	
	/**
	 * Skida objekt sa vrha stoga.
	 * @return skinuti objekt sa vrha stoga
	 * @throws EmptyStackException ako je stog prazan
	 */
	public Object pop() {
		if(this.arg.size() == 0) {
			throw new EmptyStackException();
		}
		Object o = this.arg.get(this.size()-1);
		this.arg.remove(this.size()-1);
		return o;
	}
	
	/**
	 * Vraca objekt koji se nalazi na vrhu stoga
	 * @return objekt koji se nalazi na vrhu stoga
	 * @throws EmptyStackException ako je stog prazan
	 */
	public Object peek() {
		if(this.arg.size() == 0) {
			throw new EmptyStackException();
		}
		Object o = this.arg.get(this.size()-1);
		return o;
	}
	
	public void clear() {
		this.arg.clear();
	}

}
