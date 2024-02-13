package hr.fer.zemrsi.java.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/setcolor")
public class ColorServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String bgCol = req.getParameter("color");
		
		if(bgCol != null) {
			req.getSession().setAttribute("pickedBgCol", bgCol);
		}
		req.getRequestDispatcher("/colors.jsp").forward(req, resp);
	}

}
