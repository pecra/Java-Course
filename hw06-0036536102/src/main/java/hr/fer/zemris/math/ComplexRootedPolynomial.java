package hr.fer.zemris.math;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Razred koji modelira polinom nad kompleksim brojevima.
 * @author Petra
 *
 */
public class ComplexRootedPolynomial {
	
	Complex constant;
	List<Complex> roots = new LinkedList<>();
	
	/**
	 * Konstruktor.
	 * @param constant
	 * @param roots
	 */
	public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
		this.constant = constant;
		for(Complex r : roots) {
			this.roots.add(r);
		}
	}
	/**
	 * Racuna vrijednost polinoma u tocki z.
	 * @param z
	 * @return
	 */
	public Complex apply(Complex z) {
		Complex c = new Complex();
		c.add(this.constant);
		for(Complex comp : this.roots) {
			c = c.multiply(z.sub(comp));
		}
		return c;
	}
	// converts this representation to ComplexPolynomial type
	/**
	 * Pretvara ovaj broj u oblik ComplexPolynomial.
	 * @return
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial c = new ComplexPolynomial(this.constant);
		for(int i = 0, j = this.roots.size(); i < j; i++) {
			c = c.multiply(new ComplexPolynomial(this.roots.get(i).negate(),Complex.ONE));
		}
		return c;
	}
	/**
	 * Vraca ovaj polinom u obliku stringa.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.constant);
		for(Complex comp : this.roots) {
			sb.append("*(z-"+comp+')');
		}
		return sb.toString();
	}
	// finds index of closest root for given complex number z that is within
	// treshold; if there is no such root, returns -1
	// first root has index 0, second index 1, etc
	/**
	 * Vraca index najblizeg korijena za predani kompleksni broj z koji je unutar predanog ogranicenja.
	 * Ako nema takvog broja vraca -1.
	 * @param z
	 * @param treshold
	 * @return
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
        int minDist = -1;
		double dist = -1;
		int i = 0;
		for(Complex c: this.roots) {
			double dis = Math.sqrt(Math.pow(c.first-z.first,2)+Math.pow(c.second-z.second,2));
			if(dis< treshold) {
				if(dist == -1) {
					minDist = i;
					dist = dis;
				}
				else {
					if(dis < dist) {
						minDist = i;
						dist = dis;
					}
				}
			}
			i++;
		}
		return minDist;
	}
	
}
