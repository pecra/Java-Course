package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RequestContext {
	
	/**
	 * Private static class.
	 * @author Petra
	 *
	 */
	public static class RCCookie{
		private String name;
		private String value;
		private String domain;
		private String path;
		private Integer maxAge;
		
		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			super();
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}
		
		public String getName() {
			return name;
		}
		public String getValue() {
			return value;
		}
		public String getDomain() {
			return domain;
		}
		public String getPath() {
			return path;
		}
		public Integer getMaxAge() {
			return maxAge;
		}
	}
	/**
	 * Class variables
	 */
	private OutputStream outputStream;
	private Charset charset;
	public String encoding = "UTF-8";
	public int statusCode = 200;
	public String statusText = "OK";
	public String mimeType = "text/html";
	public Long contentLength = null;
	private Map<String,String> parameters;
	Map<String,String> temporaryParameters;
	private Map<String,String> persistentParameters;
	private List<RCCookie> outputCookies;
	private boolean headerGenerated = false;
	private IDispatcher dispatcher;
	
	/**
	 * Konstruktor
	 * @param outputStream
	 * @param parameters
	 * @param persistentParameters
	 * @param outputCookies
	 */
	public RequestContext(
	        OutputStream outputStream,
	        Map<String, String> parameters,
	        Map<String, String> persistentParameters,
	        List<RCCookie> outputCookies
	    ) 
	{
	      	if (outputStream == null) {
		     	throw new IllegalArgumentException("Argumenti ne mogu biti null");
			}
	        this.outputStream = outputStream;
	        init(parameters, persistentParameters, outputCookies);
	    }

	/**
	 * Konstruktor koji prima i tempParams i dispatcher.
	 * @param outputStream
	 * @param parameters
	 * @param persistentParameters
	 * @param outputCookies
	 * @param temporaryParameters
	 * @param dispatcher
	 */
	public RequestContext(
	        OutputStream outputStream,
	        Map<String, String> parameters,
	        Map<String, String> persistentParameters,
	        List<RCCookie> outputCookies,
	        Map<String, String> temporaryParameters,
	        IDispatcher dispatcher
	    ) 
	{
		    if (outputStream == null) {
		    	throw new IllegalArgumentException("Argumenti ne mogu biti null");
			}
	        this.outputStream = outputStream;
	        init(parameters, persistentParameters, outputCookies);
	        this.temporaryParameters = temporaryParameters;
	        this.dispatcher = dispatcher;
	}

	private void init(Map<String, String> parameters, Map<String, String> persistentParameters, List<RCCookie> outputCookies) {
	       
		if (parameters == null) {
	            this.parameters = Collections.unmodifiableMap(new HashMap<String,String>());
	        
		} else {
	            this.parameters = Collections.unmodifiableMap(parameters);
	        }
	        
		if (persistentParameters == null) {
	            this.persistentParameters = new LinkedHashMap<String,String>();
	        
		} else {
	            this.persistentParameters = persistentParameters;
	        }
	        
		if (outputCookies == null) {
	            this.outputCookies = new LinkedList<RCCookie>();
	        
		} else {
	            this.outputCookies = outputCookies;
	        }
	}
	public IDispatcher getDispatcher() {
		return dispatcher;
	}
	/**
	 * Retreives value from parameters map,
	 * null if no association exists.
	 * @param name
	 * @return
	 */
	public String getParameter(String name) {
		return this.parameters.get(name);
	}
	/**
	 *  Method that retrieves names
	 *  of all parameters in parameters map
	 * @return
	 */
	public Set<String> getParameterNames(){
		return Collections.unmodifiableSet(this.parameters.keySet());
	}
	/**
	 *  Method that retrieves value from 
	 *  persistentParameters map, null if no association exists.
	 * @param name
	 */
	public String getPersistentParameter(String name) {
		return this.persistentParameters.get(name);
	}
	/**
	 * Adds cookie.
	 * @param c
	 */
	public  void addRCCookie(RCCookie c) {
		this.outputCookies.add(c);
	}
	/**
	 *  Method that retrieves names
	 *  of all parameters in persistentParameters map
	 * @return
	 */
	public Set<String> getPersistentParameterNames(){
		return Collections.unmodifiableSet(this.persistentParameters.keySet());
	}
	/**
	 * Method that stores a value
	 *  to persistentParameters map.
	 * @param name
	 * @param value
	 */
	public void setPersistentParameter(String name, String value) {
		this.persistentParameters.put(name, value);
	}
	/**
	 * Method that removes a value
	 *  to persistentParameters map.
	 * @param name
	 * @param value
	 */
	public void removePersistentParameter(String name) {
		this.persistentParameters.remove(name);
	}
	/**
	 * Method that retrieves value from
	 *  temporaryParameters map
	 * @param name
	 * @return
	 */
	public String getTemporaryParameter(String name) {
		return this.temporaryParameters.get(name);
	}
	/**
	 * Method that retrieves names of 
	 * all parameters in temporary parameters map
	 * @return
	 */
	public Set<String> getTemporaryParameterNames(){
		return Collections.unmodifiableSet(this.temporaryParameters.keySet());
	}
	/**
	 * Method that retrieves an 
	 * identifier which is unique for current user session
	 * @return
	 */
	public String getSessionID() {
		for(RCCookie cookie : this.outputCookies) {
			if (cookie.getName().equals("sid")) {
				return cookie.getValue();
			}
		}
		return null;
	}
	/**
	 * method that stores a value 
	 * to temporaryParameters map
	 * @param e
	 */
	public void setTemporaryParameter(String name, String value) {
		this.temporaryParameters.put(name, value);
	}
	/**
	 * method that removes 
	 * a value from temporaryParameters map
	 * @param e
	 */
	public void removeTemporaryParameter(String name) {
		this.temporaryParameters.remove(name);
	}
	private void generateHeader() throws IOException {

		this.charset = Charset.forName(encoding);
		StringBuilder sb = new StringBuilder();
		sb.append("HTTP/1.1 " + this.statusCode + " "+ this.statusText+"\r\n");
		sb.append("Content-Type: "+this.mimeType);
		if(this.mimeType.startsWith("text/")) {
			sb.append("; charset="+this.encoding);
		}
		
		if(this.contentLength != null) {
			sb.append("\r\n");
			sb.append("Content-Length: " +this.contentLength);
		}
		sb.append("\r\n");
	//	if(this.outputCookies.size() != 0) {
			for(RCCookie cookie : this.outputCookies) {
				sb.append("Set-Cookie: ");
	            sb.append(cookie.getName());
	            sb.append("=\"");
	            sb.append(cookie.getValue());
	            sb.append("\"");
	            if (cookie.getDomain() != null) {
	                sb.append("; Domain=");
	                sb.append(cookie.getDomain());
	            }
	            if (cookie.getPath() != null) {
	                sb.append("; Path=");
	                sb.append(cookie.getPath());
	            }
	            if (cookie.getMaxAge() != null) {
	                sb.append("; Max-Age=");
	                sb.append(cookie.getMaxAge());
	            }
	            sb.append("\r\n");
		//	}
		}
		sb.append("\r\n");
		
		this.outputStream.write(sb.toString().getBytes(StandardCharsets.ISO_8859_1));
		this.headerGenerated = true;
	}
	
	
	/**
     * This method is used for writing data to output stream.
     *
     * @param data data
     * @return This
     * @throws IOException Input output exception
     */
	public RequestContext write(byte[] data) throws IOException {
		if(!this.headerGenerated) {
			this.generateHeader();
		}
		this.outputStream.write(data);
		return this;
	}
	/**
     * This method is used for writing data to output stream.
     *
     * @param data data
     * @return This
     * @throws IOException Input output exception
     */
	public RequestContext write(byte[] data, int offset, int len) throws IOException{
		if(!this.headerGenerated) {
			this.generateHeader();
		}
		this.outputStream.write(Arrays.copyOfRange(data, offset, len));
		return this;
	}
	/**
     * This method is used for writing data to output stream.
     *
     * @param data data
     * @return This
     * @throws IOException Input output exception
     */
	public RequestContext write(String text) throws IOException{
		if(!this.headerGenerated) {
			this.generateHeader();
		}
		this.outputStream.write(text.getBytes(this.charset));
		return this;
	}

	
	public void setEncoding(String e) {
		if(this.headerGenerated) {
			throw new RuntimeException("Header already generated");
		}
		this.encoding = e;
	}
	public void setContentLength(Long e) {
		if(this.headerGenerated) {
			throw new RuntimeException("Header already generated");
		}
		this.contentLength = e;
	}
	public void setStatusCode(int e) {
		if(this.headerGenerated) {
			throw new RuntimeException("Header already generated");
		}
		this.statusCode = e;
	}
	public void setMimeType(String e) {
		if(this.headerGenerated) {
			throw new RuntimeException("Header already generated");
		}
		this.mimeType = e;
	}
	public void setStatusText(String e) {
		if(this.headerGenerated) {
			throw new RuntimeException("Header already generated");
		}
		this.statusText = e;
	}

}
