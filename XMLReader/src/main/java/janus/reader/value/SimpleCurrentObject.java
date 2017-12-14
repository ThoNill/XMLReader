package janus.reader.value;


/**
 * holds the current object that will be emitted
 * 
 * @author javaman
 *
 */
public class SimpleCurrentObject implements CurrentObject {
    private Object current;

    /**
     * emit the next value
     * 
     * @return
     */
    @Override
    public Object next() {
        Object o = current;
        current = null;
        return o;
    }

    @Override
    public void setCurrent(Object current) {
        this.current = current;
    }

    @Override
    public boolean hasObject() {
        return current != null;
    }

    @Override
    public Object getCurrent() {
        return current;
    }
}
