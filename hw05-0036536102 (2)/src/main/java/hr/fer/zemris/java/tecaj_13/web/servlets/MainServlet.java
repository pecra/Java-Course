package hr.fer.zemris.java.tecaj_13.web.servlets;

import java.io.IOException;
import java.util.List;

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

@WebServlet("/servleti/main")
public class MainServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("HALLO");
		List<BlogUser> rusers = DAOProvider.getDAO().getRegUsers();
		req.getSession().setAttribute("regAuthors", rusers);
		for(BlogUser bu : rusers) {
			System.out.println(bu.getFirstName());
		}
		req.getRequestDispatcher("/WEB-INF/pages/Main.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		String pass = req.getParameter("password");
		String nick = req.getParameter("nick");
		
		if(pass.trim().equals("") || nick.trim().equals("")) {
			req.setAttribute("error", "Please fill all fields!");
			req.getRequestDispatcher("/WEB-INF/pages/Main.jsp").forward(req, resp);
            return;
		}
		
		BlogUser bu = DAOProvider.getDAO().getUser(nick);
		
		if(bu == null || !Cypher.process(pass).equals(bu.getPasswordHash())) {
			if(bu != null) {
				RegForm rf = new RegForm();
				rf.setNick(nick);
				req.setAttribute("rf", rf);
			}
			req.setAttribute("error", "Wrong paswword or nickname!");
			req.getRequestDispatcher("/WEB-INF/pages/Main.jsp").forward(req, resp);
            return;
		}
		req.getSession().setAttribute("current.user.id", bu.getId());
		req.getSession().setAttribute("current.user.fn", bu.getFirstName());
		req.getSession().setAttribute("current.user.ln", bu.getLastName());
		req.getSession().setAttribute("current.user.ni", bu.getNick());
		req.setAttribute("success", "DONE");
		resp.sendRedirect("main");
		}
	
	
}