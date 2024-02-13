package hr.fer.zemrsi.java.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

import hr.fer.zemrsi.java.servlets.GlasanjeServlet.Band;

@WebServlet("/glasanje-grafika")
public class GlasanjeGrafikaServlet extends HttpServlet {

	HttpServletRequest req;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		 try {
		resp.setContentType("image/png");
		 this.req = req;
		 DefaultCategoryDataset dataset = createDataset(); 
	     JFreeChart chart = createChart(dataset, "Bar-chart");
		 OutputStream os = resp.getOutputStream();
		 ChartUtilities.writeChartAsPNG(os, chart, 1000, 1000);
		    os.close();
		 } catch (Exception e) {
		        // Log the exception
		        e.printStackTrace();
		        // You can also send the error message as a response
		        resp.setContentType("text/html");
		        resp.getWriter().println("<h1>Error:</h1><pre>" + e.getMessage() + "</pre>");
		    }
		
	}
	
	private  DefaultCategoryDataset createDataset() {
		DefaultCategoryDataset result = new DefaultCategoryDataset();
        
        HttpSession session = req.getSession();
        List<Band> bendovi =  (List<Band>) session.getAttribute("bands");
        Map<String,String> rezultati = (Map<String,String>)session.getAttribute("results");
        
   //     int a = rezultati.entrySet().stream().map((b)-> Integer.parseInt(b.getValue())).mapToInt(Integer::intValue).sum();
        
        
        for(Map.Entry<String, String> e : rezultati.entrySet()) { 
            String name = "";
            for(Band b : bendovi){
              if(b.getId().equals(e.getKey())){
                  name = b.getName();
              }
            } 
            result.setValue(Integer.parseInt(e.getValue()),name,"");
        }    
        return result;

	 
    }


    private JFreeChart createChart(DefaultCategoryDataset dataset, String title) {

        JFreeChart chart = ChartFactory.createBarChart(
        		title, //Chart Title  
                "Bands", // Category axis  
                "Votes", // Value axis  
                dataset,  
                PlotOrientation.VERTICAL,  
                true,true,false 
        );

       /* PiePlot3D plot = (PiePlot3D) chart.getPlot();
     //   plot.setStartAngle(289);
     //   plot.setDirection(Rotation.CLOCKWISE);*/
      //  plot.setForegroundAlpha(0.5f);
        return chart;

    }

}