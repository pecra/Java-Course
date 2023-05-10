package hr.fer.zemris.java.gui.layouts;
/**
 * Razred koji predstavlja implementaciju layout managera.
 */
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

import javax.swing.JComponent;
import javax.swing.JLabel;

public class CalcLayout implements LayoutManager2{
	
	Dimension prefComp = new Dimension(0,0);
	Dimension maxComp;
	Dimension minComp = new Dimension(0,0);
	int gap = 0;
	final int maxBrojKomponenti = 31;
	List<JComponent> listaComp = new ArrayList<>();
	List<RCPosition> listaRCP = new ArrayList<>();
	
	/**
	 * konstruktor
	 * @param broj
	 */
	public CalcLayout(int broj) {
		this.gap = broj;
	}
	
	public CalcLayout() {
		super();
	}

	/**
	 * Dodavanje komponente u layout.
	 */
	public void addLayoutComponent(String name, Component comp) {
		throw new UnsupportedOperationException();
		
	}

	/**
	 * Uklanjanje komponente iz layouta.
	 */
	public void removeLayoutComponent(Component comp) {
		int index = this.listaComp.indexOf(comp);
		this.listaComp.remove(index);
		this.listaRCP.remove(index);
		
	}

	/**
	 * Preferirana velicina cijele komponenta koja koristi layout.
	 */
	public Dimension preferredLayoutSize(Container parent) {
		return this.calculate(this.prefComp,this.gap);
	}
	/**
	 * Minimalna velicina cijele komponenta kojakoristi layout.
	 */
	public Dimension minimumLayoutSize(Container parent) {
		return this.calculate(this.minComp,this.gap);
	}
	/**
	 * Dodavanje parent containera za komponente.
	 */
	public void layoutContainer(Container parent) {
		Dimension contDim = parent.getSize();
		Insets insets = parent.getInsets();
		int pheight = contDim.height-insets.top-insets.bottom;
		int pwidth = contDim.width-insets.left-insets.right;
		int cwidth;
		int cheight;
		boolean neparnoW = false;
		boolean neparnoH = false;
		if(((pwidth-6*this.gap)*1.0/(7*1.0))-((int)((pwidth-6*this.gap)/(7*1.0))) >= 0.5) {
			cwidth = (int) Math.round(((pwidth-6*this.gap)/(7*1.0)));
			neparnoW = true;

		}
		else {
			cwidth = (int) Math.round(((pwidth-6*this.gap)/(7*1.0)));
		}
		if(((pheight-4*this.gap)*1.0/(5*1.0))-((int)((pheight-4*this.gap)/(5*1.0))) >= 0.5) {
			cheight = (int) Math.round(((pheight-4*this.gap)/(5*1.0)));
			neparnoH = true;
		}
		else {
			cheight = (int) Math.round(((pheight-4*this.gap)/(5*1.0)));
		}
		for(int i = 0,j = this.listaComp.size();i<j;i++) {
			JComponent c = listaComp.get(i);
			RCPosition p = this.listaRCP.get(i);
			
			int x,y,width,height;
			int col = p.getColumn();
			int row = p.getRow();
			if(col == 1 && row == 1) {
				x = insets.left;
				if(neparnoW) {
					width = cwidth*3+2*(cwidth-1)+4*this.gap;
				}
				else {
					width = cwidth*5+4*this.gap;
				}
			}
			else {
				if(!neparnoW) {//ako se ne izmjenjuju velicine
					x = insets.left + (col-1)*this.gap + (col-1)*cwidth;
					width = cwidth;
				}
				else {
					if(col%2 == 0) {//ako se izmjenjuju velicine i parni je column
						x = insets.left + (col-1)*this.gap + (col/2)*cwidth + (col/2 - 1)*(cwidth-1);
						width = cwidth-1;
					}
					else {//ako se izmjenjuju velicine i neparni je column
						x = insets.left + (col-1)*this.gap + (col/2)*cwidth + (col/2)*(cwidth-1);
						width = cwidth;
					}	
				}
			}
			if(!neparnoH) {//ako se ne izmjenjuju velicine
				y = insets.top + (row-1)*this.gap + (row-1)*cheight;
				height = cheight;
			}
			else {
				if(row%2 == 0) {//ako se izmjenjuju velicine i parni je row
					y = insets.top + (row-1)*this.gap + (row/2)*cheight + (row/2 - 1)*(cheight-1);
					height = cheight-1;
				}
				else {//ako se izmjenjuju velicine i neparni je row
					y = insets.top + (row-1)*this.gap + (row/2)*cheight + (row/2)*(cheight-1);
					height = cheight;
				}	
			}
			c.setBounds(x, y, width, height);
		}
		
	}

