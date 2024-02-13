package hr.fer.zemrsi.java.servlets;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/trigonometric")
public class TrigonometricServlet extends HttpServlet{

	 
    public static class Angle{
    	
    	private int angle;
    	private double sin,cos;
    	
		public int getAngle() {
			return angle;
		}

		public double getSin() {
			return sin;
		}

		public double getCos() {
			return cos;
		}

		public Angle(int angle) {
			super();
			this.angle = angle;
			this.sin = Math.sin(angle * Math.PI / 180);
			this.cos = Math.cos(angle * Math.PI / 180);
		}
    	
    	
    }
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		int a = 0;
		int b = 360;
		String a1 = req.getParameter("a");
		String b1 = req.getParameter("b");
		
		if (a1 != null) {
            try {
                a = Integer.parseInt(a1);
            } catch (NumberFormatException ignorable) {

            }
        }

        if (b1 != null) {
            try {
                b = Integer.parseInt(b1);
            } catch (NumberFormatException ignorable) {
            }
        }
        
        if(a>b) {
        	int c= a;
        	a = b;
        	b = c;
        }
        
        if(b > a+720) {
        	b = a+720;
        }
        
        List<Angle> angles = new LinkedList();
        for(int i = a; i < b;i++) {
        	angles.add(new Angle(i));
        }
        
        req.setAttribute("anglesTable", angles);
        req.getRequestDispatcher("WEB-INF/pages/trigonometric.jsp").forward(req, resp);
       
	}
}
