package janus.reader.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The SetterMap is a Collection of Setter entries
 * 
 * @author Thomas Nill
 *
 */
public class SetterMap extends PathEntryMap<Setter> {
    private static final Logger log = LoggerFactory.getLogger(SetterMap.class);

    /**
     * constructor of parent class
     */
    public SetterMap() {
        super();
    }

    /**
     * call a {@link Setter} setValue action
     * 
     * @param path
     * @param value
     */
    public void setValue(TagPath path, String value) {
        log.debug(" setValue auf Path {} to {} ", path, value);
        Setter setter = get(path);
        if (setter != null && setter.isSetableFromString()) {
            log.debug(" find {} ", setter.getPath());
            setter.setValue(value);
        }
    }

}
