package hr.fer.zemris.math;

import java.util.LinkedList;
import java.util.List;
/**
 * Razred koji predstavlja model kompleksnog broja.
 * @author Petra
 *
 */
public class Complex {
	/**
	 * Varijable realni i imaginarni dio broja.
	 */
   double first;
   public double second;
   public static final Complex ZERO = new Complex(0,0);
   public static final Complex ONE = new Complex(1,0);
   public static final Complex ONE_NEG = new Complex(-1,0);
   public static final Complex IM = new Complex(0,1);
   public static final Complex IM_NEG = new Complex(0,-1);

   /**
    * Konstruktor
    */
   public Complex() {
	   super();
}
   /**
    * Konstruktor
    */
   public Complex(double re, double im) {
	   this.first = re;
	   this.second = im;
   }
 /**
   * Vraca modul komplksnog broja
   */
   public double module() {
	   return Math.sqrt(first*first + second*second);
   }
   
   /**
    * Vraca komplksni broj koji je umnozak ovog broja sa predanim parametrom.
    * @param c
    * @return
    */
   public Complex multiply(Complex c) {
	   return new Complex((this.first*c.first -this.second*c.second),(this.first*c.second + this.second*c.first));
   }
   
   /**
    * Vraca rezultat dijeljenja sa predanim parametrom.
    * @param c
    * @return
    */
   public Complex divide(Complex c) {
	   return new Complex((this.first*c.first+this.second*c.second)/(c.first*c.first+c.second*c.second),
			   (this.second*c.first-this.first*c.second)/(c.first*c.first+c.second*c.second));
}
   /**
    * Vraca zbrajanje sa predanim parametrom.
    * @param c
    * @return
    */
   public Complex add(Complex c) {	   
	   return new Complex((this.first+c.first),(this.second+c.second));
}

   /**
    * Vraca oduzimanje predanog parametra.
    * @param c
    * @return
    */
   public Complex sub(Complex c) {
	   return new Complex((this.first-c.first),(this.second-c.second));
}
   /**
    * Vraca negirani broj.
    * @return
    */
   public Complex negate() {
	   return new Complex(-this.first,-this.second);
   }
   
   /**
    * Vraca broj na potenciju koja je predana kao parametar.
    * @param n
    * @return
    */
   public Complex power(int n) {
	   Complex temp = new Complex(1,0);
	   temp = temp.multiply(this);
	   if(n == 0) {
		   return Complex.ONE;
	   }
	   if(n == 1) {
		   return this;
	   }
	   for(int i = 0; i < n-1; i++) {
		   temp = temp.multiply(this);
	   }
	   return temp;
   }

   /**
    * Vraca n-ti korijen ovog broja.
    * @param n
    * @return
    */
   public List<Complex> root(int n) {
	   double r = this.module();
	   r = Math.pow(r, 1.0/n);
	   double fi = Math.atan2(this.second,this.first);
	   List<Complex> list = new LinkedList<>();
	   for(int i = 0; i < n; i++) {
		   list.add(new Complex(r*Math.cos((fi+2*i*Math.PI)/(1.0*n)),r*Math.sin((fi+2*i*Math.PI)/(1.0*n))));
	   }
	   return list;
	   
}

   /**
    * vraca kompleksi broju obliku stringa.
    */
   @Override
   public String toString() {
	   StringBuilder sb = new StringBuilder();
	   sb.append('(');
		   sb.append(this.first);
		   if(this.second >= 0) {
			   sb.append('+');
		   }
		   else {
			   sb.append('-');
		   }
           sb.append('i');
		   sb.append(Math.abs(this.second));
		   sb.append(')');
	   return sb.toString();
   }
}