package hr.fer.oprpp2.p08.servleti;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import hr.fer.oprpp2.p08.Data;
import hr.fer.oprpp2.p08.dao.DAOProvider;
//import hr.fer.oprpp2.p08.servleti.GlasanjeServlet.Band;

@WebServlet("/servleti/glasanje-xls")
public class GlasanjeTablicaServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment;filename=rezultati.xls");

        String p = req.getParameter("pollID");

            try {
                List<Data> data = DAOProvider.getDao().getData(p);
                Workbook powerBook = createResultsBook(data);
                powerBook.write(resp.getOutputStream());
            } catch (NumberFormatException ignorable) {
        }


    }
	
    private Workbook createResultsBook(List<Data> data) {

        Workbook workbook = new HSSFWorkbook();

        Sheet sheet = workbook.createSheet("Rezultati");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("N");
        row.createCell(1).setCellValue("Broj glasova");
        int rowNumber = 1;
        
        for (Data d : data) {
            row = sheet.createRow(rowNumber++);
            row.createCell(0).setCellValue(d.getTitle());
            row.createCell(1).setCellValue(Integer.parseInt(d.getVotes()));
        }

        return workbook;


    }
}
