package hr.fer.oprpp1.custom.collections;

/**
 * Sucelje koje naslijeduje sucelje Collection.
 * @author Petra
 *
 */

public interface List<T> extends Collection<T> {

    public T get(int index);
    public void insert(T value, int position);
    public int indexOf(Object value);
    public void remove(int index); 
    
}
