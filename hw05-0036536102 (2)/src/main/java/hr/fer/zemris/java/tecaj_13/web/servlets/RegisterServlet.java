package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;
import hr.fer.zemris.java.tecaj_13.util.Cypher;
import hr.fer.zemris.java.tecaj_13.util.RegForm;

@WebServlet("/servleti/register")
public class RegisterServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		req.getRequestDispatcher("/WEB-INF/pages/Register.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RegForm rf = new RegForm();
		req.setCharacterEncoding("UTF-8");
		rf.setEmail(req.getParameter("email"));
		rf.setPassword(req.getParameter("password"));
		rf.setFirstName(req.getParameter("firstName"));
		rf.setLastName(req.getParameter("lastName"));
		rf.setNick(req.getParameter("nick"));
		if(req.getParameter("nick").trim().equals("") || 
		   req.getParameter("password").trim().equals("") ||
		   req.getParameter("firstName").trim().equals("") ||
		   req.getParameter("lastName").trim().equals("") ||
		   req.getParameter("email").trim().equals("")) {
			req.setAttribute("error", "Please fill all fields!");

			req.setAttribute("rf", rf);
			req.getRequestDispatcher("/WEB-INF/pages/Register.jsp").forward(req, resp);
            return;
		}
		
		BlogUser bu = DAOProvider.getDAO().getUser(rf.getNick());
		
		if(bu != null) {
			req.setAttribute("error", "Nickname taken!");
			req.setAttribute("rf", rf);
			req.getRequestDispatcher("/WEB-INF/pages/Register.jsp").forward(req, resp);
            return;
		}
		
		bu = new BlogUser();
		bu.setNick(rf.getNick());
		bu.setFirstName(rf.getFirstName());
		bu.setLastName(rf.getLastName());
		bu.setPasswordHash(Cypher.process(rf.getPassword()));
		bu.setEmail(rf.getEmail());
		
		DAOProvider.getDAO().addUser(bu);
		bu = DAOProvider.getDAO().getUser(rf.getNick());//dobije id
		
		req.getSession().setAttribute("current.user.id", bu.getId());
		req.getSession().setAttribute("current.user.fn", bu.getFirstName());
		req.getSession().setAttribute("current.user.ln", bu.getLastName());
		req.getSession().setAttribute("current.user.ni", bu.getNick());
		req.setAttribute("success", "DONE");
		req.getRequestDispatcher("/WEB-INF/pages/Register.jsp").forward(req, resp);	}
	
	
}