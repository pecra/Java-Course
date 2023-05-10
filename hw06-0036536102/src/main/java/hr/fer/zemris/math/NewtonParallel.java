package hr.fer.zemris.math;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
/**
 * Razred koji racuna i prikazuje fraktal
 * pomocu predanih nultocaka funkcije.
 * Izracun je ostvaren visedretveno,
 * broj dretvi i poslova za se predaje preko argumenata komandne linije.
 * @author Petra
 *
 */
public class NewtonParallel {
	static List<Complex> l = new LinkedList<>();
	static int brRadnika = Runtime.getRuntime().availableProcessors();
	static int brojTraka = 4;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean noviRadnici = false;
		boolean noveTrake = false;
		if(args.length > 2) {
			throw new IllegalArgumentException();
		}
		for(int i = 0,j = args.length; i < j; i++) {
			String s = args[i];
			if(s.startsWith("--workers=")) {
				if(noviRadnici) {
					throw new IllegalArgumentException("Krivi predani argumenti!");
				}
				noviRadnici = true;
				s = s.substring(10);
				brRadnika = Integer.valueOf(s);
				continue;
			}
			if(s.startsWith("-w")) {
				if(noviRadnici) {
					throw new IllegalArgumentException("Krivi predani argumenti!");
				}
				noviRadnici = true;
				s = s.substring(3);
				brRadnika = Integer.valueOf(s);
				continue;
			}
			if(s.startsWith("--tracks=")) {
				if(noveTrake) {
					throw new IllegalArgumentException("Krivi predani argumenti!");
				}
				noveTrake = true;
				s = s.substring(9);
				int k = Integer.valueOf(s);
				if(k < 1) {
					throw new IllegalArgumentException("Premalo traka");
				}
				brojTraka = k;
				continue;
			}
			if(s.startsWith("-t")) {
				if(noveTrake) {
					throw new IllegalArgumentException("Krivi predani argumenti!");
				}
				noveTrake = true;
				s = s.substring(3);
				int k = Integer.valueOf(s);
				if(k < 1) {
					throw new IllegalArgumentException("Premalo traka");
				}
				brojTraka = k;
				continue;
			}
		}
		
		
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
	 * Razred koji obavlja posao izracuna.
	 * @author Petra
	 *
	 */
	public static class PosaoIzracuna implements Runnable {
		double reMin;
		double reMax;
		double imMin;
		double imMax;
		int width;
		int height;
		int yMin;
		int yMax;
		int maxIters;
		short[] data;
		AtomicBoolean cancel;
		double treshold =  0.001;
		double rootTreshold =  0.002;
		ComplexPolynomial polynomial;
		ComplexPolynomial derived;
		ComplexRootedPolynomial c;
		public static PosaoIzracuna NO_JOB = new PosaoIzracuna();
		
		private PosaoIzracuna() {
		}
		
		/**
		 * Konstruktor.
		 * @param reMin
		 * @param reMax
		 * @param imMin
		 * @param imMax
		 * @param width
		 * @param height
		 * @param yMin
		 * @param yMax
		 * @param maxIters
		 * @param data
		 * @param cancel
		 * @param polynomial
		 * @param derived
		 * @param c
		 */
		public PosaoIzracuna(double reMin, double reMax, double imMin,
				double imMax, int width, int height, int yMin, int yMax, 
				 int maxIters, short[] data, AtomicBoolean cancel,ComplexPolynomial polynomial,
					ComplexPolynomial derived,ComplexRootedPolynomial c) {
			super();
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.maxIters = maxIters;
			this.data = data;
			this.cancel = cancel;
			this.polynomial = polynomial;
			this.derived = derived;
			this.c = c;
		}
		
		@Override
		public void run() {
			
			calculate(reMin,reMax,imMin,
				imMax,width,height,maxIters, yMin, yMax,data,cancel,
				treshold, rootTreshold,polynomial,derived, c);
			
		}
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
		/**
		 * Proizvodenje vrijednosti fraktala.
		 */
		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax,
				int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
			System.out.println("Zapocinjem izracun...");
			int maxIters = 16*16*16;
			short[] data = new short[width * height];
			ComplexPolynomial polynomial = c.toComplexPolynom();
			ComplexPolynomial derived = polynomial.derive();
			if(brojTraka > height) {
				brojTraka = height;
			}
			int brojYPoTraci = height / brojTraka;
			final BlockingQueue<PosaoIzracuna> queue = new LinkedBlockingQueue<>();
			
			System.out.println("Broj poslova: " + brojTraka);
			System.out.println("Broj dretvi: " + brRadnika);

			Thread[] radnici = new Thread[brRadnika];
			for(int i = 0; i < radnici.length; i++) {
				radnici[i] = new Thread(new Runnable() {
					@Override
					public void run() {
						while(true) {
							PosaoIzracuna p = null;
							try {
								p = queue.take();
								if(p==PosaoIzracuna.NO_JOB) break;
							} catch (InterruptedException e) {
								continue;
							}
							p.run();
						}
					}
				});
			}
			for(int i = 0; i < radnici.length; i++) {
				radnici[i].start();
			}
			
			for(int i = 0; i < brojTraka; i++) {
				int yMin = i*brojYPoTraci;
				int yMax = (i+1)*brojYPoTraci-1;
				if(i==brojTraka-1) {
					yMax = height-1;
				}
				PosaoIzracuna posao = new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height,yMin,yMax, maxIters, data, cancel, polynomial, derived, c);
				while(true) {
					try {
						queue.put(posao);
						break;
					} catch (InterruptedException e) {
					}
				}
			}
			for(int i = 0; i < radnici.length; i++) {
				while(true) {
					try {
						queue.put(PosaoIzracuna.NO_JOB);
						break;
					} catch (InterruptedException e) {
					}
				}
			}
			
			for(int i = 0; i < radnici.length; i++) {
				while(true) {
					try {
						radnici[i].join();
						break;
					} catch (InterruptedException e) {
					}
				}
			}
			
			System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
			observer.acceptResult(data, (short)(polynomial.order()+1), requestNo);
		}
	}


	/**
	 * Racunanje vrijednosti.
	 * @param reMin
	 * @param reMax
	 * @param imMin
	 * @param imMax
	 * @param width
	 * @param height
	 * @param maxIters
	 * @param yMin
	 * @param yMax
	 * @param data
	 * @param cancel
	 * @param treshold
	 * @param rootTreshold
	 * @param polynomial
	 * @param derived
	 * @param c
	 */
	public static void calculate(double reMin, double reMax, double imMin,
			double imMax, int width, int height, 
			int maxIters,int yMin, int yMax,short[] data, AtomicBoolean cancel,
			double treshold,double rootTreshold,ComplexPolynomial polynomial,
		ComplexPolynomial derived,ComplexRootedPolynomial c) {
		int offset = yMin * width;
		for(int y = yMin; y <= yMax; y++) {
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
		
	}

}

