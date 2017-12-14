package janus.reader.actions;

import java.util.Map;

/**
 * The NamedActionMap calls the {@link Action} and {@link Setter} actions, for a
 * path. It connects the path, with the actions.
 * 
 * @author Thomas Nill
 *
 */
public class ValueMap extends PathEntryMap<Value> {

    /**
     * constructor of parent class
     */
    public ValueMap() {
        super();
    }

    /**
     * call a {@link NamedAction} push action
     * 
     * @param name
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
     */
    public void pop(TagPath name) {
        Value action = get(name);
        if (action != null) {
            action.pop();
        }
    }

}
