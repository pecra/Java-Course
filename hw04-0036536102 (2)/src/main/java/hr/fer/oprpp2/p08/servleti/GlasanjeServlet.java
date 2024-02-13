package hr.fer.oprpp2.p08.servleti;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import hr.fer.oprpp2.p08.dao.DAOProvider;
import hr.fer.oprpp2.p08.dao.sql.SQLDAO;


@WebServlet("/servleti/glasanje")
public class GlasanjeServlet extends HttpServlet {
	
	/*public static class Band{
		private String id;
		private String name;
		private String link;
		
		public Band(String id,String name,String link) {
			this.id = id;
			this.name = name;
			this.link = link;
		}

		public String getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public String getLink() {
			return link;
		}
		
		
	}*/

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
        SQLDAO dao = (SQLDAO) DAOProvider.getDao();
		
		req.setAttribute("data", dao.getData(req.getParameter("pollID")));
		req.setAttribute("pollID", req.getParameter("pollID"));
		
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
		
	}
}
