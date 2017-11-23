package janus.reader.actions;

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
    static private Logger log = LoggerFactory.getLogger(StackCurrentObject.class);

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

    public void setCurrent(Object obj) {
        log.debug("-------------push " +obj);
        this.current.push(obj);
    }

    public boolean hasObject() {
        return current.isEmpty();
    }
    
    public Object getCurrent() {
        log.debug("peek " + current.peek());
        return current.peek();
    }

}
