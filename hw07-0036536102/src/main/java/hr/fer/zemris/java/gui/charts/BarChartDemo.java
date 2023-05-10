package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
/**
 * Razred koji iscrtava graf za predane podatke.
 * @author Petra
 *
 */
public class BarChartDemo extends JFrame {

	private static final long serialVersionUID = 1L;
/**
 * Konstruktor.
 * @param ba
 * @param naslov
 */
	public BarChartDemo(BarChartComponent ba,String naslov) {
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocation(0,0);
		setSize(500, 500);
		initGUI(ba,naslov);
		
	}
	/**
	 * GUI.
	 * @param ba
	 * @param naslov
	 */
	private void initGUI(BarChartComponent ba,String naslov) {
		
		
		JComponent komponenta1 = ba;
		JLabel labela = new JLabel(naslov);
		labela.setHorizontalAlignment(SwingConstants.CENTER);
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(komponenta1, BorderLayout.CENTER);
		cp.add(labela, BorderLayout.NORTH);
		komponenta1.setOpaque(true);
		komponenta1.setBackground(Color.WHITE);
		
		getContentPane().add(komponenta1);
	}

	
	public static void main(String[] args) {
		String name = "";
		String name2 ="";
		String[] str;
		List<XYValue> arr=null;
		int min=0,max=0,red=0;
		try {
			 List<String> lines = Files.readAllLines(
					 Paths.get(args[0]),
					 StandardCharsets.UTF_8
					);
			 name = lines.get(0);
			 name2 = lines.get(1);
			 str = lines.get(2).split("\\s+");
             arr = new ArrayList<>();
			 for(int i = 0,j = str.length; i < j;i++) {
				 String[] s = str[i].split(",");
				 arr.add(i, new XYValue(Integer.valueOf(s[0]),Integer.valueOf(s[1]))); 
			 }
			 min = Integer.valueOf(lines.get(3));
			 max = Integer.valueOf(lines.get(4));
			 red = Integer.valueOf(lines.get(5));
		} catch (IOException e) {
		}
		
		BarChart model = new BarChart(
				 arr,name,name2,min,max,red
				);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				BarChartDemo prozor = new BarChartDemo(new BarChartComponent(model),args[0]);
				prozor.setVisible(true);
			}
		});
	}
}

