package oprpp2.poruke;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ByePoruka extends Poruka{

	public long UID;
	
	public ByePoruka(long rbr, long UID) {
		super((byte)3);
		this.rbr = rbr;
		this.UID = UID;
	}
	public ByePoruka(byte[] polje) {
		super((byte)3);
           
		ByteBuffer buffer = ByteBuffer.wrap(Arrays.copyOfRange(polje, 1, 9));
	    this.rbr = buffer.getLong();
	    
	    buffer = ByteBuffer.wrap(Arrays.copyOfRange(polje, 9, polje.length));
	    this.UID = buffer.getLong();

	}
	
	@Override
	public byte[] okteti() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeByte(this.kod);
			dos.writeLong(this.rbr);
			dos.writeLong(this.UID);
			dos.close();
		} catch(Exception e) {
			
		}
		byte[] buf = bos.toByteArray();
		return buf;
	}

}
