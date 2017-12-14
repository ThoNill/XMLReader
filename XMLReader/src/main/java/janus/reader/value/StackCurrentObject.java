package janus.reader.value;

import java.util.ArrayDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * holds the current object that will be emitted
 * 
 * @author javaman
 *
 */
public class StackCurrentObject implements CurrentObject {
    private static final Logger log = LoggerFactory
            .getLogger(StackCurrentObject.class);

    private ArrayDeque<Object> current;

    /**
     * Constructor
     */
    public StackCurrentObject() {
        super();
        current = new ArrayDeque<>();
    }

    @Override
    public Object next() {
        if (current.isEmpty()) {
            return null;
        }
        return current.pop();
    }

    @Override
    public void setCurrent(Object obj) {
        log.debug("-------------push " + obj);
        this.current.push(obj);
    }

    @Override
    public boolean hasObject() {
        return current.isEmpty();
    }

    @Override
    public Object getCurrent() {
        log.debug("peek " + current.peek());
        return current.peek();
    }

}
