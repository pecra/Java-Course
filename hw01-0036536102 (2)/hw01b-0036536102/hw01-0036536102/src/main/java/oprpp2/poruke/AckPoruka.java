package oprpp2.poruke;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class AckPoruka extends Poruka{

	public long UID;
	
	public AckPoruka(long rbr, long UID) {
		super((byte)2);
		this.rbr = rbr;
		this.setUID(UID);
	}
	public AckPoruka(byte[] polje) {
		super((byte)2);
         
	    
		ByteBuffer buffer = ByteBuffer.wrap(Arrays.copyOfRange(polje, 1, 9));
	    this.rbr = buffer.getLong();
	    
	    buffer = ByteBuffer.wrap(Arrays.copyOfRange(polje, 9, polje.length));
	    this.setUID(buffer.getLong());

	}
	@Override
	public byte[] okteti() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeByte(this.kod);
			dos.writeLong(this.rbr);
			dos.writeLong(this.getUID());
			dos.close();
		} catch(Exception e) {
			
		}
		byte[] buf = bos.toByteArray();
		return buf;
	}
	public long getUID() {
		return UID;
	}
	public void setUID(long uID) {
		this.UID = uID;
	}

}