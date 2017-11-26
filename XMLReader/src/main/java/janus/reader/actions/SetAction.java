package janus.reader.actions;

/**
 * Action that sets a attribute
 * 
 * @author javaman
 *
 */
//@FunctionalInterface
public interface SetAction {
    void setValue(Object value);
    boolean isSetableFromString();
    TagPath getValuePath();
}
