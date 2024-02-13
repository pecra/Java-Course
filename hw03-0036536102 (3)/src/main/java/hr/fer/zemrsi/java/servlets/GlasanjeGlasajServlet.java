package hr.fer.zemrsi.java.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/glasanje-glasaj")
public class GlasanjeGlasajServlet extends HttpServlet {

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
		String glas = req.getParameter("id");
		String p = null;
		try {
			p = String.valueOf(Integer.parseInt(results.get(glas))+1);
		} catch(Exception e) {
		}
		 
		results.put(glas, p);
		
		OutputStream os = Files.newOutputStream(Paths.get(fileName));
		
        StringBuilder sb = new StringBuilder();
        
        for (Entry<String, String> e:results.entrySet()) {
            sb.append(e.getKey()).append("\t").append(e.getValue()).append("\n");
        }
        sb.setLength(sb.length() - 1);
        os.write(sb.toString().getBytes(StandardCharsets.UTF_8));
		
		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
		
	}
}
