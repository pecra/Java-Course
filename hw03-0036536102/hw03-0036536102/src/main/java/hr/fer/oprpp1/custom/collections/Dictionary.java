package hr.fer.oprpp1.custom.collections;

/**
 * Razred koji predstavlja rječnik koji se sastoji
 * od parametriziranih parova ključeva i njihovih vrijednosti.
 * @author Petra
 *
 */
public class Dictionary<K,V> {
	
	@SuppressWarnings("rawtypes")
	private ArrayIndexedCollection<Pair> arg = new ArrayIndexedCollection<>();
	
	private static class Pair<K,V>{
		private K key;
		private V value;
		
		/**
		 * Javni konstruktor koji postavlja
		 * ključ i vrijednost na vrijednosti predanih argumenata.
		 * @param key vrijednost ključa, ne smije biti null
		 * @param value vrijednost varijeble value
		 * @throws NullPointerException ako je predan null ključ
		 */
		public Pair(K key, V value) {
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
	 * Je li rječnik prazan.
	 * @return rječnik je prazan
	 */
	boolean isEmpty() {
		return this.arg.isEmpty();
	}
	
	/**
	 * Vraća veličinu rječnika.
	 * @return veličina
	 */
	int size() {
		return this.arg.size();
	}
	
	/**
	 * Prazni rječnik.
	 * Veličina rječnika ostaje ista.
	 */
	void clear() {
		this.arg.clear();
	}
	
	/**
	 * Dodaje novi par. Ako postoji par sa predanim
	 * ključem, mijenja vrijednost tog para.
	 * @return value null ako par nije postojao u rječniku,
	 * staru  vrijednost ako je postojao
	 */
	@SuppressWarnings("unchecked")
	V put(K key, V value) {
		if(this.get(key) != null) {
			int i = 0;
			for(int j = this.arg.size(); i < j; i++) {
				Pair<K,V> elem = (Pair<K,V>) this.arg.get(i);
				if(elem.getKey() == key) {
					break;
				}
			}
			Pair<K,V> p = (Pair<K,V>) this.arg.get(i);
			this.arg.remove(i);
			Pair<K,V> pair = new Pair<>(key, value);
			if(this.size() >= i) {
				this.arg.add(pair);
			}
			else {
				this.arg.insert(pair, i);
			}
			return p.getValue();
		}
		this.arg.add(new Pair<>(key, value));
		return null;
	} // "gazi" eventualni postojeći zapis
	
	/**
	 * Dohvaća vrijednost predanog ključa,
	 * null ako ne postoji.
	 * @param key ključa
	 * @return vrijednost za predani ključ
	 */
	@SuppressWarnings("unchecked")
	V get(K key) {
		for(int i = 0, j = this.arg.size(); i < j; i++) {
			Pair<K,V> elem = (Pair<K,V>) this.arg.get(i);
			if(elem.getKey() == key) {
				return elem.getValue();
			}
		}
		return null;
		
	} // ako ne postoji pripadni value, vraća null
	
	/**
	 * Uklanja par iz rječnika ako taj par postoji,
	 * vraća vrijednost value para kojem je ključ predani ključ.
	 * @param key predani ključ
	 * @return vrijednost value para kojem je ključ predani ključ
	 */
	@SuppressWarnings("unchecked")
	V remove(K key) {
		Pair<K,V> elem = null;
		if(this.get(key) != null) {
			int i = 0;
			for(int j = this.arg.size(); i < j; i++) {
				elem = (Pair<K,V>) this.arg.get(i);
				if(elem.getKey() == key) {
					break;
				}
			}
			Pair<K,V> p = (Pair<K,V>) this.arg.get(i);
			this.arg.remove(i);
			return p.getValue();
		}
		return null;
	}

}
