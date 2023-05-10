package hr.fer.oprpp1.hw05.crypto;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class Tester {
	@Test
	public void testArguments() {
		assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("01aE2"));
	}
	@Test
	public void testArguments2() {
		assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("01aE2["));
	}
	@Test
	public void testEmpty() {
		byte[] arr = new byte[1];
		arr[0] = Byte.valueOf("0");
		assertEquals(arr[0], Util.hextobyte("")[0]);
	}
	@Test
	public void test() {
		byte[] arr = new byte[] {1, -82, 34};
		assertEquals(arr[0], Util.hextobyte("01aE22")[0]);
		assertEquals(arr[1], Util.hextobyte("01aE22")[1]);
		assertEquals(arr[2], Util.hextobyte("01aE22")[2]);
	}
	@Test
	public void testEmpty2() {
		assertEquals("00", Util.bytetohex(new byte[] {0}));
	}
	@Test
	public void test2() {
		assertEquals("01ae22", Util.bytetohex( new byte[] {1, -82, 34}));
	}
	
}

