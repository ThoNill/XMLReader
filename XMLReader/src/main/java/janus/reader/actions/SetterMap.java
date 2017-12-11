package janus.reader.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The SetterMap is a Collection of Setter entries
 * 
 * @author Thomas Nill
 *
 */
public class SetterMap extends PathEntryMap<Setter> {
    static private Logger log = LoggerFactory.getLogger(SetterMap.class);

    /**
     * constructor of parent class
     */
    public SetterMap() {
        super();
    }

    /**
     * constructor of parent class
     */
    public SetterMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * constructor of parent class
     */
    public SetterMap(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * constructor of parent class
     */
    public SetterMap(SetterMap m) {
        super(m);
    }

    /**
     * call a {@link Setter} setValue action
     * 
     * @param path
     * @param value
     */
    
    public void setValue(TagPath path, String value) {
        log.debug(" setValue auf Path {} to {} ", path, value);
        Setter action = get(path);
        if (action != null && action.isSetableFromString()) {
            log.debug(" find {} ", action.getPath());
            action.setValue(value);
        }
    }


}
