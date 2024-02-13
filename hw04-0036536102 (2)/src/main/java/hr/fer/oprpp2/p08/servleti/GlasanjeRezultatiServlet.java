package hr.fer.oprpp2.p08.servleti;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import hr.fer.oprpp2.p08.Data;
import hr.fer.oprpp2.p08.dao.DAOProvider;
import hr.fer.oprpp2.p08.dao.sql.SQLDAO;

@WebServlet("/servleti/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SQLDAO dao = (SQLDAO) DAOProvider.getDao();
		List<Data> l = dao.getData(req.getParameter("pollID"));
		l.sort((a,b) -> b.getVotes().compareTo(a.getVotes()));
		req.setAttribute("data", l);
		req.setAttribute("pollID", req.getParameter("pollID"));
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);

		
	}
}

