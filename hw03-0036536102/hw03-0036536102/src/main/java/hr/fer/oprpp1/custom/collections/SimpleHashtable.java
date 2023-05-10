package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/** 
 * Razred predstavlja rječnik koji se 
 * sastoji od parova(ključ, vrijednost). 
 * 
 * @author Petra Renic 
 */
public class SimpleHashtable<K,V> implements Iterable<SimpleHashtable.TableEntry<K,V>>{
	
	public TableEntry<K,V>[] table;
    int size = 0;
    int modificationCount = 0;

    
	static class TableEntry<K,V> {
		private K key;
		private V value;
		private TableEntry<K,V> next = null;
		
		/**
		 * Javni konstruktor koji postavlja
		 * ključ i vrijednost na vrijednosti predanih argumenata.
		 * @param key vrijednost ključa, ne smije biti null
		 * @param value vrijednost varijeble value
		 * @throws NullPointerException ako je predan null ključ
		 */
		public TableEntry(K key, V value) {
			if(key == null) {
				throw new NullPointerException();
			}
			this.key = key;
			this.value = value;
		}

		/**
		 * Vraća ključ od para.
		 * @return vrijednost ključa
		 */
		public K getKey() {
			return key;
		}

		/**
		 * Vraća vrijednost od para.
		 * @return vrijednost para
		 */
		public V getValue() {
			return value;
		}

		/**
		 * Postavlja vrijednost value od para.
		 */
		public void setValue(V value) {
			this.value = value;
		}	
	}
	
	/**
	 * Razred koji predstavlja implementaciju
	 * iteratora za rječnik.
	 * @author Petra
	 *
	 */
	private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K,V>> {
		
		private TableEntry<K,V> curr; 
		private int left;
		private int currSlot;
		private boolean callNext = false;
		private boolean firstElement = true;
		private int iteratorModCnt;
		
		/**
		 * Konstruktor.
		 */
		public IteratorImpl() {
			this.left = SimpleHashtable.this.size() - 1;
			if(left != 0) {
				for(int i = 0, j = SimpleHashtable.this.table.length; i < j; i++) {
					if(SimpleHashtable.this.table[i] != null) {
						currSlot = i;
						curr = SimpleHashtable.this.table[i];
						iteratorModCnt = SimpleHashtable.this.modificationCount;
						break;
					}
				}
			}
		}
		
		/**
		 * Postoji li u rječniku idući element.
		 * @throws ConcurrentModificationException ako je za vrijeme iteriranja
		 * došlo o uklanjanja ili dodavanja elementara u rječnik, a nije bilo od
		 * strane iteratora
		 */
		public boolean hasNext() {
			if(this.iteratorModCnt != SimpleHashtable.this.modificationCount) {
				throw new ConcurrentModificationException();
			}
			if(this.left < 1) {
				return false;
			}
			return true;
		}
		
		/**
		 * Prebacuje iterator na idući element.
		 * @return idući element
		 * @throws ConcurrentModificationException ako je za vrijeme iteriranja
		 * došlo o uklanjanja ili dodavanja elementara u rječnik, a nije bilo od
		 * strane iteratora
		 * @throws NoSuchElementException ako je kraj rječnika
		 */
		public SimpleHashtable.TableEntry next() {
			if(this.iteratorModCnt != SimpleHashtable.this.modificationCount) {
				throw new ConcurrentModificationException();
			}
			this.callNext = false;
			if(this.left < 1) {
				throw new NoSuchElementException("End of list.");
			}
			if(firstElement) {
				firstElement = false;
				return curr;
			}
			if(curr.next != null) {
				curr = curr.next;
			}
			else {
				for(int i = currSlot + 1, j = SimpleHashtable.this.table.length; i < j; i++) {
					if(SimpleHashtable.this.table[i] != null) {
						currSlot = i;
						curr = SimpleHashtable.this.table[i];
						break;
					}
				}
			}
			this.left--;
			return curr;
		}
		
		/**
		 * Uklanja element ne kojem je trenutno iterator.
		 * @throws IllegalStateException ako je element već uklonjen
		 * @throws ConcurrentModificationException ako je za vrijeme iteriranja
		 * došlo o uklanjanja ili dodavanja elementara u rječnik, a nije bilo od
		 * strane iteratora
		 */
		public void remove() { 
			if(this.callNext) {
				throw new IllegalStateException("Element has already been removed!");
			}
			
			if(this.iteratorModCnt != SimpleHashtable.this.modificationCount) {
				throw new ConcurrentModificationException();
			}
			
			K key2 = this.curr.getKey();
			SimpleHashtable.this.remove(key2);
			this.iteratorModCnt = SimpleHashtable.this.modificationCount;
			this.callNext = true;
		}
	}
	
