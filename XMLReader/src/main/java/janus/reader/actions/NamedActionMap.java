package janus.reader.actions;

import java.util.HashMap;
import java.util.Map;

/**
 * The NamedActionMap calls the {@link Action} and {@link SetAction} actions,
 * for a path. It connects the path, with the actions.
 * 
 * @author Thomas Nill
 *
 */
public class NamedActionMap extends HashMap<TagPath, NamedAction> {

    /**
     * constructor of parent class
     */
    public NamedActionMap() {
        super();
    }

    /**
     * constructor of parent class
     */
    public NamedActionMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * constructor of parent class
     */
    public NamedActionMap(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * constructor of parent class
     */
    public NamedActionMap(Map<? extends TagPath, ? extends NamedAction> m) {
        super(m);
    }

    /**
     * put a {@link NamedAction} in the Map
     * 
     * @param action
     */
    public void put(NamedAction action) {
        put(action.getName(), action);
    }

    /**
     * call a {@link NamedAction} setValue action 
     * 
     * @param name
     * @param value
     */
    public void setValue(TagPath name, String value) {
        NamedAction action = get(name);
        if (action != null) {
            action.setValue(value);
        }
    }

    /**
     * call a {@link NamedAction} push action 
     * 
     * @param name
     * @param value
     */
    public void push(TagPath name) {
        NamedAction action = get(name);
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
        NamedAction action = get(name);
        if (action != null) {
            action.pop();
        }
    }

}
