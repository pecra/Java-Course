package hr.fer.zemris.java.tecaj_13.util;

public class RegForm {
	
	private String firstName;
	private String lastName;
	private String nick;
	private String email;
	private String password;
	
	public RegForm(String firstName, String lastName, String nick, String email, String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.nick = nick;
		this.email = email;
		this.password = password;
	}
	
	public RegForm() {
		this.firstName = null;
		this.lastName = null;
		this.nick = null;
		this.email = null;
		this.password = null;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
