package oprpp2.glavni;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import oprpp2.poruke.AckPoruka;
import oprpp2.poruke.ByePoruka;
import oprpp2.poruke.InmsgPoruka;
import oprpp2.poruke.OutmsgPoruka;
import oprpp2.poruke.Poruka;
import oprpp2.poruke.helloPoruka;


public class Posluzitelj {
	
	private AtomicLong osnovniUID = new AtomicLong((long)new Random().nextInt() & 0xFFFFFFFFL);
	private Map<Long,Client> klijenti = new HashMap<>();
	private Map<Long,Long> uidrandkey = new HashMap<>(); //rand:uid
	
	public Client dohvatiKlijenta(long UID) {
		return klijenti.get((Long)UID);
	}
	
	public long UIDzaKey(long randKey) {
		return uidrandkey.get((Long) randKey);
	}
	
	public void ukloniKlijenta(long UID) {
		Client c;
		synchronized (this.klijenti) {
			c = klijenti.remove((Long)UID);
        }
		synchronized(this.uidrandkey) {
			uidrandkey.remove(c.randKey);
		}
	}
	
	public void broadcastaj(OutmsgPoruka p4) {
		Client posiljatelj = this.dohvatiKlijenta(p4.UID);
		String ime = posiljatelj.ime;
		synchronized(this.klijenti) {
			for(Map.Entry<Long,Client> entry : klijenti.entrySet()) {
				//if(entry.getKey() == UIDposiljatelja) {
			//		continue;
				//}
				Client c = entry.getValue();
				LinkedBlockingQueue<Poruka> zaSlan = c.zaSlanje;
				long r;
			//	synchronized(c) {
			//		r = c.zadnjiRbr;
			//	}
				InmsgPoruka porr = new InmsgPoruka(c.povecajRbrIVrati(), ime, p4.text);
				zaSlan.add(porr);
				
			}
		}
	}
	
    
	public boolean dodajKlijenta(InetAddress IP,int port,long randKey,String ime) {
		
		long UID = 0;
		
		if(uidrandkey.containsKey((Long) randKey)){
			
			 UID = uidrandkey.get((Long) randKey);
			
			if(this.dohvatiKlijenta(UID).getIP().equals(IP)) {
				if(this.dohvatiKlijenta(UID).port == port) {
					return false;
				}
			}
		}
		UID = this.dajUID();
		uidrandkey.put((Long)randKey,(Long) UID);
		klijenti.put((Long) UID, new Client(IP, port, ime, randKey,(Long) UID ));
		return true;
	}
	
