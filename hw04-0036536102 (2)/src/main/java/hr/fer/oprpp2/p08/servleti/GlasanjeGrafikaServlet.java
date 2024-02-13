
package hr.fer.oprpp2.p08.servleti;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.util.Rotation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.p08.Data;
import hr.fer.oprpp2.p08.dao.DAOProvider;

@WebServlet("/servleti/glasanje-grafika")
public class GlasanjeGrafikaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("image/png");

        String pollId = req.getParameter("pollID");

        try {
                List<Data> results = DAOProvider.getDao().getData(pollId);
                PieDataset dataset = createDataset(results);
                JFreeChart chart = createChart(dataset, "Rezultati");

                resp.getOutputStream().write(ChartUtils.encodeAsPNG(chart.createBufferedImage(500, 500)));
            } catch (NumberFormatException ignorable) {
        }

        


    }

    private PieDataset createDataset(List<Data> data) {
    	
        DefaultPieDataset dataset = new DefaultPieDataset();

        for (Data d : data) {
            dataset.setValue(d.getTitle(), Integer.parseInt(d.getVotes()));
        }
        return dataset;

    }

    private JFreeChart createChart(PieDataset dataset, String title) {

        JFreeChart chart = ChartFactory.createPieChart3D(
                title,                  
                dataset,                
                true,                  
                true,
                false
        );
        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(289);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        
        return chart;

    }

}
