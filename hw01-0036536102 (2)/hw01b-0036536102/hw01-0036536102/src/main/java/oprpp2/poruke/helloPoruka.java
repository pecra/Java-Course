package oprpp2.poruke;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class helloPoruka extends Poruka{

	public String ime;
	public long randKey;
	
	public helloPoruka(long rbr, String ime, long randKey) {
		super((byte)1);
		this.rbr = rbr;
		this.ime = ime;
		this.randKey = randKey;
	}
	
	public helloPoruka(byte[] polje) {
		super((byte)1);
           
		ByteBuffer buffer = ByteBuffer.wrap(Arrays.copyOfRange(polje, 1, 10));
	    this.rbr = buffer.getLong();
	    
	    this.ime = new String(Arrays.copyOfRange(polje, 11, polje.length-8));
	    
	    buffer = ByteBuffer.wrap(Arrays.copyOfRange(polje, polje.length-8, polje.length));
	    this.randKey = buffer.getLong();

	}
	
	@Override
	public byte[] okteti() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeByte(this.kod);
			dos.writeLong(this.rbr);
			dos.writeUTF(this.ime);
			dos.writeLong(this.randKey);
			dos.close();
		} catch(Exception e) {
			
		}
		byte[] buf = bos.toByteArray();
		return buf;
	}

}
