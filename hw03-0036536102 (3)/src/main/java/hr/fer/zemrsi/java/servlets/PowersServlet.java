package hr.fer.zemrsi.java.servlets;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

@WebServlet("/powers")
public class PowersServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		int a = 0,b = 0,n = 1;
		String a1 = req.getParameter("a");
		String b1 = req.getParameter("b");
		String n1 = req.getParameter("n");
		
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
        
        if (n1 != null) {
            try {
                n = Integer.parseInt(n1);
            } catch (NumberFormatException ignorable) {
            }
        }
        
        if(a < -100 || a > 100) {
        	req.setAttribute("err", "Var a is invalid!");
        	req.getRequestDispatcher("err.jsp").forward(req, resp);
        }
        if(b < -100 || b > 100) {
        	req.setAttribute("err", "Var b is invalid!");
        	req.getRequestDispatcher("err.jsp").forward(req, resp);
        }
        if(n < 1 || n > 5) {
        	req.setAttribute("err", "Var n is invalid!");
        	req.getRequestDispatcher("err.jsp").forward(req, resp);
        }
        
        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment;filename=powers.xls");
        
        Workbook powerBook = createPowerBook(a, b, n);
        powerBook.write(resp.getOutputStream());


    }

    /**
     * This method is used for creating spreadsheet.
     *
     * @param a From number
     * @param b To number
     * @param n Power
     * @return Spreadsheet
     */
    private Workbook createPowerBook(int a, int b, int n) {

    	HSSFWorkbook workbook = new HSSFWorkbook();

        for (int i = 1; i <= n; i++) {
        	
        	HSSFSheet sheet = workbook.createSheet(i + ". power");
        	HSSFRow row = sheet.createRow(0);
            row.createCell(0).setCellValue("Number");
            row.createCell(1).setCellValue(i + ". power");
            
            for (int j = a, rowIndex = 1; j <= b; j++, rowIndex++) {
                row = sheet.createRow(rowIndex);
                row.createCell(0).setCellValue(j);
                row.createCell(1).setCellValue(Math.pow(j, i));
            }
        }

        return workbook;


    }
}

