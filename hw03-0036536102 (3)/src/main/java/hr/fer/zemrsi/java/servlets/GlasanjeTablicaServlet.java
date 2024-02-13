package hr.fer.zemrsi.java.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import hr.fer.zemrsi.java.servlets.GlasanjeServlet.Band;

@WebServlet("/glasanje-xls")
public class GlasanjeTablicaServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
        
        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment;filename=results.xls");
        HttpSession session = req.getSession();
        List<Band> bendovi =  (List<Band>) session.getAttribute("bands");
        Map<String,String> rezultati = (Map<String,String>)session.getAttribute("results");
        Workbook powerBook = createBook(bendovi,rezultati);
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
    private Workbook createBook(List<Band> bendovi, Map<String,String> rezultati) {

    	HSSFWorkbook workbook = new HSSFWorkbook();
        	
        	HSSFSheet sheet = workbook.createSheet("Rezultati glasanja");
        	HSSFRow row = sheet.createRow(0);
            row.createCell(0).setCellValue("Bend");
            row.createCell(1).setCellValue("Broj glasova");
            
            int i = 1;
            for(Map.Entry<String, String> e : rezultati.entrySet()) { 
                String name = "";
                for(Band b : bendovi){
                  if(b.getId().equals(e.getKey())){
                      name = b.getName();
                  }
                } 
                row = sheet.createRow(i);
                i++;
                row.createCell(0).setCellValue(name);
                row.createCell(1).setCellValue(Integer.parseInt(e.getValue()));
            }  

        return workbook;


    }
}
