package hr.fer.zemris.java.tecaj_13.util;


public class EntryForm {
	
	private String title;
	private String text;
	
	public EntryForm(String title, String text) {
		super();
		this.title = title;
		this.text = text;
	}
	
	public EntryForm() {
		this.title = null;
		this.text = null;
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

}