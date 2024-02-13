package hr.fer.zemris.java.webserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

public class SmartHttpServer {
	
    private String address;
    private String domainName;
    private int port;
    private int workerThreads;
    private int sessionTimeout;
    private Map<String,String> mimeTypes = new HashMap<String, String>();
    private ServerThread serverThread;
    private ExecutorService threadPool;
    private Path documentRoot;
    private Map<String,IWebWorker> workersMap = new HashMap<>();
    private Map<String, SessionMapEntry> sessions = new ConcurrentHashMap <String, SmartHttpServer.SessionMapEntry>();
    private Random sessionRandom = new Random();

    /**
     * Konstruktor.
     * Inicijalizira varijable i ucitava u varijable iz config fileova
     * @param configFileName path do propertiesa
     * @throws Exception
     */
    public SmartHttpServer(String configFileName) throws Exception {
    	InputStream input = null;
    	Properties prop = new Properties();
    	try {
    		input = new FileInputStream(configFileName);
    	    prop.load(input);
    	} catch (IOException e) {
    	    e.printStackTrace();
    	} finally {
    	    try {
    	        input.close();
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	    }
    	}
        
        this.address = prop.getProperty("server.address");
        this.domainName = prop.getProperty("server.domainName");
        this.port = Integer.parseInt(prop.getProperty("server.port"));
        this.workerThreads = Integer.parseInt(prop.getProperty("server.workerThreads"));
        this.documentRoot = Paths.get(prop.getProperty("server.documentRoot"));
        this.sessionTimeout = Integer.parseInt(prop.getProperty("session.timeout"));
        
        Path mimeConfig = Paths.get(prop.getProperty("server.mimeConfig"));
        List<String> mimes = null;
        try {
			mimes = Files.readAllLines(mimeConfig,StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        for(String line : mimes) {
        	String[] s = line.split(" ");
        	this.mimeTypes.put(s[0],s[2]);
        }
        System.out.println(this.address);
        System.out.println(this.domainName);
        System.out.println(this.port);
        
        Path workersConfig = Paths.get(prop.getProperty("server.workers"));
        List<String> lines = null;
        try {
			lines = Files.readAllLines(workersConfig,StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        for(String line : lines) {
        	String[] s = line.split(" = ");
        	if(this.workersMap.containsKey(s[0])) {
        		throw new Exception("neispravan workers file");
        	}
        	String path = s[0];
        	String fqcn = s[1];
        	Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
        	Object newObject = referenceToClass.newInstance();
        	IWebWorker iww = (IWebWorker)newObject;
        	workersMap.put(path, iww);

        }

    }
    private synchronized void start() {
    	this.serverThread = new ServerThread();
    	this.serverThread.start();
    	System.out.println("Stratam");
    	this.threadPool = Executors.newFixedThreadPool(this.workerThreads);
    	
    	Thread thread = new Thread(()->{
    	    while(true) {
    	    	for(java.util.Map.Entry<String, SessionMapEntry> e : sessions.entrySet()) {
    	    		String key = e.getKey();
    	    		SessionMapEntry value = e.getValue();
    	    		if(value.validUntil < System.currentTimeMillis()/1000) {
    	    			sessions.remove(key);
    	    		}
    	    	}
    	    		try {
						Thread.sleep(30000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
    	    	
    	    	
    	    }
        });
    	thread.setDaemon(true);
    	thread.start();
        }
    @SuppressWarnings("removal")
	protected synchronized void stop() {
    	this.serverThread.stop();
    	this.threadPool.shutdown();
        }
    
    /**
     * Dretva koja obraduje zahtjev.
     * @author Petra
     *
     */
    protected class ServerThread extends Thread {
    	ServerSocket serverSocket = null;
    	List<Future<?>> rezultati = new ArrayList<Future<?>>();
        @Override
        public void run() {
        	try {
				serverSocket = new ServerSocket();
				serverSocket.bind(new InetSocketAddress(InetAddress.getByName(address), port));
				while(true) {
	        		 Socket client = serverSocket.accept();
	        		 ClientWorker cw = new ClientWorker(client);
	        		 rezultati.add(SmartHttpServer.this.threadPool.submit(cw));
	        		 }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
    private static class SessionMapEntry {
    	String sid;
    	String host;
    	long validUntil;
    	Map<String,String> map;
    	
    	public SessionMapEntry(String sid,String host,long validUntil,
    	    Map<String,String> map) {
    		this.host = host;
    		this.sid = sid;
    		this.validUntil = validUntil;
    		if(map == null) {
    			this.map = new ConcurrentHashMap<>();
    		}
    		else {
    			this.map = map;
    		}
    	    }
    	}

    
    private class ClientWorker implements Runnable,IDispatcher {
    	
        private Socket csocket;
        private InputStream istream;
        private OutputStream ostream;
        private String version;
        private String method;
        private String host;
        private Map<String,String> params = new HashMap<String, String>();
        private Map<String,String> tempParams = new HashMap<String, String>();
        private Map<String,String> permParams = new HashMap<String, String>();
        private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
        private String SID;
        private RequestContext context = null;
        
        public ClientWorker(Socket csocket) {
            super();
            this.csocket = csocket;
            }
        
        @Override
        public void run() {
        	// obtain input stream from socket
       	    // obtain output stream from socket
        	Socket toClient = this.csocket;
        	OutputStream cos = null;
    		try {
    			InputStream cis = new BufferedInputStream(
    				csocket.getInputStream()
    			);
    			cos = new BufferedOutputStream(
    				toClient.getOutputStream()
    			);
    			this.ostream = cos;
    			Optional<byte[]> request = readRequest(cis);
    			if(request.isEmpty()) {
    				return;
    			}
    			String requestStr = new String(
    				request.get(), 
    				StandardCharsets.US_ASCII
    			);
    			
    			List<String> headers = extractHeaders(requestStr);
    			
    			
    			String[] firstLine = headers.isEmpty() ? 
    				null : headers.get(0).split(" ");
    			if(firstLine==null || firstLine.length != 3) {
    				sendEmptyResponse(cos, 400, "Bad request");
    				return;
    			}

    			String method = firstLine[0].toUpperCase();
    			if(!method.equals("GET")) {
    				sendEmptyResponse(cos, 405, "Method Not Allowed");
    				return;
    			}
    			
    			String version = firstLine[2].toUpperCase();
    			if(!(version.equals("HTTP/1.1") || version.equals("HTTP/1.0"))) {
    				sendEmptyResponse(cos, 505, "HTTP Version Not Supported");
    				return;
    			}
    			
    			//look for host
    			String host = null;
    			for(String s: headers) {
    				if(s.startsWith("Host:")) {
    					host = s.substring(5).trim();
    					if(host.contains(":")) {
    						host = host.substring(0, host.indexOf(":"));
    					}
    					this.host = host;
    				}
    			}
    			if(host == null) {
    				this.host = SmartHttpServer.this.domainName;
    			}
    			checkSession(headers);
    			String rp = firstLine[1];
    			String[] splitPath = rp.split("\\?");
    			String path = splitPath[0];
    			if(splitPath.length == 2) {
    				parseParameters(splitPath[1].trim());
    			}
    			if(path.startsWith("C:")) {
        			path = path.substring(1);
    			}
    			
    			internalDispatchRequest(path,true);
    			
    			

    			
    		} catch(Exception ex) {
    			ex.printStackTrace();
    			System.out.println("Pogreska: " + ex.getMessage());
    		} finally {
    			if(cos!=null) {
    				try { cos.flush(); } catch(Exception ignorable) {}
    			}
    			try {
    				toClient.close();
    			} catch(Exception ex) {
    				System.out.println("PogreĹˇka: " + ex.getMessage());
    			}
    		}

            }

        private void checkSession(List<String> headers) {

        	String sidCandidate = null;
			for(String line : headers) {
				System.out.println(line);
				if(line.startsWith("Cookie:")) {
					String[] cookies = line.substring(8).split(";");
					for(String cookie : cookies) {
						if(cookie.startsWith("sid")) {
							
							sidCandidate = cookie.substring(4).trim();

						}
					}
				}
			}


			if(sidCandidate != null) {
				SessionMapEntry object = sessions.get(sidCandidate);
			    if(object != null ) {
			    	if(host.equals(object.host)) {
			    		if(object.validUntil <= System.currentTimeMillis() / 1000) {
				    		sessions.remove(sidCandidate);
				    	//	cont = true;//jel treba i tu perm params??/
				    	}
				    	else {
				    		object.validUntil = System.currentTimeMillis() / 1000 +sessionTimeout;
				    		permParams = object.map;
				    		return;
				    	}
			    	}
			    	
			}}
            if (sidCandidate != null) {
                SessionMapEntry session = sessions.get(sidCandidate);
                if (session != null) {
                    if (session.host.equals(host)) {
                        if (session.validUntil > System.currentTimeMillis() / 1000) {
                            session.validUntil = System.currentTimeMillis() / 1000 + sessionTimeout;
                            permParams = session.map;
                            return;
                        } else {
                            sessions.remove(sidCandidate);
                        }
                    }
                }
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 20; i++) {
                    sb.append((char) ((int) 'A' + sessionRandom.nextDouble() * ((int) 'Z' - (int) 'A' + 1)));
                }
                sidCandidate = sb.toString();
            }
            SessionMapEntry newSession = new SessionMapEntry(sidCandidate, host, System.currentTimeMillis() / 1000 + sessionTimeout,
                    new ConcurrentHashMap<>());
            sessions.put(sidCandidate, newSession);
            outputCookies.add(new RequestContext.RCCookie("sid", sidCandidate, null, host, "/"));
            permParams = newSession.map;
        }

		private void parseParameters(String trim) {
			String[] parms = trim.split("&");
			for(String s : parms) {
				String[] nv = s.split("=");
				this.params.put(nv[0], nv[1]);
			}
			
		}

		private void internalDispatchRequest(String urlPath, boolean directCall)
				 throws Exception {
		//	System.out.println(urlPath);
			if(context == null) {
				this.context = new RequestContext(ostream, params, permParams, outputCookies,
						new HashMap<>(), this);
			}
			if (urlPath.equals("/private") || urlPath.startsWith("/private")) {
                if (directCall) {
                    sendEmptyResponse(ostream, 404, "File not Found");
                    return;
                }

            }

			if(urlPath.contains("/ext/")) {
					String a = "hr.fer.zemris.java.webserver.workers.";
					int index = urlPath.lastIndexOf("/");
					String fqcn = a+ urlPath.substring(index+1);
					Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
		        	Object newObject = referenceToClass.newInstance();
		        	IWebWorker iww = (IWebWorker)newObject;    
			        iww.processRequest(context);
			}
			
			else if(SmartHttpServer.this.workersMap.containsKey(urlPath)) {
 
				IWebWorker worker = SmartHttpServer.this.workersMap.get(urlPath);
				worker.processRequest(context);
			}
			else {
				String pathString = urlPath.toString().replace('/', '\\');
				if (pathString.startsWith("\\")) {
				    pathString = pathString.substring(1);
				}
				Path urlPat = Paths.get(pathString);
				Path doc2 = SmartHttpServer.this.documentRoot.normalize().toAbsolutePath();
				Path requestedPath = doc2.resolve(urlPat);
    			if(!requestedPath.startsWith(doc2)) {
    				sendEmptyResponse(ostream, 403, "Forbidden");
    				return;
    			}
    			if(!Files.exists(requestedPath)) {
    				sendEmptyResponse(ostream, 404, "File not found");
    			}
    			if(!Files.exists(requestedPath)) {
    				sendEmptyResponse(ostream, 404, "File not found");
				}
    			if(!Files.isReadable(requestedPath)) {
    				sendEmptyResponse(ostream, 404, "File not found");
				}
    			RequestContext rc = new RequestContext(ostream, params, permParams, outputCookies,context.temporaryParameters,this);
    			if(requestedPath.toString().endsWith("smscr")) {
    				String docBody = new String(
                            Files.readAllBytes(requestedPath),
                            StandardCharsets.UTF_8
                    );
    				SmartScriptParser parser = new SmartScriptParser(docBody);
    				new SmartScriptEngine(
    						parser.getDocumentNode(), rc
    						).execute();
    			}
    			else {
    				this.sendFile(rc, requestedPath);
    			
    			}
			}
		}
		public void dispatchRequest(String urlPath) throws Exception {
				 internalDispatchRequest(urlPath, false);
		}
		public void sendFile(RequestContext rc, Path requestedPath) throws IOException {
			System.out.println(requestedPath.toString());
		    int i = requestedPath.toString().lastIndexOf(".");
		    String type = null;
		    if (i > 0) {
		        String extension = requestedPath.toString().substring(requestedPath.toString().indexOf(".") + 1);
		        type = SmartHttpServer.this.mimeTypes.get(extension);
		    }
		    if (type == null) {
		        type = "application/octet-stream";
		    }

		    rc.setMimeType(type);
		    rc.setStatusCode(200);

		    int bufferSize = 4096;
		    byte[] buffer = new byte[bufferSize];
		    int bytesRead = 0;

		    try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(requestedPath))) {
		        while ((bytesRead = bis.read(buffer, 0, bufferSize)) != -1) {
		            rc.write(buffer, 0, bytesRead);
		        }
		    }
		}
        }
    private static Optional<byte[]> readRequest(InputStream is) throws IOException {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int state = 0;
l:		while(true) {
			int b = is.read();
			if(b==-1) {
				if(bos.size()!=0) {
					throw new IOException("Incomplete header received.");
				}
				return Optional.empty();
			}
			if(b!=13) {
				bos.write(b);
			}
			switch(state) {
			case 0: 
				if(b==13) { state=1; } else if(b==10) state=4;
				break;
			case 1: 
				if(b==10) { state=2; } else state=0;
				break;
			case 2: 
				if(b==13) { state=3; } else state=0;
				break;
			case 3: 
				if(b==10) { break l; } else state=0;
				break;
			case 4: 
				if(b==10) { break l; } else state=0;
				break;
			}
		}
		return Optional.of(bos.toByteArray());
	}
    private static List<String> extractHeaders(String requestHeader) {
		List<String> headers = new ArrayList<String>();
		String currentLine = null;
		for(String s : requestHeader.split("\n")) {
			if(s.isEmpty()) break;
			char c = s.charAt(0);
			if(c==9 || c==32) {
				currentLine += s;
			} else {
				if(currentLine != null) {
					headers.add(currentLine);
				}
				currentLine = s;
			}
		}
		if(!currentLine.isEmpty()) {
			headers.add(currentLine);
		}
		return headers;
	}
    private static void sendResponseWithData(OutputStream cos, int statusCode, String statusText, String contentType, byte[] data) throws IOException {

		cos.write(
			("HTTP/1.1 "+statusCode+" "+statusText+"\r\n"+
			"Server: simple java server\r\n"+
			"Content-Type: "+contentType+"\r\n"+
			"Content-Length: "+data.length+"\r\n"+
			"Connection: close\r\n"+
			"\r\n").getBytes(StandardCharsets.US_ASCII)
		);
		cos.write(data);
		cos.flush();
	}
	
	// Pomocna metoda za slanje odgovora bez tijela...
	private static void sendEmptyResponse(OutputStream cos, int statusCode, String statusText) throws IOException {
		sendResponseWithData(cos, statusCode, statusText, "text/plain;charset=UTF-8", new byte[0]);
	}
	
	public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("You must provide path to server.properties");
            System.exit(1);
        }

        try {
			new SmartHttpServer(args[0]).start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
}
