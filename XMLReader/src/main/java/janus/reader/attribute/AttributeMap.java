package janus.reader.attribute;

import janus.reader.path.PathEntryMap;
import janus.reader.path.XmlElementPath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The AttributeMap is a Collection of Attribute entries
 * 
 * @author Thomas Nill
 *
 */
public class AttributeMap extends PathEntryMap<Attribute> {
    private static final Logger log = LoggerFactory.getLogger(AttributeMap.class);

    /**
     * constructor of parent class
     */
    public AttributeMap() {
        super();
    }

    /**
     * call a {@link Attribute} setValue action
     * 
     * @param path
     * @param value
     */
    public void setValue(XmlElementPath path, String value) {
        log.debug(" setValue auf Path {} to {} ", path, value);
        Attribute attribute = get(path);
        if (attribute != null && attribute.isSetableFromString()) {
            log.debug(" find {} ", attribute.getPath());
            attribute.setValue(value);
        }
    }

}
