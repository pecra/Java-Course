package hr.fer.oprpp1.custom.collections.demo;

import javax.naming.LinkException;

import hr.fer.oprpp1.custom.collections.*;

/**
 * Razred za testiranje krazreda
 * iz paketa hr.fer.oprpp1.custom.collections.
 * @author Petra
 *
 */
public class StackDemo {
	
	public static void main(String[] args) {
		List col1 = new ArrayIndexedCollection();
        List col2 = new LinkedListIndexedCollection();
        col1.add("Ivana");
        col2.add("Jasna");
        Collection col3 = col1;
        Collection col4 = col2;
        col1.get(0);
        col2.get(0);
        ///col3.get(0); // neće se prevesti! Razumijete li zašto?
        //col4.get(0); // neće se prevesti! Razumijete li zašto?
        col1.forEach(System.out::println); // Ivana
        col2.forEach(System.out::println); // Jasna
        col3.forEach(System.out::println); // Ivana
        col4.forEach(System.out::println); // Jasna
		}
	}		