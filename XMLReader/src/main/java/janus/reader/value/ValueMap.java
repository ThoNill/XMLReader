package janus.reader.value;

import janus.reader.attribute.Attribute;
import janus.reader.path.PathEntryMap;
import janus.reader.path.XmlElementPath;


/**
 * The NamedActionMap calls the {@link Action} and {@link Attribute} actions, for a
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
    public void push(XmlElementPath name) {
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
    public void pop(XmlElementPath name) {
        Value action = get(name);
        if (action != null) {
            action.pop();
        }
    }

}
