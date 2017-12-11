package janus.reader.actions;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The NamedActionMap calls the {@link Action} and {@link Setter} actions,
 * for a path. It connects the path, with the actions.
 * 
 * @author Thomas Nill
 *
 */
public class ValueMap extends PathEntryMap<Value> {
    static private Logger log = LoggerFactory.getLogger(ValueMap.class);

    /**
     * constructor of parent class
     */
    public ValueMap() {
        super();
    }

    /**
     * constructor of parent class
     */
    public ValueMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * constructor of parent class
     */
    public ValueMap(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * constructor of parent class
     */
    public ValueMap(Map<? extends TagPath,Value> m) {
        super(m);
    }

 
    /**
     * call a {@link NamedAction} push action
     * 
     * @param name
     * @param value
     */
    public void push(TagPath name) {
        Value action = get(name);
        if (action != null) {
            action.push();
        }
    }

    /**
     * call a {@link NamedAction} pop action
     * 
     * @param name
     * @param value
     */
    public void pop(TagPath name) {
        Value action = get(name);
        if (action != null) {
            action.pop();
        }
    }

}
