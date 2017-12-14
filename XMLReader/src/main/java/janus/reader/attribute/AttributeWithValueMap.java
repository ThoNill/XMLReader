package janus.reader.attribute;

import janus.reader.path.PathEntryMap;
import janus.reader.path.XmlElementPath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The AttributeWithValue calls the {@link Action} and {@link Attribute} actions, for a
 * path. It connects the path, with the actions.
 * 
 * @author Thomas Nill
 *
 */
public class AttributeWithValueMap extends PathEntryMap<AttributeWithValue> {
    private static final Logger log = LoggerFactory
            .getLogger(AttributeWithValueMap.class);

    /**
     * constructor of parent class
     */
    public AttributeWithValueMap() {
        super();
    }

    @Override
    protected XmlElementPath searchTheBestMatchingPath(XmlElementPath path) {
        log.debug("searchTheBestMatchingPath {} ", path);
        XmlElementPath bestPath = null;
        for (XmlElementPath p : keySet()) {
            if (p.compare(path)) {
                log.debug("better path {} ", p);
                bestPath = p;
            }
        }
        return bestPath;
    }

    public void setValue(XmlElementPath path) {
        AttributeWithValue s = get(path);
        if (s != null) {
            s.setValue();
        }
    }

}
