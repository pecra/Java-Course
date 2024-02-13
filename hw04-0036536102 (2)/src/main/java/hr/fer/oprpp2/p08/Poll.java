package hr.fer.oprpp2.p08;

public class Poll {
	
	private String title;
	private String link;
	private String id;
	
	public Poll(String title, String link, String id) {
		super();
		this.title = title;
		this.link = link;
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}
