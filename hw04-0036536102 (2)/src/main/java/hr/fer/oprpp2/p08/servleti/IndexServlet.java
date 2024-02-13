package hr.fer.oprpp2.p08.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.p08.dao.DAOProvider;
import hr.fer.oprpp2.p08.dao.sql.SQLDAO;

@WebServlet("/servleti/index.html")
public class IndexServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		SQLDAO dao = (SQLDAO) DAOProvider.getDao();
		
		req.setAttribute("polls", dao.getPolls());
		
		req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
		
	}
}

