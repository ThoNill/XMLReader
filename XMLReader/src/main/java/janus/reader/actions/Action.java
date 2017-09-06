package janus.reader.actions;

/**
 * Action at the start- and end- tags of a element in the XML document
 * 
 * @author javaman
 *
 */
public interface Action {

    /**
     * Action at start-tag
     */
    void push();
    
    /**
     * Action at end-tag
     */
    void pop();
}
