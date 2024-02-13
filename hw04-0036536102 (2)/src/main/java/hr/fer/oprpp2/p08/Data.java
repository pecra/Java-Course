package hr.fer.oprpp2.p08;

public class Data {
	
	private String title;
	private String link;
	private String id;
	private String votes;
	public Data(String title, String link, String id, String votes) {
		super();
		this.title = title;
		this.link = link;
		this.id = id;
		this.votes = votes;
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
	public String getVotes() {
		return votes;
	}
	public void setVotes(String votes) {
		this.votes = votes;
	}
	

}
