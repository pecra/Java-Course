package oprpp2.poruke;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class InmsgPoruka extends Poruka{

	private String text;
	private String ime;
	
	public String getIme() {
		return ime;
	}

	public String getText() {
		return text;
	}
	public InmsgPoruka(long rbr, String ime, String text) {
		super((byte)5);
		this.rbr = rbr;
		this.text = text;
		this.ime = ime;
	}
	
	public InmsgPoruka(byte[] polje) {
		super((byte)5);
           
		ByteBuffer buffer = ByteBuffer.wrap(Arrays.copyOfRange(polje, 1, 9));
	    this.rbr = buffer.getLong();
	    
	    buffer = ByteBuffer.wrap(Arrays.copyOfRange(polje, 9, 11));
	    short duljina1 = buffer.getShort();
	    
	    this.ime = new String(Arrays.copyOfRange(polje, 11, 11+duljina1));
	    this.text = new String(Arrays.copyOfRange(polje, 11+duljina1+2, polje.length));

	}
	
	@Override
	public byte[] okteti() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		try {
			dos.writeByte(this.kod);
			dos.writeLong(this.rbr);
			dos.writeUTF(this.ime);
			dos.writeUTF(this.text);
			dos.close();
		} catch(Exception e) {
			
		}
		byte[] buf = bos.toByteArray();
		return buf;
	}

}
