package hr.fer.oprpp1.custom.collections;

/**
 * Sucelje koje naslijeduje sucelje Collection.
 * @author Petra
 *
 */

public interface List extends Collection {

    public Object get(int index);
    public void insert(Object value, int position);
    public int indexOf(Object value);
    public void remove(int index); 
    
}
