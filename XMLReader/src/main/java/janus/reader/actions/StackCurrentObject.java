package janus.reader.actions;

import java.util.ArrayDeque;

/**
 * holds the current object that will be emitted
 * 
 * @author javaman
 *
 */
public class StackCurrentObject implements CurrentObject {
    private ArrayDeque<Object> current = new ArrayDeque<>();

    /**
     * emit the next value
     * @return
     */
    public Object next() {
        if (current.isEmpty()) {
            return null;
        }
        return current.pop();
    }

    public void setCurrent(Object current) {
        this.current.push(current);
    }

    public boolean hasObject() {
        return current.isEmpty();
    }
    
    public Object getCurrent() {
        return current.peek();
    }

}
