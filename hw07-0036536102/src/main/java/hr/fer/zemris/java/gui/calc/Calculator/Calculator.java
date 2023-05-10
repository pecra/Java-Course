package hr.fer.zemris.java.gui.calc.Calculator;

import java.awt.Color;
import java.awt.Container;
import java.util.Stack;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;
/**
 * Razred koji predstavlja Kalkulator.
 * @author Petra
 *
 */
public class Calculator extends JFrame{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Konsturktor
	 */
	public Calculator() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(500, 500);
		initGUI();
		}
	/**
	 * GUI
	 */
	private void initGUI() {
		
		Stack<Double> stack = new Stack<>();
		Container cp = getContentPane();
		cp.setLayout(new CalcLayout(3));
		CalcModel model = new CalcModelImpl();
		JLabel dis = new JLabel(model.toString(),JLabel.RIGHT);
		dis.setFont(dis.getFont().deriveFont(30f));
		dis.setOpaque(true);
		dis.setBackground(Color.yellow);
		dis.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		cp.add(dis, new RCPosition(1,1));
		JButton equals = l("=");
		cp.add(equals, new RCPosition(1,6));
		JButton clr = l("clr");
		cp.add(clr, new RCPosition(1,7));
		JButton divByX = l("1/x");
		
		cp.add(divByX, new RCPosition(2,1));
		
		
		JButton sin = l("sin");
		cp.add(sin, new RCPosition(2,2));
		JButton seven = l("7");
		seven.setFont(seven.getFont().deriveFont(30f));
		cp.add(seven, new RCPosition(2,3));
		JButton eight = l("8");
		eight.setFont(eight.getFont().deriveFont(30f));
		cp.add(eight, new RCPosition(2,4));
		JButton nine = l("9");
		nine.setFont(nine.getFont().deriveFont(30f));
		cp.add(nine, new RCPosition(2,5));
		JButton divide = l("/");
		cp.add(divide, new RCPosition(2,6));
		JButton reset = l("reset");
		cp.add(reset, new RCPosition(2,7));
		
		JButton log = l("log");
		cp.add(log, new RCPosition(3,1));
		JButton cos = l("cos");
		cp.add(cos, new RCPosition(3,2));
		JButton four = l("4");
		four.setFont(four.getFont().deriveFont(30f));
		cp.add(four, new RCPosition(3,3));
		JButton five = l("5");
		five.setFont(five.getFont().deriveFont(30f));
		cp.add(five, new RCPosition(3,4));
		JButton six = l("6");
		six.setFont(six.getFont().deriveFont(30f));
		cp.add(six, new RCPosition(3,5));
		JButton mul = l("*");
		cp.add(mul, new RCPosition(3,6));
		JButton push = l("push");
		cp.add(push, new RCPosition(3,7));
		
		JButton ln = l("ln");
		cp.add(ln, new RCPosition(4,1));
		JButton tan = l("tan");
		cp.add(tan, new RCPosition(4,2));
		JButton one = l("1");
		one.setFont(one.getFont().deriveFont(30f));
		cp.add(one, new RCPosition(4,3));
		JButton two = l("2");
		two.setFont(two.getFont().deriveFont(30f));
		cp.add(two, new RCPosition(4,4));
		JButton three = l("3");
		three.setFont(three.getFont().deriveFont(30f));
		cp.add(three, new RCPosition(4,5));
		JButton sub = l("-");
		cp.add(sub, new RCPosition(4,6));
		JButton pop = l("pop");
		cp.add(pop, new RCPosition(4,7));
		
		JButton pow = l("x^n");
		cp.add(pow, new RCPosition(5,1));
		JButton ctg = l("ctg");
		cp.add(ctg, new RCPosition(5,2));
		JButton zero = l("0");
		zero.setFont(zero.getFont().deriveFont(30f));
		cp.add(zero, new RCPosition(5,3));
		JButton sign = l("+/-");
		cp.add(sign, new RCPosition(5,4));
		JButton point = l(".");
		cp.add(point, new RCPosition(5,5));
		JButton plus = l("+");
		cp.add(plus, new RCPosition(5,6));
		JCheckBox inv = new JCheckBox("Inv");
		cp.add(inv, new RCPosition(5,7));
		
		model.addCalcValueListener((t)->dis.setText(t.toString()));
		
		this.addNumber(zero, 0, model);
		this.addNumber(one, 1, model);
		this.addNumber(two, 2, model);
		this.addNumber(three, 3, model);
		this.addNumber(four, 4, model);
		this.addNumber(five, 5, model);
		this.addNumber(six, 6, model);
		this.addNumber(seven, 7, model);
		this.addNumber(eight, 8, model);
		this.addNumber(nine, 9, model);
		
		point.addActionListener(e -> {
			model.insertDecimalPoint();
		});
		
		push.addActionListener(e -> {
			stack.push(model.getValue());
		});
		
		pop.addActionListener(e -> {
			double d = stack.pop();
			model.setValue(d);
		});
		
		this.setBin(sub,(a,b)->a-b, model);
		this.setBin(plus,(a,b)->a+b, model);
		this.setBin(mul,(a,b)->a*b, model);
		this.setBin(divide,(a,b)->a/b, model);
		
		inv.addActionListener(e -> {
			String s = pow.getText();
			if(pow.getActionListeners().length > 0) {
				pow.removeActionListener(pow.getActionListeners()[0]);
			}
			if(s.equals("x^n")) {
				pow.setText("x^(1/n)");
				this.setBin(pow,(a,b)->Math.pow(a,1/b) , model);
			}
			else {
				pow.setText("x^n");
				this.setBin(pow, Math::pow, model);
			}
		});
		
		sign.addActionListener(e -> {
			model.swapSign();
		});
		equals.addActionListener(e -> {
			double result = model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue());
			model.setValue(result);
			model.clearActiveOperand();
			model.setPendingBinaryOperation(null);
		});
		clr.addActionListener(e -> {
			model.clear();
			((CalcModelImpl) model).freezeValue("");
		});
		reset.addActionListener(e -> {
			model.clearAll();
			stack.clear();
			((CalcModelImpl) model).freezeValue("");
		});
		this.setUn(divByX,(a)-> 1/a, model);
		this.setUn(log, Math::log10, model);
		this.setUn(ln, Math::log, model);
		this.setUn(sin, Math::sin, model);
		this.setUn(cos, Math::cos, model);
		this.setUn(tan, Math::tan, model);
		this.setUn(ctg,(a)-> 1/(Math.tan(a)), model);
		this.addInv(inv,"10^x","log", log, Math::log10, model, (t)->Math.pow(10, t));
		this.addInv(inv,"e^x","ln", ln, Math::log, model, (t)->Math.pow(Math.E, t));		
		this.addInv(inv,"arcsin","sin", sin, Math::sin, model, Math::asin);
		this.addInv(inv,"arccos","cos", cos, Math::cos, model, Math::acos);
		this.addInv(inv,"arctan","tan", tan, Math::tan, model, Math::atan);
		this.addInv(inv,"arcctg","ctg", ctg,(a)-> 1/(Math.tan(a)), model, (a)->  Math.PI / 2 - Math.atan(a));
		
	}

	/**
	 * Dodavanje action listenera za tipku koja prebacuje kalkulator
	 * u drugi mode.
	 * @param inv
	 * @param prvi
	 * @param drugi
	 * @param button
	 * @param op
	 * @param model
	 * @param op2
	 */
	private void addInv(JCheckBox inv,String prvi,String drugi,
			JButton button,DoubleUnaryOperator op,CalcModel model,DoubleUnaryOperator op2) {
		inv.addActionListener(e -> {
			String s = button.getText();
			if(button.getActionListeners().length > 0) {
				button.removeActionListener(button.getActionListeners()[0]);
			}
			if(s.equals(prvi)) {
				button.setText(drugi);
				this.setUn(button,op, model);
			}
			else {
				button.setText(prvi);
				this.setUn(button,op2, model);
			}
		});
		
	}
	/**
	 * Dodavanje action listenera za brojeve na kalkulatoru.
	 * @param button
	 * @param i
	 * @param model
	 */
	private void addNumber(JButton button,Integer i,CalcModel model) {
		button.addActionListener(e -> {
			model.insertDigit(i);
		});
	}
	/**
	 * Dodavanje action listenera za unarne operavije.
	 * @param button
	 * @param op
	 * @param model
	 */
	private void setUn(JButton button,DoubleUnaryOperator op,CalcModel model) {
		button.addActionListener(e -> {
			double result = 0;
			if(!(model.getPendingBinaryOperation()==null)) {
				result = model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue());
				result = op.applyAsDouble(result);
				
			}
			else {
				if(model.isActiveOperandSet()) {
					result = op.applyAsDouble(model.getActiveOperand());
				}
				else {
					result = op.applyAsDouble(model.getValue());
				}
				
			}
			model.setActiveOperand(result);
			((CalcModelImpl) model).freezeValue(String.valueOf(result));
			model.setPendingBinaryOperation(null);
			model.clear();
		});
	}
	/**
	 * Dodavanje action listenera za binarne operavije.
	 * @param button
	 * @param op
	 * @param model
	 */
	private void setBin(JButton button,DoubleBinaryOperator op, CalcModel model) {
		button.addActionListener(e -> {
			if(!(model.getPendingBinaryOperation()==null)) {
				double result = model.getPendingBinaryOperation().applyAsDouble(model.getActiveOperand(), model.getValue());
				model.setActiveOperand(result);
				((CalcModelImpl) model).freezeValue(String.valueOf(result));
				
			}
			else {
				if(!model.isActiveOperandSet()) {
	                model.setActiveOperand(model.getValue());
				}
			}
			model.setPendingBinaryOperation(op);
			model.clear();
		});
	}
	/**
	 * Stavarnje novog JButtona.
	 * @param text
	 * @return
	 */
	private JButton l(String text) {
		JButton l = new JButton(text);
		l.setBackground(new Color(175,175,225));
		l.setOpaque(true);
		l.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
		return l;
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(()->{new Calculator().setVisible(true);});
		}

}
