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
        log.debug(" setValue auf String {} {} ", name, value);
        NamedAction action = get(name);
        if (action != null && action.isSetableFromString()) {
            log.debug(" find {} ", action.getName());
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
        log.debug("get {} ", path);
        TagPath bestPath = searchTheBestMatchingPath(path);
        if (bestPath != null) {
            return super.get(bestPath);
        }
        return null;
    }

    private TagPath searchTheBestMatchingPath(TagPath path) {
        log.debug("searchTheBestMatchingPath {} ", path);
        int bestDepth = 0;
        TagPath bestPath = null;
        for (TagPath p : keySet()) {
            if (path.compare(p) && p.getDepth() > bestDepth) {
                log.debug("better path {} ", p);
                bestPath = p;
                bestDepth = p.getDepth();
            }
        }
        return bestPath;
    }

    private TagPath searchTheBestMatchingValue(TagPath path) {
        log.debug("searchTheBestMatchingPath {} ", path);
        int bestDepth = 0;
        TagPath bestPath = null;
        for (TagPath p : keySet()) {
            if (path.compare(p) && p.getDepth() > bestDepth) {
                NamedAction a = super.get(p);
                if (a.getAction() != null && a.getAction() instanceof Value) {
                    log.debug("better path {} ", p);
                    bestPath = p;
                    bestDepth = p.getDepth();
                }
            }
        }
        return bestPath;
    }

    public NamedAction getValueAction(TagPath path) {
        log.debug("get {} ", path);
        TagPath bestPath = searchTheBestMatchingValue(path);
        if (bestPath != null) {
            return super.get(bestPath);
        }
        return null;
    }

    public NamedAction getSetterAction(TagPath path) {
        return get(path);
    }

    /*
     * public NamedAction getSetterAction(TagPath path) { for (TagPath p :
     * keySet()) { if (path.compare(p)) { NamedAction valueAction =
     * super.get(p); if (valueAction.getSetter() != null &&
     * valueAction.getSetter() instanceof SetAction) { return valueAction; } } }
     * return null; }
     */

    public boolean contains(TagPath path) {
        TagPath bestPath = searchTheBestMatchingPath(path);
        return (bestPath != null);
    }

    public void setValue(TagPath setterPath) {
        if (setterPath == null) {
            return;
        }
        log.debug("Suche Setter zu {} ", setterPath);
        NamedAction setter = getSetterAction(setterPath);
        if (setter != null && setter.getSetter() != null
                && !setter.isSetableFromString()) {
            log.debug("Setter gefunden " + setter.getName());
            NamedAction valueAction = getValueAction(setter.getValuePath());
            if (valueAction != null && valueAction.getAction() instanceof Value) {
                log.debug("Value gefunden " + valueAction.getName()
                        + " action " + valueAction.getAction());
                Value value = (Value) valueAction.getAction();
                Object currentValue = value.getCurrent().next();
                log.debug("gesetzt wird " + value.getCurrent().getCurrent()
                        + " mit " + setter + " auf " + currentValue);
                setter.setValue(currentValue);
                value.getCurrent().setCurrent(currentValue);
            }
        } else {
            if (setter == null || setter.getSetter() == null) {
                log.debug("Kein Setter gefunden zu " + setterPath);
            } else {
                log.debug("Setter wird ueber String gesetzt");
            }
        }
    }
}
