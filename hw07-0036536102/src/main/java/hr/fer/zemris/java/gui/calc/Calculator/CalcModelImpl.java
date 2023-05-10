package hr.fer.zemris.java.gui.calc.Calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;
/**
 * Model kalkulatora.
 * @author Petra
 *
 */
public class CalcModelImpl implements CalcModel{
	
	boolean editable = true;
	boolean positive = true;
	String znamenke = "";
	double decim = 0;
	String zamrznuto = null;
	double activeOperand;
	boolean hasActiveOperand = false;
	DoubleBinaryOperator pendingOperation = null;
	private List<CalcValueListener> promatraci = new ArrayList<>();

	@Override
	public void addCalcValueListener(CalcValueListener l) {
		if(l== null) {
			throw new NullPointerException();
		}
		promatraci.add(l);
		
	}

	@Override
	public void removeCalcValueListener(CalcValueListener l) {
		if(l.equals(null)) {
			throw new NullPointerException();
		}
		promatraci.remove(l);
		
	}

	@Override
	public double getValue() {
		return this.decim;
	}

	@Override
	public void setValue(double value) {
		try {
			this.znamenke = String.valueOf(value);
		} catch(Exception e) {}
		if(value == Double.POSITIVE_INFINITY) {
			this.znamenke = String.valueOf("Infinity");
		}
		if(value == Double.NEGATIVE_INFINITY) {
			this.positive = false;
			this.znamenke = String.valueOf("-Infinity");
		}
		if(value == Double.NaN) {
			this.znamenke = String.valueOf("NaN");
		}
		this.decim = value;
		this.editable = false;
		this.freezeValue(znamenke);
		for(CalcValueListener l : promatraci) {
			l.valueChanged(this);
		}
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	@Override
	public void clear() {
		this.positive=true;
		this.decim = 0;
		this.znamenke = "";
		for(CalcValueListener l : promatraci) {
			l.valueChanged(this);
		}
		this.editable = true;
		
	}

	@Override
	public void clearAll() {
		this.clear();
		this.clearActiveOperand();
		this.setPendingBinaryOperation(null);
		
	}

	@Override
	public void swapSign() throws CalculatorInputException {
		if(!editable) {
			throw new CalculatorInputException("nije editabilno");
		}
		if(this.znamenke == "") {
			this.znamenke = "0";
		}
		if(this.positive) {
			this.znamenke = "-" + this.znamenke;
			this.decim = this.decim * (-1);
		}
		else {
			this.znamenke =this.znamenke.substring(1);
			this.decim = this.decim * (-1);
		}
		this.positive = !this.positive;
		this.freezeValue(znamenke);
		for(CalcValueListener l : promatraci) {
			l.valueChanged(this);
		}
		
	}

	@Override
	public void insertDecimalPoint() throws CalculatorInputException {
		if(!editable) {
			throw new CalculatorInputException("nije editabilno");
		}
		if(this.znamenke.equals("-0")) {
			throw new CalculatorInputException();
		}
		if(this.znamenke.contains(".")) {
			throw new CalculatorInputException("dec tocka vec postoji");
		}
		if(this.znamenke.equals("")) {
			throw new CalculatorInputException("dec tocka vec postoji");
		}
		this.znamenke = this.znamenke + ".";
		this.zamrznuto = null;
		this.freezeValue(znamenke);
		for(CalcValueListener l : promatraci) {
			l.valueChanged(this);
		}
		
	}

	@Override
	public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
		if(!editable) {
			throw new CalculatorInputException("nije editabilno");
		}
		if(digit < 0 || digit > 9) {
			throw new IllegalArgumentException();
		}
		try {
			if(!znamenke.equals("")) {
				if(decim==0.0000000) {
					if(digit == 0.00000000) {
							if(znamenke.contains(".")) {
								String s = znamenke + String.valueOf(digit);
								decim = Double.valueOf(s);
								znamenke = s;
							}
					}
					else {
						if(znamenke.contains(".")) {
							String s = znamenke + String.valueOf(digit);
							decim = Double.valueOf(s);
							znamenke = s;
						}
						else {
                            znamenke = "";
							String s = znamenke + String.valueOf(digit);
							decim = Double.valueOf(s);
							znamenke = s;
						}
					}
				}
				else {
					String s = znamenke + String.valueOf(digit);
					decim = Double.valueOf(s);
					znamenke = s;
					if(this.getValue() >= 1.0E308) {
						throw new CalculatorInputException();
					}
				}
			}
			else {
				String s = znamenke + String.valueOf(digit);
				decim = Double.valueOf(s);
				znamenke = s;
				if(decim >= Double.MAX_VALUE) {
					throw new CalculatorInputException();
				}
			}	
		} catch(Exception e) {
			throw new CalculatorInputException("new moze se pretvoriti u double");
		}
		this.zamrznuto = null;
		this.freezeValue(this.znamenke);
		for(CalcValueListener l : promatraci) {
			l.valueChanged(this);
		}
		
		
	}

	@Override
	public boolean isActiveOperandSet() {
		return this.hasActiveOperand;
	}

	@Override
	public double getActiveOperand() throws IllegalStateException {
		if(!this.isActiveOperandSet()) {
			throw new IllegalStateException("aktivni operand je nula");
		}
		return this.activeOperand;
	}

	@Override
	public void setActiveOperand(double activeOperand) {
		this.activeOperand = activeOperand;
		this.hasActiveOperand = true;
		this.positive= true;
		for(CalcValueListener l : promatraci) {
			l.valueChanged(this);
		}
		
	}

	@Override
	public void clearActiveOperand() {
		this.activeOperand = 0;
		this.hasActiveOperand = false;
		for(CalcValueListener l : promatraci) {
			l.valueChanged(this);
		}
		
	}
	@Override
	public DoubleBinaryOperator getPendingBinaryOperation() {
		return this.pendingOperation;
	}

	@Override
	public void setPendingBinaryOperation(DoubleBinaryOperator op) {
	//	this.freezeValue(this.znamenke);
		this.pendingOperation = op;
		for(CalcValueListener l : promatraci) {
			l.valueChanged(this);
		}
		
	}
	@Override
	public String toString() {
		if(zamrznuto=="" || zamrznuto == null) {
			zamrznuto = "0";
		}
		return zamrznuto;
	}
	
	public void freezeValue(String s) {
		this.zamrznuto = s;
		for(CalcValueListener l : promatraci) {
			l.valueChanged(this);
		}
	}

}
