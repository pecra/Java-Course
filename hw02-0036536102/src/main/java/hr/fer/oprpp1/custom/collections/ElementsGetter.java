package hr.fer.oprpp1.custom.collections;

/**
 * Sucelje predstavlja osnovu za izvodenje objekta
 * koji dohvaca elemente na korisnikov zahtjev.
 * @author Petra
 *
 */
public interface ElementsGetter {
	
    public abstract boolean hasNextElement();
    public abstract Object getNextElement();
    
    /**
     * Nad svim preostalim elementima kolekcije poziva 
     * p.process(.)
     * @param p predani procesor
     */
    void processRemaining(Processor p);
    
}