	@SuppressWarnings("unchecked")
	public SimpleHashtable() {
		this.table = (TableEntry<K,V>[]) new TableEntry[16];
	}
	
	@SuppressWarnings("unchecked")
	public SimpleHashtable(int i) {
		if(i < 1) {
			throw new IllegalArgumentException();
		}
		double d = Double.valueOf(i);
		while(d % 1 == 0) {
			d = d / 2.0;
		}
		if(d > 1.0) {
			int j = 1;
			while(j < i) {
				j = j * 2;
			}
			i = j;
		}
		this.table = (TableEntry<K,V>[]) new TableEntry[i];
	}
	
	 /**
	 * Metoda za provjeru je li rječnik prazan.
	 */
	boolean isEmpty() {
		for(int i = this.size() - 1; i >= 0; i--) {
			if(this.table[i] != null) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Vraca broj parova u rječniku.
	 * @return velicina
	 */	
	int size() {
		return this.size;
	}
	
	/**
	 * Isprazni rječnik.
	 * Veličina rječnika ostaje ista.
	 */
	void clear() {
		for(int i = this.table.length - 1; i >= 0; i--) {
			this.table[i] = null;
		}
		this.size = 0;
		SimpleHashtable.this.modificationCount++;
	}
	
	/**
	 * Dodaje novi par. Ako postoji par sa predanim
	 * ključem, mijenja vrijednost tog para.
	 * @return value null ako par nije postojao u rječniku,
	 * staru  vrijednost ako je postojao
	 */
	V put(K key, V value) {
		if(key == null) {
			throw new NullPointerException();
		}
		if(this.popunjenost(0.75) && this.get(key) != null) {
			@SuppressWarnings("unchecked")
			TableEntry<K,V>[] tmpTable = (TableEntry<K,V>[]) new TableEntry[this.table.length * 2];
			TableEntry<K,V>[] tmpArray = this.toArray();
			this.table = tmpTable;
			int oldSize = this.size();
			this.size = 0;
			for(int i = 0; i < oldSize; i++) {
				this.put(tmpArray[i].getKey(), tmpArray[i].getValue());
			}
		}
		int slot = Math.abs(key.hashCode() % this.table.length);
		if(this.table[slot] == null) {
			this.table[slot] = new TableEntry<>(key,value);
			this.size++;
			SimpleHashtable.this.modificationCount++;
			return null;
		} 
		else {
			TableEntry<K,V> tmp = this.table[slot];
			while(tmp.next != null) {
				if(tmp.getKey().equals(key)) {
					V tmpVal = tmp.getValue(); 
					tmp.setValue(value);
					return tmpVal;
				}
				tmp = tmp.next;
			}
			tmp.next = new TableEntry<>(key,value);
			size++;
			SimpleHashtable.this.modificationCount++;
			return null;
		}
		
	} // "gazi" eventualni postojeći zapis
	
	/**
	 * Dohvaća vrijednost predanog ključa,
	 * null ako ne postoji.
	 * @param key ključa
	 * @return vrijednost za predani ključ
	 */
	V get(Object key) {
		int slot = Math.abs(key.hashCode() % this.table.length);
		if(this.table[slot] == null) {
			return null;
		} 
		else {
			TableEntry<K,V> tmp = this.table[slot];
			while(tmp != null) {
				if(tmp.getKey().equals(key)) {
					return tmp.getValue();
				}
				tmp = tmp.next;
			}
			return null;
		}
	} // ako ne postoji pripadni value, vraća null
	
	/**
	 * Uklanja par iz rječnika ako taj par postoji,
	 * vraća vrijednost value para kojem je ključ predani ključ.
	 * @param key predani ključ
	 * @return vrijednost value para kojem je ključ predani ključ
	 */
	V remove(K key) {
		int slot = Math.abs(key.hashCode() % this.table.length);
		if(this.table[slot] == null) {
			return null;
		} 
		else {
			TableEntry<K,V> tmp = this.table[slot];
			
			if(tmp.getKey().equals(key)) {
				this.table[slot] = tmp.next;
				this.size--;
				SimpleHashtable.this.modificationCount++;
				return tmp.getValue();
			}
			
			while(tmp.next != null) {
				if(tmp.next.getKey().equals(key)) {
					V tmpVal = tmp.next.getValue(); 
					tmp.next = tmp.next.next;
					this.size--;
					SimpleHashtable.this.modificationCount++;
					return tmpVal;
				}
				tmp = tmp.next;
			}
			return null;
		}
	}
	
	/**
	 * Sadrži li rječnik predani ključ.
	 */
	public boolean containsKey(Object key) {//ne koristimo metodu od gore jer ne znamo sto znaci null
		if(key == null) {
			return false;
		}
		int slot = Math.abs(key.hashCode() % this.table.length);
		if(this.table[slot] == null) {
			return false;
		} 
		else {
			TableEntry<K,V> tmp = this.table[slot];
			
			while(tmp != null) {
				if(tmp.getKey().equals(key)) {
					return true;
				}
				tmp = tmp.next;
			}
			return false;
		}
	}
	
	/**
	 * Sadrži li rječik predanu vrijednost.
	 * @param value
	 * @return
	 */
    public boolean containsValue(Object value) {
    	for(int i = 0, j = this.table.length; i < j; i++) {
    		if(this.table[i] != null) {
    			TableEntry<K,V> tmp = this.table[i];
    			
    			while(tmp != null) {
    				if(tmp.getValue().equals(value)) {
    					return true;
    				}
    				tmp = tmp.next;
    			}
    		} 
    	}
    	return false;
    }
    
    /**
     * Vraća rječnik u obliku stringa.
     */
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	StringBuilder sb2 = new StringBuilder();
    	
    	for(int i = 0, j = this.table.length; i < j; i++) {
    		
    		if(this.table[i] != null) {
                sb.append("[");
    			TableEntry<K,V> tmp = this.table[i];
    			while(tmp != null) {

    				sb.append(tmp.getKey().toString());
    				sb.append("=");
    				sb.append(tmp.getValue().toString());
    				sb.append(", ");
    				tmp = tmp.next;
    			}
    			sb.setLength(sb.length() - 2);
        		sb.append("]\n");
        		sb2.append(sb.toString());
        		sb.setLength(0);
    		}
    		
    		
    	}
    	
    	return sb2.toString();
    }
	
    /**
     * Pretvara rječnik u polje parova.
     * @return polje parova
     */
	public TableEntry<K,V>[] toArray() {
		@SuppressWarnings("unchecked")
		TableEntry<K,V>[] array = (TableEntry<K,V>[]) new TableEntry[this.size()];
		int k = 0;
		for(int i = 0, j = this.table.length; i < j; i++) {
    		if(this.table[i] != null) {
    			TableEntry<K,V> tmp = this.table[i];
    			while(tmp != null) {
    				array[k] = tmp;
    				tmp = tmp.next;
    				k++;
    			}
    		}
    	}
		return array;
	}
	
	/**
	 * Je li popunjenost slotova u rječniku veća od predane vrijednosti.
	 */
	private boolean popunjenost(double d) {
		return ((double) this.size())/((double) this.table.length) >= d;
	}
	
	/**
	 * Stvara novi iterator za ovaj rječnik.
	 */
	@Override
	public IteratorImpl iterator() {
		return new IteratorImpl();
	}
	
	
	
}
