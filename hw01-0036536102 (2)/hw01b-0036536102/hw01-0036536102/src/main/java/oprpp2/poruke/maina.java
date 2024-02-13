package oprpp2.poruke;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class maina {

	
	
	public static void main(String[] args) {
		Poruka p = new InmsgPoruka((long)5,"ABCD","bla");
		ByteBuffer buffer = ByteBuffer.wrap(Arrays.copyOfRange(p.okteti(), 9, 11));
	    short rbr = buffer.getShort();
		System.out.println(rbr);
		for(byte b : p.okteti()) {

			System.out.println(b);
		}
		Poruka d = new InmsgPoruka(p.okteti());
		Poruka a = new helloPoruka((long)5,"ABCD",(long)77);
		helloPoruka c = new helloPoruka(a.okteti());
	//	System.out.println(c.ime);
	}
}