	public long dajUID() {
		return osnovniUID.incrementAndGet();
	}

	
	public static void main(String[] args) throws SocketException {
		if(args.length!=1) {
			System.out.println("Ocekivao sam port");
			return;
		}
		
		Posluzitelj ja = new Posluzitelj();
		int port = Integer.parseInt(args[0]);

		// Stvori pristupnu toÄŤku posluĹľitelja:
		@SuppressWarnings("resource")
		DatagramSocket dSocket = new DatagramSocket(null);
		dSocket.bind(new InetSocketAddress((InetAddress)null, port));
		System.out.println("spojen na portu "+port);
		while(true) {
			// Pripremi prijemni spremnik:
			byte[] recvBuffer = new byte[1024];
			DatagramPacket recvPacket = new DatagramPacket(
				recvBuffer, recvBuffer.length
			);
			
			// ÄŚekaj na primitak upita:
			try {
				dSocket.receive(recvPacket);
			} catch (IOException e) {
				continue;
			}
			
			// Otkomentirati ako treba odglumiti gubitak u komunikaciji:
			// if(Math.random() < 0.5) continue;

			// Izracunaj koji je tip poruke:
		    short kodPoruke = recvBuffer[0];
		    
		    byte[] slice = trim(recvBuffer);
		    InetAddress clientAddress = recvPacket.getAddress();
            int clientPort = recvPacket.getPort();
		  
            System.out.println("nova poruka");
		    switch(kodPoruke){
		    case(1):
		    	System.out.println("zahtjev za spajanjem");
		    	helloPoruka p1 = new helloPoruka(slice);
		        synchronized(ja.klijenti) {
		        	boolean nastavi = ja.dodajKlijenta(clientAddress,clientPort,p1.randKey,p1.ime);
		        	if(nastavi) {
		        		long UID = ja.UIDzaKey(p1.randKey);
		        		Client c = ja.dohvatiKlijenta(UID);
		        		(c.dretva = new Thread(()->ja.salji(c,dSocket))).start();
		        	}
		        }
		    	long UID = ja.UIDzaKey(p1.randKey);
		    	byte[] sendBuffer = new AckPoruka(0L,UID).okteti();
				// Pripremi odlazni paket:
				DatagramPacket sendPacket = new DatagramPacket(
						sendBuffer, sendBuffer.length
				);
				Client c = ja.dohvatiKlijenta(UID);
				sendPacket.setAddress(c.IP);
				sendPacket.setPort(c.port);
				// Posalji ga:
				try {
					dSocket.send(sendPacket);
					System.out.println("Poslan odg.");
				} catch (IOException e) {
					System.out.println("Greska pri slanju odgovora.");
				}
				break;
		    case(2):
		    	System.out.println("Ack poruka.");
		    	AckPoruka p2 = new AckPoruka(slice);
		        Client c2 = ja.dohvatiKlijenta(p2.UID);
		        c2.dodajPotvrdu(p2);
		    //    c2.povecajRbrIVrati();
		        break;
		    case(3):
		    	System.out.println("bye poruke");
		    	ByePoruka p3 = new ByePoruka(slice);
		        Client c5 = ja.dohvatiKlijenta(p3.UID);
		        byte[] sendBuffer2 = new AckPoruka(p3.rbr,p3.UID).okteti();
		    	// Pripremi odlazni paket:
		    	DatagramPacket sendPacket2 = new DatagramPacket(
		    			sendBuffer2, sendBuffer2.length
		    	);
		    	sendPacket2.setAddress(c5.IP);
		    	sendPacket2.setPort(c5.port);
		    	try {
		    		dSocket.send(sendPacket2);
		    	} catch (IOException e) {
		    		System.out.println("Greska pri slanju odgovora.");
		    	}
                c5.ugasi = true;
	            ja.ukloniKlijenta(p3.UID);
	            //ugasi dretvu++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	            break;
		    case(4):
		    	OutmsgPoruka p4 = new OutmsgPoruka(slice);
                ja.broadcastaj(p4);
                byte[] sendBuffer3 = new AckPoruka(p4.rbr,p4.UID).okteti();
				// Pripremi odlazni paket:
				DatagramPacket sendPacket3 = new DatagramPacket(
						sendBuffer3, sendBuffer3.length
				);
				sendPacket3.setSocketAddress(recvPacket.getSocketAddress());
				// Posalji ga:
				try {
					dSocket.send(sendPacket3);
				} catch (IOException e) {
					System.out.println("Greska pri slanju odgovora.");
				}
				break;
		    }

			System.out.printf("Upit od %s", recvPacket.getSocketAddress());
			System.out.println();

		}
	}
	
	private void salji(Client klijent,DatagramSocket dSocket) {
		InmsgPoruka p;
		boolean p2;
        do {
        	try {
		    
				p = (InmsgPoruka) klijent.zaSlanje.take();
			
		    byte[] message = p.okteti();
		    
		    DatagramPacket pack = new DatagramPacket(message, message.length);
		    pack.setAddress(klijent.IP);
		    pack.setPort(klijent.port);
		 //   try {
		//		dSocket.setSoTimeout(5000);
		//	} catch (SocketException e) {
				// TODO Auto-generated catch block
		//		e.printStackTrace();
		//	}
		    int n = 0;
		    while (true) {
		        ++n;
		        try {
		        	System.out.println("Saljem na "+klijent.IP+" paket "+p.rbr);
		        	dSocket.send(pack);
		        }
		        catch (IOException ex) {
		            ex.printStackTrace();
		            if (n > 10) {
		                return;
		            }
		            continue;
		        }
		            try{Thread.sleep(5000);}catch(InterruptedException e){System.out.println(e);}
					p2 = klijent.sadrziPotvrdu(p.rbr);
				
		        if (p2) {
		        	System.out.println("Primljena potvrda "+p.rbr);
		            break;
		        }
		        else {
		        	System.out.println("Pokusavam ponovo poslati");
		        }
		     }
        	} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while(!klijent.ugasi);
       
        
        
	}
	static byte[] trim(byte[] bytes)
	{
	    int i = bytes.length - 1;
	    while (i >= 0 && bytes[i] == 0)
	    {
	        --i;
	    }

	    return Arrays.copyOf(bytes, i + 1);
	}
	
	
}
