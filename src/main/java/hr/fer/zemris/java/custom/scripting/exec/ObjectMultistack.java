package hr.fer.zemris.java.custom.scripting.exec;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class ObjectMultistack {
	
	public static class MultistackEntry{
		
		private ValueWrapper value;
		private MultistackEntry next;
		
		public MultistackEntry(ValueWrapper value,MultistackEntry next) {
			this.value = value;
			this.next = next;
		}
		
	}
	
	private Map<String,MultistackEntry> map = new LinkedHashMap<>();
	
	public void push(String keyName, ValueWrapper valueWrapper) {
		Objects.requireNonNull(keyName, "Ključ ne može biti null!");
        Objects.requireNonNull(valueWrapper, "Wrapper ne može biti null, ali null možeš umotati u wrappper!");
		
        MultistackEntry currHead = map.get(keyName);
        
		if(currHead == null) {
			map.put(keyName, new MultistackEntry(valueWrapper,null));
		}
		else {
			map.remove(keyName);
			map.put(keyName, new MultistackEntry(valueWrapper,currHead));
		}
	}
	
	public ValueWrapper pop(String keyName) {
		if(this.map.size() == 0) {
			throw new UnsupportedOperationException("Ne moze pop nad praznom mapom");
		}
		MultistackEntry currHead = map.get(keyName);
		map.remove(keyName);
		map.put(keyName, currHead.next);
		return currHead.value;
	}
	
	public ValueWrapper peek(String keyName) {
		if(this.map.size() == 0) {
			throw new UnsupportedOperationException("Ne moze pop nad praznom mapom");
		}
		return map.get(keyName).value;
	}
	
	public boolean isEmpty(String keyName) {
		return map.get(keyName) == null;
    }


}
