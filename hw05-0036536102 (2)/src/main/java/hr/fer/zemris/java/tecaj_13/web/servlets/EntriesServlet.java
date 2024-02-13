package hr.fer.zemris.java.tecaj_13.web.servlets;


import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.dao.jpa.JPADAOImpl;
import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;
import hr.fer.zemris.java.tecaj_13.util.Cypher;
import hr.fer.zemris.java.tecaj_13.util.EntryForm;
import hr.fer.zemris.java.tecaj_13.util.RegForm;

@WebServlet("/servleti/author/*")
public class EntriesServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JPADAOImpl dao = (JPADAOImpl) DAOProvider.getDAO();
		String requestURI = req.getRequestURI();
        String[] pathParts = requestURI.split("/");
        List<String> path = Arrays.asList(pathParts);
        int indexAuthor = path.indexOf("author");
        String lastPart = path.get(indexAuthor+1);
		BlogUser author = dao.getUser(lastPart);
		
		if (author == null) {
            req.setAttribute("error", "Traženi autor ne postoji");
            req.getRequestDispatcher("/WEB-INF/pages/Err.jsp").forward(req, resp);
            return;
        }
		
		if(path.size() <= indexAuthor+2) {
			List<BlogEntry> entries = author.getEntries();
			req.setAttribute("author", author);
			req.getRequestDispatcher("/WEB-INF/pages/Entries.jsp").forward(req, resp);
		}
		else {
			
			if(pathParts[indexAuthor + 2].equals("new")) {//ako je stvaranje novog
				if(req.getSession().getAttribute("current.user.id") == null || !req.getSession().getAttribute("current.user.id").equals(author.getId())) {
					req.setAttribute("author", author);
					req.setAttribute("accDen", "Access denied!!");
					req.getRequestDispatcher("/WEB-INF/pages/NewEntry.jsp").forward(req, resp);
					return;
				}
				req.setAttribute("author", author);
				req.setAttribute("entry", new EntryForm());
				req.setAttribute("action", req.getContextPath() + "/servleti/author/" + author.getNick() + "/new");
				req.getRequestDispatcher("/WEB-INF/pages/NewEntry.jsp").forward(req, resp);
				return;
			}
			
			if(pathParts[indexAuthor + 2].equals("edit")) {//ako je uredivanje
				if(req.getSession().getAttribute("current.user.id") == null ||  !req.getSession().getAttribute("current.user.id").equals(author.getId())) {
					req.setAttribute("author", author);
					req.setAttribute("accDen", "Access denied!!");
					req.getRequestDispatcher("/WEB-INF/pages/NewEntry.jsp").forward(req, resp);
					return;
				}
				req.setAttribute("author", author);
				BlogEntry be = DAOProvider.getDAO().getBlogEntry(Long.valueOf(pathParts[indexAuthor + 3]));
				if(be == null) {
					req.setAttribute("error", "Traženi entry ne postoji");
		            req.getRequestDispatcher("/WEB-INF/pages/Err.jsp").forward(req, resp);
		            return;
				}
				req.setAttribute("action", req.getContextPath() + "/servleti/author/" + author.getNick() + "/edit/"+be.getId());
				req.setAttribute("entry", be);
				req.getRequestDispatcher("/WEB-INF/pages/NewEntry.jsp").forward(req, resp);
				return;
			}
			else {
				req.setAttribute("author", author);//citanje entryja
				BlogEntry be = DAOProvider.getDAO().getBlogEntry(Long.valueOf(pathParts[indexAuthor + 2]));
				if(be == null) {
					req.setAttribute("error", "Traženi entry ne postoji");
		            req.getRequestDispatcher("/WEB-INF/pages/Err.jsp").forward(req, resp);
		            return;
				}
				if(!be.getCreator().equals(author)) {
					req.setAttribute("error", "Greska");
		            req.getRequestDispatcher("/WEB-INF/pages/Err.jsp").forward(req, resp);
		            return;
				}
				req.setAttribute("action", req.getContextPath() + "/servleti/author/" + author.getNick() + "/edit");
				req.setAttribute("entry", be);
				req.getRequestDispatcher("/WEB-INF/pages/Entry.jsp").forward(req, resp);
			}
		}
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JPADAOImpl dao = (JPADAOImpl) DAOProvider.getDAO();
		String requestURI = req.getRequestURI();
        String[] pathParts = requestURI.split("/");
        List<String> path = Arrays.asList(pathParts);
        int indexAuthor = path.indexOf("author");
        String lastPart = path.get(indexAuthor+1);
		BlogUser author = dao.getUser(lastPart);
		
		if (author == null) {
            req.setAttribute("error", "Traženi autor ne postoji");
            req.getRequestDispatcher("/WEB-INF/pages/Err.jsp").forward(req, resp);
            return;
        }
		
		if(pathParts[indexAuthor + 2].equals("new")) {//stvaranje novog entryja
			
			if (!req.getSession().getAttribute("current.user.id").equals(author.getId())) {
	            req.setAttribute("error", "Zabranjen pristup!");
	            req.getRequestDispatcher("/WEB-INF/pages/Err.jsp").forward(req, resp);
	            return;
	        }
			
			EntryForm ef = new EntryForm();
			req.setCharacterEncoding("UTF-8");
			ef.setText(req.getParameter("text"));
			ef.setTitle(req.getParameter("title"));
			
			if(req.getParameter("text").trim().equals("") || 
			   req.getParameter("title").trim().equals("")) {
				req.setAttribute("error", "Please fill all fields!");

				req.setAttribute("rf", ef);
				req.getRequestDispatcher("/WEB-INF/pages/NewEntry.jsp").forward(req, resp);
	            return;
			}
			
			BlogEntry be = DAOProvider.getDAO().getEntry(ef.getTitle(),author);
			
			if(be != null) {
				req.setAttribute("error", "Entry with that title already exists!");
				req.setAttribute("ef", ef);
				req.getRequestDispatcher("/WEB-INF/pages/NewEntry.jsp").forward(req, resp);
	            return;
			}
			
			be = new BlogEntry();
			be.setTitle(ef.getTitle());
			be.setText(ef.getText());
			be.setCreatedAt(new Date());
			be.setLastModifiedAt(new Date());
			be.setCreator(author);
			
			dao.addEntry(be);
			
			resp.sendRedirect(req.getContextPath() + "/servleti/author/" + author.getNick());
			return;
		}
        if(pathParts[indexAuthor + 2].equals("edit")) {//stvaranje novog entryja
			
			if (!req.getSession().getAttribute("current.user.id").equals(author.getId())) {
	            req.setAttribute("error", "Zabranjen pristup!");
	            req.getRequestDispatcher("/WEB-INF/pages/Err.jsp").forward(req, resp);
	            return;
	        }
			
			EntryForm ef = new EntryForm();
			req.setCharacterEncoding("UTF-8");
			ef.setText(req.getParameter("text"));
			ef.setTitle(req.getParameter("title"));
			
			if(req.getParameter("text").trim().equals("") || 
			   req.getParameter("title").trim().equals("")) {
				req.setAttribute("error", "Please fill all fields!");

				req.setAttribute("rf", ef);
				req.getRequestDispatcher("/WEB-INF/pages/NewEntry.jsp").forward(req, resp);
	            return;
			}
			
			BlogEntry be = DAOProvider.getDAO().getBlogEntry(Long.valueOf(pathParts[indexAuthor + 3]));
			
			if(be == null) {
				req.setAttribute("error", "Entry with that id doesn't exist!");
				req.setAttribute("ef", ef);
				req.getRequestDispatcher("/WEB-INF/pages/NewEntry.jsp").forward(req, resp);
	            return;
			}
			
			be.setTitle(ef.getTitle());
			be.setText(ef.getText());
			be.setCreatedAt(new Date());
			be.setLastModifiedAt(new Date());
			
			dao.addEntry(be);
			
			resp.sendRedirect(req.getContextPath() + "/servleti/author/" + author.getNick());
			return;
		}
        ///komentar

			req.setCharacterEncoding("UTF-8");
			String comment = req.getParameter("comment");
			if(comment == null || comment.trim().equals("")) {
				req.setAttribute("error", "Comment is empty!");
				req.getRequestDispatcher("/WEB-INF/pages/NewEntry.jsp").forward(req, resp);
	            return;
			}
			BlogComment bc = new BlogComment();
			BlogEntry be = DAOProvider.getDAO().getBlogEntry(Long.valueOf(pathParts[indexAuthor + 2]));
			
			if (be == null) {
	            req.setAttribute("error", "Nepostojeci entry!");
	            req.getRequestDispatcher("/WEB-INF/pages/Err.jsp").forward(req, resp);
	            return;
	        }
			BlogUser bu = dao.getUser(String.valueOf(req.getSession().getAttribute("current.user.ni")));
			bc.setBlogEntry(be);
			bc.setMessage(comment);
			bc.setPostedOn(new Date());
			if(bu != null) {
				bc.setUsersEMail(bu.getEmail());
			}
			else {
				bc.setUsersEMail("anon");
			}
			
			dao.addComment(bc);
			
			resp.sendRedirect(req.getContextPath() + "/servleti/author/" + author.getNick());
			return;
		
		
	}
	
	
	
}