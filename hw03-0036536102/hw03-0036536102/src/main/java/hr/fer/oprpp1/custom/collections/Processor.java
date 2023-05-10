package hr.fer.oprpp1.custom.collections;


/**
 * Sucelje predstavlja osnovu za pisanje
 * procesora koji obavlja neku funkciju.
 * 
 * @author Petra
 *
 */
public interface Processor<G> {
	public abstract void process(G o);

}
