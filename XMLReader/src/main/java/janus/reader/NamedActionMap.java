package janus.reader;

import java.util.HashMap;
import java.util.Map;

public class NamedActionMap extends HashMap<String, NamedAction> {

    public NamedActionMap() {
        super();
    }

    public NamedActionMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public NamedActionMap(int initialCapacity) {
        super(initialCapacity);
    }

    public NamedActionMap(Map<? extends String, ? extends NamedAction> m) {
        super(m);
    }

    public void put(NamedAction action) {
        put(action.getName(), action);
    }

    public void setValue(String name, String value) {
        NamedAction action = get(name);
        if (action != null) {
            action.setValue(value);
        }
    }

    public void push(String name) {
        NamedAction action = get(name);
        if (action != null) {
            action.push();
        }
    }

    public void pop(String name) {
        NamedAction action = get(name);
        if (action != null) {
            action.pop();
        }
    }

}
