package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

import javax.swing.JComponent;
/**
 * Komponenta koja predstavlja graf.
 * @author Petra
 *
 */
public class BarChartComponent extends JComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private BarChart bc;
	
	public BarChartComponent(BarChart bc) {
		this.bc = bc;
		
	}
	
	/**
	 * Iscrtavanje komponente pomocu predanog graphics-a.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D gr = (Graphics2D) g;
		Insets ins = getInsets();
		Dimension dim = getSize();
		Rectangle r = new Rectangle(
				ins.left, 
				ins.top, 
				dim.width-ins.left-ins.right,
				dim.height-ins.top-ins.bottom);
		if(isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(r.x, r.y, r.width, r.height);
		}
		g.setColor(getForeground());
		
		FontMetrics fm = g.getFontMetrics();
		
		int w = fm.stringWidth(bc.opisX);
		int a = fm.getAscent();
		int d = fm.getDescent();
		int h = fm.getHeight();
		int space = 2;
		int wipsilona = fm.stringWidth(String.valueOf(bc.maxY));//sirina najveceg bropja y
		int lenY = dim.height-ins.bottom-2*h-4-ins.top-10;
		int widthX = -(ins.left+2*space+wipsilona+h)+( dim.width-ins.right-15);
		
		gr.drawString(bc.opisX,(ins.left+2*space+wipsilona+h+ ((widthX)-w)/2), dim.height-d);//opisx nacrtaj
		
		int[] prvi = new int[3];
		prvi[0] = ins.left+2*space+wipsilona+h-3;
		prvi[1] = ins.left+2*space+wipsilona+h+3;
		prvi[2] = ins.left+2*space+wipsilona+h;
		int[] drugi = new int[3];
		drugi[0] =  ins.top+10;
		drugi[1] =  ins.top+10;
		drugi[2] =  ins.top+10-6;
		gr.fillPolygon(prvi, drugi, 3);
		
		prvi[0] = dim.width-ins.right-10;
		prvi[1] = dim.width-ins.right-10;
		prvi[2] = dim.width-ins.right-10+6;
		drugi[0] =  dim.height-ins.bottom-2*h-2*space-3;
		drugi[1] =  dim.height-ins.bottom-2*h-2*space+3;
		drugi[2] =  dim.height-ins.bottom-2*h-2*space;
		gr.fillPolygon(prvi, drugi, 3);
		
		gr.drawLine(ins.left+2*space+wipsilona+h, dim.height-ins.bottom-2*h-2*space, dim.width-ins.right-10, dim.height-ins.bottom-2*h-2*space);//nacrtaj x
		
		double da = widthX/bc.lista.size();
		int i = 0;

		for(XYValue v : bc.lista) {
			gr.drawString(String.valueOf(v.getX()), ins.left+2*space+wipsilona+h+ Math.round(i*da+da/2-fm.stringWidth(String.valueOf(v.getX()))), dim.height-ins.bottom-h-space);
			g.setColor(Color.ORANGE);
			gr.fillRect((int)(ins.left+2*space+wipsilona+h+ Math.round(i*da)),(int)((lenY+10)-(bc.lista.get(i).y-bc.minY)*(lenY/(bc.maxY-bc.minY))),
	        		(int)( Math.round(da)), (bc.lista.get(i).y-bc.minY)*(lenY/(bc.maxY-bc.minY)));
			gr.draw3DRect((int)(ins.left+2*space+wipsilona+h+ Math.round(i*da)),(int)((lenY+10)-(bc.lista.get(i).y-bc.minY)*(lenY/(bc.maxY-bc.minY))),
	        		(int)( Math.round(da)), (bc.lista.get(i).y-bc.minY)*(lenY/(bc.maxY-bc.minY)), true);
			i++;
			g.setColor(Color.DARK_GRAY);
		}
		for(int e = bc.minY; e < bc.maxY+bc.razmak;e = e + bc.razmak) {
			gr.drawLine(ins.left+2*space+wipsilona+h-2,((lenY+10)-(e-bc.minY)*(lenY/(bc.maxY-bc.minY))),ins.left+2*space+wipsilona+h+2,((lenY+10)-(e-bc.minY)*(lenY/(bc.maxY-bc.minY))));
			gr.drawString(String.valueOf(e), ins.left+h+space-1+wipsilona-fm.stringWidth(String.valueOf(e)), ((lenY+10)-(e-bc.minY)*(lenY/(bc.maxY-bc.minY)))+fm.getAscent()/2);
		}
		gr.drawLine(ins.left+2*space+wipsilona+h, ins.top+10,ins.left+2*space+wipsilona+h, dim.height-ins.bottom-2*h-4);
		
		AffineTransform at = new AffineTransform();
		at.rotate(-Math.PI / 2);
		gr.setTransform(at);
		gr.drawString(bc.opisY,-((dim.height)-(bc.maxY-bc.minY)/2/bc.razmak*(lenY/(bc.maxY-bc.minY))-space*2-2*h-fm.getAscent()/2),h);

		
		
	}
}
