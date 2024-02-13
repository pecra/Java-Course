package hr.fer.zemrsi.java.servlets;

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

@WebServlet("/glasanje-rezultati")
public class GlasanjeRezultatiServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		
		if (!Files.exists(Paths.get(fileName))) {
            Files.createFile(Paths.get(fileName));
        }
		
		List<String> lines = Files.readAllLines(Paths.get(fileName));
		Map<String,String> results = new HashMap<>();
		for(String line : lines) {
			String[] s = line.split("\\t");
			results.put(s[0], s[1]);
		}
		Map<String, String> sortedMap = results.entrySet()
                .stream()
                .sorted(Map.Entry.<String, String>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		req.setAttribute("results", sortedMap);
		HttpSession session = req.getSession();
        req.setAttribute("bands", session.getAttribute("bands"));
        session.setAttribute("results", sortedMap);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);

		
	}
}

