package hr.fer.zemrsi.java.servlets;

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

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.PieDataset;

@WebServlet("/glasanje")
public class GlasanjeServlet extends HttpServlet {
	
	public static class Band{
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
		
		
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		List<String> lines = Files.readAllLines(Paths.get(fileName));
		List<Band> band = new LinkedList<>();
		for(String line : lines) {
			String[] s = line.split("\\t");
			band.add(new Band(s[0],s[1],s[2]));
		}
		req.setAttribute("bands", band);
		HttpSession session = req.getSession();
        session.setAttribute("bands", band);
		req.getRequestDispatcher("WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
		
	}
}
