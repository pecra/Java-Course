package oprpp2.poruke;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class OutmsgPoruka extends Poruka{

	public String text;
	public long UID;
	public long rbr;
	
	public OutmsgPoruka(long rbr, String text, long UID) {
		super((byte)4);
		this.text = text;
		this.UID = UID;
		this.rbr = rbr;
	}
	public OutmsgPoruka(byte[] polje) {
		super((byte)4);

		ByteBuffer buffer = ByteBuffer.wrap(Arrays.copyOfRange(polje, 1, 9));
	    this.rbr = buffer.getLong();
	    
	    buffer = ByteBuffer.wrap(Arrays.copyOfRange(polje, 9, 17));
	    this.UID = buffer.getLong();
	    
	    this.text = new String(Arrays.copyOfRange(polje, 19, polje.length));

	}
	
	@Override
	public byte[] okteti() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeByte(this.kod);
			dos.writeLong(this.rbr);
			dos.writeLong(this.UID);
			dos.writeUTF(this.text);
			dos.close();
		} catch(Exception e) {
			
		}
		byte[] buf = bos.toByteArray();
		return buf;
	}

}
