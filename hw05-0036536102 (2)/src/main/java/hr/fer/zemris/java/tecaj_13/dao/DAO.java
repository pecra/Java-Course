package hr.fer.zemris.java.tecaj_13.dao;

import java.util.List;

import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

public interface DAO {

	public BlogEntry getBlogEntry(Long id) throws DAOException;

	public BlogUser getUser(String nick);

	public void addUser(BlogUser bu);

	public List<BlogUser> getRegUsers();
	public void addEntry(BlogEntry be);

	public BlogEntry getEntry(String title, BlogUser author);

	void addComment(BlogComment bc);
	
}