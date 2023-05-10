package hr.fer.oprpp1.hw04.db;

/**
 * Razred predstavlja izraz iz querryja koji se sastoji od gettera podataka,string literala 
 * i operatora.
 * @author Petra
 *
 */
public class ConditionalExpression {
	
	private IFieldValueGetter getter;
	private String str;
	private IComparisonOperator oper;
	public ConditionalExpression(IFieldValueGetter getter, String str, IComparisonOperator oper) {
		this.getter = getter;
		this.str = str;
		this.oper = oper;
	}
	public IFieldValueGetter getFieldGetter() {
		return getter;
	}
	public String getStringLiteral() {
		return str;
	}
	public IComparisonOperator getComparisonOperator() {
		return oper;
	}
	
	
	
	

}
