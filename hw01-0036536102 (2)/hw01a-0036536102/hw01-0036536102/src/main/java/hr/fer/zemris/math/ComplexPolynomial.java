package hr.fer.zemris.math;

import java.util.LinkedList;
import java.util.List;
/**
 * Razred koji modelira polinom nad kompleksim brojevima.
 * @author Petra
 *
 */
public class ComplexPolynomial {

	List<Complex> factors = new LinkedList<>();
	
	/**
	 * Konstruktor.
	 * @param factors
	 */
	public ComplexPolynomial(Complex ...factors) {
		for(Complex r : factors) {
			this.factors.add(r);
		}
	}
	// returns order of this polynom; eg. For (7+2i)z^3+2z^2+5z+1 returns 3
	/**
	 * Vraca red ovog polinoma.
	 * @return
	 */
	public short order() {
		return (short) (this.factors.size()-1);
	}
	// computes a new polynomial this*p
	/**
	 * Vraca umnozak ovog polinoma sa predanim polinomom.
	 * @param p
	 * @return
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Complex[] li = new Complex[this.order()+1+p.order()];
		for(int i = 0,j = this.order()+1;i < j;i++) {
			for(int k = 0,h = p.order()+1;k < h;k++) {
				Complex a = this.factors.get(i).multiply(p.factors.get(k));
				if(li[i+k] == null) {
					li[i+k] = a;
				}
				else {
					li[i+k] = li[i+k].add(a);
				}
			}
		}
		return new ComplexPolynomial(li);
	}
	// computes first derivative of this polynomial; for example, for
	// (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5
	/**
	 * Vraca derivaciju ovog polinoma.
	 * @return
	 */
	public ComplexPolynomial derive() {
		Complex[] der = new Complex[this.order()];
		for(int i = 0,j = this.order()+1;i < j;i++) {
			if(i == 0) {
			}
			else {
				Complex c = this.factors.get(i);
				c = c.multiply(new Complex(i,0));
				der[i-1] = c;
			}
		}
		return  new ComplexPolynomial(der);
	}
	// computes polynomial value at given point z
	/**
	 * Racuna vrijednost polinoma u predanoj tocki z.
	 * @param z
	 * @return
	 */
	public Complex apply(Complex z) {
		Complex co = Complex.ZERO;
		int i = 0;
		for(Complex c : this.factors) {
			co = co.add(c.multiply(z.power(i)));
			i++;
		}
		return co;
	}
	/**
	 * Vraca ovaj polinom u obliku stringa.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0,j = this.order();i < j;j--) {
			sb.append(this.factors.get(j)+"*z^"+j+'+');
		}
		sb.append(this.factors.get(0));
		return sb.toString();
	}
}