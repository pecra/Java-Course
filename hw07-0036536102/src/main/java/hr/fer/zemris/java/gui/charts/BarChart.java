package hr.fer.zemris.java.gui.charts;

import java.util.List;
/**
 * Razred koji cuva podatke za izradu grafa.
 * @author Petra
 *
 */
public class BarChart {

	List<XYValue> lista;
	String opisX;
	String opisY;
	int minY,maxY,razmak;
	
	/**
	 * KOnstruktor.
	 * @param lista
	 * @param opisX
	 * @param opisY
	 * @param minY
	 * @param maxY
	 * @param razmak
	 */
	public BarChart(List<XYValue> lista,String opisX,String opisY,int minY,int maxY,int razmak) {
		this.lista = lista;
		this.opisX=opisX;
		this.opisY=opisY;
		if(minY < 0) {
			throw new IllegalArgumentException("premali y");
		}
		this.minY = minY;
		if(!(minY < maxY)) {
			throw new IllegalArgumentException("premali maxy");
		}
		this.maxY=maxY;
		this.razmak = razmak;
		if((maxY-minY)%razmak != 0) {
			for(int i = 0;i < maxY;i++) {
				if((maxY-minY+i)%razmak != 0) {
					this.minY = minY+i;
					break;
				}
			}
		}
		for(XYValue val : lista) {
			if(val.getY()< this.minY) {
				throw new IllegalArgumentException("premali neki y");
			}
		}
	}
}
