package hr.fer.zemris.java.tecaj_13.dao.jpa;

import java.util.List;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOException;
import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
		return blogEntry;
	}

	@Override
	public BlogUser getUser(String nick) {
		List<BlogUser> blogUser = JPAEMProvider.getEntityManager().createQuery("SELECT b"
				+ " FROM BlogUser as b where b.nick =:nick").setParameter("nick" , nick).getResultList();
		if(blogUser.size() == 0) {
			return null;
		}
		return blogUser.get(0);
	}
	

	@Override
	public void addUser(BlogUser bu) {
		JPAEMProvider.getEntityManager().persist(bu);
	}

	@Override
	public List<BlogUser> getRegUsers() {
		List<BlogUser> blogUsers = JPAEMProvider.getEntityManager().createQuery("SELECT b"
				+ " FROM BlogUser as b ").getResultList();
		return blogUsers;
	}

	@Override
	public BlogEntry getEntry(String title, BlogUser author) {
		List<BlogEntry> blogE = JPAEMProvider.getEntityManager().createQuery("SELECT b"
				+ " FROM BlogEntry as b where b.title =:title and b.creator =:author").setParameter("title" , title).setParameter("author", author).getResultList();
		if(blogE.size() == 0) {
			return null;
		}
		return blogE.get(0);
	}

	@Override
	public void addEntry(BlogEntry be) {
		JPAEMProvider.getEntityManager().persist(be);
	}
	
	@Override
	public void addComment(BlogComment bc) {
		JPAEMProvider.getEntityManager().persist(bc);
	}

}