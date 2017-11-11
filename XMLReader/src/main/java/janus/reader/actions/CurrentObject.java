package janus.reader.actions;

/**
 * holds the current object that will be emitted
 * 
 * @author javaman
 *
 */
public interface CurrentObject {
  
    /**
     * emit the next value
     * @return
     */
    Object next();
    void setCurrent(Object current);
    boolean hasObject();
    Object getCurrent();
}
