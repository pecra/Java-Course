package oprpp2.glavni;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import oprpp2.poruke.AckPoruka;
import oprpp2.poruke.ByePoruka;
import oprpp2.poruke.InmsgPoruka;
import oprpp2.poruke.OutmsgPoruka;
import oprpp2.poruke.Poruka;
import oprpp2.poruke.helloPoruka;


public class Klijent {
	
	String hostname;
	int port;
	String ime;
	long randKey;
	long UID;
	DatagramSocket dSocket;
	volatile long zadnjiRbr;
	JTextField unos;
	JTextArea chat;
	Queue<Long> potvrde = new LinkedList<>();
	
	public Klijent(String hostname, int port, String ime, long randKey,long UID,DatagramSocket dSocket) {
		super();
		this.hostname = hostname;
		this.port = port;
		this.ime = ime;
		this.randKey = randKey;
		this.dSocket = dSocket;
		this.UID = UID;
		this.zadnjiRbr = 0;
		this.potvrde.add(0L);
		this.initGUI();
	}
	
	private void initGUI() {
        JFrame frame = new JFrame("Chat client: " + this.ime);
        this.unos = new JTextField();
        this.chat = new JTextArea();
        
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(this.unos, "First");
        
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.chat.setEditable(false);
        frame.getContentPane().add(new JScrollPane(this.chat), "Center");
        this.unos.addActionListener(a -> {
            String poruka = this.unos.getText().trim();
            this.unos.setText("");
            this.unos.setEnabled(false);
            if(poruka.length() > 0) {
            	new Thread(() -> this.posaljiPoruku(poruka)).start();
            }
            return;
        });
        
        frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) { /// posalji bye u slucaju zatvaranja
				byte[] podatci = new ByePoruka(Klijent.this.zadnjiRbr+1L,Klijent.this.UID).okteti();
				DatagramPacket packet = new DatagramPacket(
					podatci, podatci.length
				);
			    try {
			    	InetAddress addr = InetAddress.getByName(Klijent.this.hostname);
				    packet.setAddress(addr);
				    packet.setPort(Klijent.this.port);
					dSocket.send(packet);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			    dSocket.close();
				System.exit(0);
			}
		});
        
        
        
        frame.setSize(500, 300);
        frame.setVisible(true);
        new Thread(() -> this.primajPoruke()).start();
    }

	private void primajPoruke() {
		try {
			dSocket.setSoTimeout(0);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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

			// Izracunaj koji je tip poruke:
		    short kodPoruke = recvBuffer[0];
		    
		    byte[] slice = trim(recvBuffer);
		  
            System.out.println("nova poruka");
		    switch(kodPoruke){
		    case(1):
		    	System.out.println("Neocekivana poruka 1.");
				break;
		    case(2):
		    	
		    	AckPoruka p2 = new AckPoruka(slice);
		        this.potvrde.add(p2.rbr);
		        System.out.println("Ack poruka."+p2.rbr);
		        break;
		    case(3):
		    	System.out.println("Neocekivana poruka 3.");
	            break;
		    case(4):
		    	System.out.println("Neocekivana poruka 4.");
				break;
		    case(5):
		    	InmsgPoruka p = new InmsgPoruka(slice);
		        SwingUtilities.invokeLater(() -> this.chat.append("[" + recvPacket.getSocketAddress() + "] Poruka od korisnika: " + p.getIme() + "\n" + p.getText() + "\n\n"));
		        byte[] podatci = new AckPoruka(p.rbr,this.UID).okteti();

				// Umetni podatke u paket i paketu postavi adresu i 
				// port posluĹľitelja
				DatagramPacket packet = new DatagramPacket(
					podatci, podatci.length
				);
			    InetAddress addr;
				try {
					addr = InetAddress.getByName(hostname);
					packet.setAddress(addr);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			    packet.setPort(port);
			    try {
					this.dSocket.send(packet);
				} catch(IOException e) {
					System.out.println("Ne mogu poslati potvrdu:"+p.rbr);
					break;
				}
		        break;
	        }

		}
	}

	private void posaljiPoruku(String poruka) {
		   System.out.println("PORUKA"+poruka);
		   long rednibr = this.zadnjiRbr+1;
		   
		  // System.out.println("rbr"+rednibr);
		   OutmsgPoruka po = new OutmsgPoruka(rednibr, poruka, this.UID);
		   byte[] podatci = po.okteti();
		   for(byte b : podatci) {
			   System.out.println(b);
		   }
		   DatagramPacket packet = new DatagramPacket(
				podatci, podatci.length
			);
		   InetAddress addr;
		   try {
				addr = InetAddress.getByName(this.hostname);
				packet.setAddress(addr);
			    packet.setPort(this.port);
		   } catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
		   }
			
	        int i = 0;
	        boolean answerReceived = false;
	        AckPoruka p = null;
			// Ĺ alji upite sve dok ne dobijeĹˇ odgovor:
			while(i < 5) {
				
				// PoĹˇalji upit posluĹľitelju:
				System.out.println("Saljem poruku");
				try {
					dSocket.send(packet);
				} catch(IOException e) {
					System.out.println("Ne mogu poslati poruku.");
					break;
				}
				try{Thread.sleep(5000);}catch(InterruptedException e){System.out.println(e);}  
				// ÄŚekaj na odgovor:
				if(!this.sadrziPotvrdu(rednibr)) {
					i++;
					continue;
				}
				// Ispisi rezultat, i prekini slanje retransmisija:
				System.out.println(
					"Primljena potvrda"
				);
				answerReceived = true;
				break;
			}
			if(!answerReceived) {
				System.out.println("Posluzitelj ne odgovara");
			}
			else {
				this.zadnjiRbr++;
			}
       
		this.unos.setEnabled(true);
	}

	private boolean sadrziPotvrdu(long rednibr) {
		return this.potvrde.contains(rednibr);
	}

	public static void main(String[] args) 
		throws SocketException, UnknownHostException {
		
		if(args.length!=3) {
			System.out.println("Ocekivao sam host port ime");
			return;
		}
		
		String hostname = args[0];
		int port = Integer.parseInt(args[1]);
		String ime = args[2];
		long randKey = new Random().nextLong();

		
		
		// Pripremi podatke upita koje treba poslati:
		byte[] podatci = new helloPoruka(0L, ime, randKey).okteti();
		
		// Stvori pristupnu toÄŤku klijenta:
		DatagramSocket dSocket = new DatagramSocket();

		// Umetni podatke u paket i paketu postavi adresu i 
		// port posluĹľitelja
		DatagramPacket packet = new DatagramPacket(
			podatci, podatci.length
		);
	    InetAddress addr = InetAddress.getByName(hostname);
	    packet.setAddress(addr);
	    packet.setPort(port);
		
		// Postavi timeout od 4 sekunde na primitak odgovora:
		dSocket.setSoTimeout(5000);
        int i = 0;
        boolean answerReceived = false;
        AckPoruka p = null;
		// Ĺ alji upite sve dok ne dobijeĹˇ odgovor:
		while(i < 5) {
			
			// PoĹˇalji upit posluĹľitelju:
			System.out.println("Saljem upit...");
			try {
				dSocket.send(packet);
			} catch(IOException e) {
				System.out.println("Ne mogu poslati upit.");
				break;
			}
		
			// Pripremi prazan paket za primitak odgovora:
			byte[] recvBuffer = new byte[1024];
			DatagramPacket recvPacket = new DatagramPacket(
				recvBuffer, recvBuffer.length
			);
			
			// cekaj na odgovor:
			try {
				dSocket.receive(recvPacket);
			} catch(SocketTimeoutException ste) {
				System.out.println("Pokusat cu ponovo...");
				// Ako je isteko timeout, ponovno salji upit
				i++;
				continue;
			} catch(IOException e) {
				// U slucaju drugih pogresaka - dogovoriti se sto dalje...
				// (mi opet radimo retransmisiju)
				continue;
			}
			short kodPoruke = recvBuffer[0];
			if(kodPoruke != (short)2) {
		    	continue;
		    }
			byte[] slice = trim(recvBuffer);
			// Analiziraj sadrzaj paketa:
			p = new AckPoruka(slice);
			if(p.rbr != 0L) {
				System.out.println("neocekivani rbr");
		    }
			// Ispisi rezultat, i prekini slanje retransmisija:
			System.out.println(
				"Primljena potvrda,uid je: " + 
				p.getUID()
			);
			answerReceived = true;
			break;
		}
		if(answerReceived) {
			long U = p.getUID();
			SwingUtilities.invokeLater(() -> new Klijent(hostname,port,ime,randKey,U,dSocket));
		} else {
			System.out.println("Nismo uspjeli uspostaviti vezu!");
			dSocket.close();
            return;
		}

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
