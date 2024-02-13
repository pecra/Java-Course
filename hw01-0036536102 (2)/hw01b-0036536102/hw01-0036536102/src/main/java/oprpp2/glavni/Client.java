package oprpp2.glavni;

import java.net.InetAddress;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import oprpp2.poruke.AckPoruka;
import oprpp2.poruke.Poruka;

public class Client {
	
	InetAddress IP;
	int port;
	String ime;
	long randKey;
	long UID;
	long zadnjiRbr;
	LinkedBlockingQueue<Poruka> zaSlanje;
	Queue<Long> potvrde;
	Thread dretva;
	volatile boolean ugasi = false;
	
	public Client(InetAddress IP,int port,String ime, long randKey, long uID) {
		super();
		this.ime = ime;
		this.randKey = randKey;
		UID = uID;
		this.zaSlanje = new LinkedBlockingQueue<>();
		this.potvrde = new LinkedList<>();
		this.IP = IP;
		this.port = port;
		this.zadnjiRbr = 0;
	}
	public InetAddress getIP() {
		return this.IP;
	}
	
	public long povecajRbrIVrati() {
		long g;
		synchronized(this) {
			this.zadnjiRbr++;
			g = this.zadnjiRbr;
		}
		return g;
	}
	public Poruka iducaZaSlanje() {
		Poruka p = zaSlanje.poll();
		return p;
	}
	
	public long iducaPotvrda() {
		return potvrde.remove();
	}
	
	
	public void dodajPotvrdu(Poruka p) {
		potvrde.add(p.rbr);
		//this.povecajRbrIVrati(); //prilikom slanja ili primanja poruke se povecava za 1
	}
	public boolean sadrziPotvrdu(long rbr) {
		return this.potvrde.contains(rbr);
	}
	
	

}
