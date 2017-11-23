package janus.reader.actions;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The NamedActionMap calls the {@link Action} and {@link SetAction} actions,
 * for a path. It connects the path, with the actions.
 * 
 * @author Thomas Nill
 *
 */
public class NamedActionMap extends HashMap<TagPath, NamedAction> {
    static private Logger log = LoggerFactory.getLogger(NamedActionMap.class);


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
        log.debug(" add " + action.getName() + " a: " + action.getAction());
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
        NamedAction action = getValueAction(name);
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
        NamedAction action = getValueAction(name);
        if (action != null) {
            action.pop();
        }
    }

    public NamedAction get(TagPath path) {
        for (TagPath p : keySet()) {
            if (path.compare(p)) {
                return super.get(p);
            }
        }
        return null;
    }


    public NamedAction getValueAction(TagPath path) {
        for (TagPath p : keySet()) {
            if (path.compare(p)) {
                NamedAction valueAction = super.get(p);
                if (valueAction.getAction() != null && valueAction.getAction() instanceof Value) {
                    return valueAction;
                }
            }
        }
        return null;
    }

    public NamedAction getSetterAction(TagPath path) {
        for (TagPath p : keySet()) {
            if (path.compare(p)) {
                NamedAction valueAction = super.get(p);
                if (valueAction.getSetter() != null && valueAction.getSetter() instanceof SetAction) {
                    return valueAction;
                }
            }
        }
        return null;
    }

    
    public boolean contains(TagPath path) {
        for (TagPath p : keySet()) {
            if (path.compare(p)) {
                return true;
            }
        }
        return false;
    }

    public void setValue(TagPath valuePath,TagPath setterPath) {
        if (valuePath == null || setterPath == null) {
            return;
        }
        log.debug("Verbinde " + valuePath + " auf " + setterPath);
        NamedAction valueAction = getValueAction(valuePath);
        NamedAction setter = getSetterAction(setterPath);
        if (valueAction != null) {
            log.debug("Value gefunden " + valueAction.getName() +  " action " + valueAction.getAction());
        }
        if (setter != null) {
            log.debug("Setter gefunden " + setter.getName());
        } else {
            log.debug("Kein Setter gefunden zu " + setterPath);
        }
       
        if (valueAction != null && setter != null && valueAction.getAction() instanceof Value) {
            log.debug("kann gesetzt werden");
            Value value = (Value)valueAction.getAction();
            Object currentValue = value.getCurrent().next();
            log.debug("gesetzt wird " + value.getCurrent().getCurrent() + " mit " + setter + " auf " + currentValue);
            setter.setValue(currentValue);
            value.getCurrent().setCurrent(currentValue);
        }
    }

}
