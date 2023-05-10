package hr.fer.zemris.java.gui.calc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.CalcLayoutException;
import hr.fer.zemris.java.gui.layouts.RCPosition;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class CalcTest {
	
	JFrame frame = new JFrame();
	Container cp = frame.getContentPane();
	//cp.setLayout(new CalcLayout(3));
//	cp.add(l("tekst 1"), new RCPosition(1,1));
	Container cp1 = new Container();

	@Test
	public void RCPositiontest1() {
		cp.setLayout(new CalcLayout(3));
		assertThrows(CalcLayoutException.class,()-> cp.add(new JLabel("tekst 1"), new RCPosition(0,1))); 
	}
	@Test
	public void RCPositiontest2() {
		cp.setLayout(new CalcLayout(3));
		assertThrows(CalcLayoutException.class,()-> cp.add(new JLabel("tekst 1"), new RCPosition(6,1))); 
	}
	@Test
	public void RCPositiontest3() {
		cp.setLayout(new CalcLayout(3));
		assertThrows(CalcLayoutException.class,()-> cp.add(new JLabel("tekst 1"), new RCPosition(1,0))); 
	}
	@Test
	public void RCPositiontest4() {
		cp.setLayout(new CalcLayout(3));
		assertThrows(CalcLayoutException.class,()-> cp.add(new JLabel("tekst 1"), new RCPosition(1,8))); 
	}
	@Test
	public void RCPositiontest5() {
		cp.setLayout(new CalcLayout(3));
		cp.add(new JLabel("tekst 1"), new RCPosition(1,6));
		assertThrows(CalcLayoutException.class,()-> cp.add(new JLabel("tekst 1"), new RCPosition(1,6))); 
	}
}
