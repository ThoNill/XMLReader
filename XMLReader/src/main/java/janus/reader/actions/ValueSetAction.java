package janus.reader.actions;

/**
 * set a attribute of a bean
 * 
 * @author javaman
 *
 */
@FunctionalInterface
public interface ValueSetAction {
    void setValue(Object obj, Object value);
}
