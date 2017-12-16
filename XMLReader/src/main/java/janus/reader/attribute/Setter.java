package janus.reader.attribute;

/**
 * A setter sets a value of an object
 *
 * @author Thomas Nill
 *
 * @param <T>
 * @param <V>
 */
@FunctionalInterface
public interface Setter<T,V> {
 
    /**
     * set the object obj to the value
     * 
     * @param obj
     * @param value
     */
    void setValue(T obj,V value);
}
