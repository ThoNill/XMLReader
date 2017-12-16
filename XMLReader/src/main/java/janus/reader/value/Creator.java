package janus.reader.value;

/**
 * A creator creates objects
 * 
 * @author Thomas Nill
 *
 * @param <T>
 */
@FunctionalInterface
public interface Creator<T> {
    
    /**
     * creat a object 
     * 
     * @return
     */
    T createObject();
}
