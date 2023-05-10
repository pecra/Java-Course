package hr.fer.oprpp1.custom.collections;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CollectionTest {
	ArrayIndexedCollection array = new ArrayIndexedCollection();
	@Test
	public void testArrayInitialCapacity() {
		assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection(0));
	}
	@Test
	public void testArrayNullConstructor() {
		assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null));
	}
	@Test
	public void testArrayInitialCapacityCopy() {
		assertThrows(IllegalArgumentException.class, () -> new ArrayIndexedCollection(new Collection(),0));
	}
	@Test
	public void testArrayNullConstructorInit() {
		assertThrows(NullPointerException.class, () -> new ArrayIndexedCollection(null,1));
	}
	
	@Test
	public void testArrayAddNull() {
		assertThrows(NullPointerException.class, () -> array.add(null));
	}
	
	@Test
	public void testArrayGetOutOfBound() {
		assertThrows(IndexOutOfBoundsException.class, () -> array.get(-1));
	}
	@Test
	public void testArrayGetOutOfBoundUpper() {
		assertThrows(IndexOutOfBoundsException.class, () -> array.get(array.size()+1));
	}
	@Test
	public void testArrayInsertOutOfBound() {
		assertThrows(IndexOutOfBoundsException.class, () -> array.insert(new Object(),-1));
	}
	@Test
	public void testArrayInsertOutOfBound2() {
		assertThrows(IndexOutOfBoundsException.class, () -> array.insert(new Object(),(array.size()+5)));
	}
	
	@Test
	public void testArrayRemoveOutOfBound() {
		assertThrows(IndexOutOfBoundsException.class, () -> array.remove(-1));
	}
	@Test
	public void testArrayRemoveOutOfBoundUpper() {
		assertThrows(IndexOutOfBoundsException.class, () -> array.remove(array.size()));
	}
	
	LinkedListIndexedCollection linkedList = new LinkedListIndexedCollection();

	@Test
	public void testListNullConstructor() {
		assertThrows(NullPointerException.class, () -> new LinkedListIndexedCollection(null));
	}
	@Test
	public void testListAddNull() {
		assertThrows(NullPointerException.class, () -> linkedList.add(null));
	}
	@Test
	public void testListGetOutOfBound() {
		assertThrows(IndexOutOfBoundsException.class, () -> linkedList.get(-1));
	}
	@Test
	public void testListGetOutOfBoundUpper() {
		assertThrows(IndexOutOfBoundsException.class, () -> linkedList.get(linkedList.size() + 1));
	}
	@Test
	public void testListInsertOutOfBound() {
		assertThrows(IndexOutOfBoundsException.class, () -> linkedList.insert(new Object(),-1));
	}
	@Test
	public void testListInsertOutOfBoundUpper() {
		assertThrows(IndexOutOfBoundsException.class, () -> linkedList.insert(new Object(),(linkedList.size() + 1)));
	}
	
	@Test
	public void testListRemoveOutOfBound() {
		assertThrows(IndexOutOfBoundsException.class, () -> linkedList.remove(-1));
	}
	@Test
	public void testListRemoveOutOfBoundUpper() {
		assertThrows(IndexOutOfBoundsException.class, () -> linkedList.remove(linkedList.size()));
	}
	
}
