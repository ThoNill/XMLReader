package janus.reader.actions;

/**
 * holds the current object that will be emitted
 * 
 * @author javaman
 *
 */
public class CurrentObject {
    private Object current;

    /**
     * emit the next value
     * @return
     */
    public Object next() {
        Object o = current;
        current = null;
        return o;
    }

    public void setCurrent(Object current) {
        this.current = current;
    }

    public boolean hasObject() {
        return current != null;
    }

}
