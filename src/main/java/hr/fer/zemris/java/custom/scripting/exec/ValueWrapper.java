package hr.fer.zemris.java.custom.scripting.exec;

import java.util.function.BiFunction;

public class ValueWrapper {
	
	Object value;
	
	public ValueWrapper(Object value) {
		super();
		this.value = value;
	}
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	private boolean areAllowed(Object v1, Object v2) {
		if(!(v1 == null || v1 instanceof Integer || v1 instanceof Double || v1 instanceof String)) {
			throw new RuntimeException("Can't call this operation on those arguments.");
		}
		if(!(v2 == null || v2 instanceof Integer || v2 instanceof Double || v2 instanceof String)) {
			throw new RuntimeException("Can't call this operation on those arguments.");
		}
		return true;
	}
	
	private void operate(Object value2, Object incValue, BiFunction<Double,Double,Double> fu) {
		if(isDouble(value2) || isDouble(incValue)) {
			this.value = fu.apply(doubleIt(value2),doubleIt(incValue));
		}
		else {
			this.value =Integer.valueOf(fu.apply(intIt(value2).doubleValue(),intIt(incValue).doubleValue()).intValue());
		}
	}
	
	private Integer intIt(Object value2) {
		if(value2 == null) {
			return Integer.valueOf(0);
		}
		if(value2 instanceof String) {
			try {
				String s = value2.toString();
				return Integer.valueOf(s);
			} catch(NumberFormatException e) {
				throw new RuntimeException("Can't cast.");
			}
		}
		return (Integer)value2;
	}
	
	private boolean isDouble(Object value2) {
		if(value2 instanceof Double) {
			return true;
		}
		if(value2 instanceof String) {
			String s = value2.toString();
			if(s.contains(".") || s.contains("E")) {
				return true;
			}
			return false;
		}
		return false;
	}
	private Double doubleIt(Object value2) {
		if(value2 == null) {
			return Double.valueOf(0);
		}
		if(value2 instanceof Integer) {
			return ((Integer) value2).doubleValue();
		}
		if(value2 instanceof String) {
			try {
				return Double.valueOf(value2.toString());
			} catch(Exception e) {
				throw new  RuntimeException("Can't cast to double.");
			}
		}
		return (Double) value2;
	}
	public void add(Object incValue) {
		if(areAllowed(this.value,incValue)) {
			operate(this.value,incValue,(a,b)->a+b);
		}
	}
	public void subtract(Object decValue){
		if(areAllowed(this.value,decValue)) {
			operate(this.value,decValue,(a,b)->a-b);
		}
	}
	public void multiply(Object mulValue){
		if(areAllowed(this.value,mulValue)) {
			operate(this.value,mulValue,(a,b)->a*b);
		}
	}
	public void divide(Object divValue){
		if(areAllowed(this.value,divValue)) {
			operate(this.value,divValue,(a,b)->a/b);
		}
	}
	
	public int numCompare(Object withValue) {
		Object o = this.value;
		if(areAllowed(o,withValue)) {
			if(isDouble(o) || isDouble(withValue)) {
				return doubleIt(o).compareTo(doubleIt(withValue));
			}
			else {
				return intIt(o).compareTo(intIt(withValue));
			}
		}
		return 0;
	}

	
	
	

}