	/**
	 * Dodavanje komponente sa ogranicenjima.
	 */
	public void addLayoutComponent(Component comp, Object constraints) {
		RCPosition rcp;
		if(comp.equals(null) || constraints.equals(null)) {
			throw new NullPointerException("predana null komponenta");
		}
		if(constraints.getClass() != String.class && 
				constraints.getClass() != RCPosition.class) {
			throw new IllegalArgumentException();
		}
		if(constraints.getClass() == String.class) {
			rcp = RCPosition.parse(String.valueOf(constraints));
		}
		else {
			rcp = (RCPosition) constraints;
		}
		if(this.listaRCP.contains(rcp) ||
				rcp.getRow() < 1 ||
				rcp.getRow() > 5 ||
				rcp.getColumn() < 1 ||
				rcp.getColumn() > 7 ||
				(rcp.getColumn() > 1 &&
						rcp.getColumn() < 6 && rcp.getRow() == 1)||
				this.listaComp.size() == maxBrojKomponenti) {
			throw new CalcLayoutException("neispravno ogranicenje");
		}
		this.listaComp.add((JComponent) comp);
		this.listaRCP.add(rcp);
		if(comp.isPreferredSizeSet()) {
			if(rcp.getColumn()== 1 && rcp.getRow()==1) {
				Dimension replace = new Dimension((comp.getPreferredSize().width-4*this.gap)/5,comp.getPreferredSize().height);
				this.prefComp = this.doSettings(replace,this.prefComp,(a,b) -> a>b);
			}
			else {
				this.prefComp = this.doSettings(comp.getPreferredSize(),this.prefComp,(a,b) -> a>b);
			}
		}
		if(comp.isMinimumSizeSet()) {
		    this.minComp = this.doSettings(comp.getMinimumSize(),this.minComp,(a,b) -> a>b);
		}
		if(comp.isMaximumSizeSet()) {
			if(this.maxComp == null) {
				this.maxComp = comp.getMaximumSize();
			}
			else {
				this.maxComp = this.doSettings(comp.getMaximumSize(),this.maxComp,(a,b) -> a<b);
			}
		}
		
	}

	/**
	 * Pomocna metoda za zamjenu max,min,preferred velicine prilikom dodavanja nove komponente.
	 * @param preferredSize
	 * @param prefComp2
	 * @param object
	 * @return
	 */
	private Dimension doSettings(Dimension preferredSize, Dimension prefComp2, BiPredicate<Integer,Integer> object) {
		int h = prefComp2.height;
		int w = prefComp2.width;
		if(object.test(preferredSize.height, h)) {
			h = preferredSize.height;
		}
		if(object.test(preferredSize.width, w)) {
			w = preferredSize.width;
		}
		return new Dimension(w,h);
	}

	/**
	 * Maksimalna velicina cijele komponenta kojakoristi layout.
	 */
	public Dimension maximumLayoutSize(Container target) {
		return this.calculate(this.maxComp,this.gap);
	}

	/**
	 * Izracun makismalne velicine cijelog parent containera.
	 * @param maxComp2
	 * @param gap2
	 * @return
	 */
	private Dimension calculate(Dimension maxComp2, int gap2) {
		return new Dimension(maxComp2.width*7+6*gap2,maxComp2.height*5+4*gap2);
	}

	
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	public void invalidateLayout(Container target) {
		// TODO Auto-generated method stub
		
	}

}
