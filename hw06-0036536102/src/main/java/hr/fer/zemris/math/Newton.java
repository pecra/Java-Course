package hr.fer.zemris.math;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
/**
 * Razred koji racuna i prikazuje fraktal
 * pomocu predanih nultocaka funkcije.
 * @author Petra
 *
 */
public class Newton {

	static List<Complex> l = new LinkedList<>();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.\n"
				+ "Please enter at least two roots, one root per line. Enter 'done' when done.");
		boolean done = false;
		int i = 1;
		Scanner sc= new Scanner(System.in);
		char[] charAr;
		while(!done) {
			System.out.print("Root "+i+"> ");
			i++;
			String s = sc.nextLine();
			if(s.equals("done")) {
				done = true;
			}
			else {
				charAr = s.toCharArray();
				String prvi = "";
				String drugi = "";
				StringBuilder sb = new StringBuilder();
				int j = 0;
				while(j< charAr.length && charAr[j] == ' ') {
					j++;
				}
				if(j< charAr.length &&(charAr[j] == '+' || charAr[j] == '-')) {
					sb.append(charAr[j]);
					j++;
				}
				while(j< charAr.length && charAr[j] != '+' && charAr[j] != '-' && charAr[j] != 'i') {
					if(charAr[j] != ' ') {
						sb.append(charAr[j]);
					}
					j++;
				}
				prvi = sb.toString();
				prvi = prvi.trim();
				sb = new StringBuilder();
				while(j< charAr.length && charAr[j] == ' ') {
					j++;
				}
				if(j< charAr.length &&(charAr[j] == '+' || charAr[j] == '-')) {
					if(charAr[j] == '-') {
                       sb.append(charAr[j]);
					}
					j++;
				}
				while(j< charAr.length && charAr[j] == ' ') {
					j++;
				}
				boolean imaI = false;
				if(j< charAr.length) {
					if(charAr[j] != 'i') {
						System.out.println("Krivi unos");
						System.exit(0);
					}
					imaI = true;
					j++;
				}
				while(j< charAr.length) {
					if(charAr[j] != ' ') {
						sb.append(charAr[j]);
					}
					j++;
				}
				drugi = sb.toString();
				if(prvi == "" && drugi == "") {
					System.out.println("Krivi unos");
					System.exit(0);
				}
				if(prvi.equals("-") || prvi.equals("+")) {
					drugi = prvi + drugi;
					prvi = "";
				}
				if(drugi.equals("-") || drugi.equals("+")) {
					drugi = drugi + "1";
				}
				if(prvi.equals("")) {
					prvi = "0";
				}
				if(drugi.equals("")) {
					drugi = "0";
					if(imaI) {
						drugi = "1";
					}
				}
                double first = Double.parseDouble(prvi);
                double second = Double.parseDouble(drugi);
                l.add(new Complex(first,second));
			}
			
		}
		sc.close();
		
		FractalViewer.show(new MojProducer());	
	}

	/**
	 * Razred koji crta fraktal.
	 * @author Petra
	 *
	 */
	public static class MojProducer implements IFractalProducer {
		
		public Complex[] toArr() {
			Complex[] a = new Complex[l.size()];
			int h = 0;
			for(Complex com : l) {
				a[h] = com;
				h++;
			}
			return a;
		}
		
		ComplexRootedPolynomial c = new ComplexRootedPolynomial(
				Complex.ONE,toArr()
				);
		double treshold =  0.001;
		double rootTreshold =  0.002;
		int maxIters = 16*16*16;

		/**
		 * Proizvodenje vrijednosti fraktala.
		 */
		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax,
				int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
			System.out.println("Zapocinjem izracun...");
			int offset = 0;
			short[] data = new short[width * height];
			ComplexPolynomial polynomial = c.toComplexPolynom();
			ComplexPolynomial derived = polynomial.derive();
			for(int y = 0; y < height; y++) {
				if(cancel.get()) break;
				for(int x = 0; x < width; x++) {
					double cre = x / (width-1.0) * (reMax - reMin) + reMin;
					double cim = (height-1.0-y) / (height-1) * (imMax - imMin) + imMin;
					Complex zn = new Complex(cre,cim);
					double module = 0.0D;
					int iters = 0;
					Complex znold;
					do {
						++iters;
						Complex numerator = polynomial.apply(zn);
						Complex denominator = derived.apply(zn);
						znold = zn;
						Complex fraction = numerator.divide(denominator);
						zn = zn.sub(fraction);
						module = znold.sub(zn).module();
					} while(module > treshold && iters < maxIters);
					int index = c.indexOfClosestRootFor(zn,rootTreshold);
					data[offset++] = (short) (index + 1);
				}
			}
			
			System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
			observer.acceptResult(data, (short)(polynomial.order()+1), requestNo);
		}
		
	}
}


