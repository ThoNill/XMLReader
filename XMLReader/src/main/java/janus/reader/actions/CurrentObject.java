package janus.reader.actions;

/**
 * holds the current object that will be emitted
 * 
 * @author javaman
 *
 */
public interface CurrentObject {
  
    /**
     * emit the next value, clears the current object
     * 
     * @return the current Object
     */
    Object next();

    /**
     * get the current Object
     *  
     *  @return the current Object
     */

    Object getCurrent();
    /**
     * set the current Object
     * @param current  
     */
    void setCurrent(Object current);
    
    /**
     * Is there a current Object?
     * 
     * @return ansers the question
     */
    boolean hasObject();
    
}
