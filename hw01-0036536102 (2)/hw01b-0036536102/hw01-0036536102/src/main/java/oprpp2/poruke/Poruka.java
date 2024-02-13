package oprpp2.poruke;

public abstract class Poruka {
	
	protected byte kod;
	public long rbr;
	
	public Poruka(byte key) {
		this.kod = key;
	}
	public abstract byte[] okteti();

}
