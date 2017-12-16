package janus.reader.attribute;

import janus.reader.path.PathEntryContainer;
import janus.reader.path.XmlElementPath;
import janus.reader.util.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The AttributeWithValue calls the {@link Action} and {@link Attribute} actions, for a
 * path. It connects the path, with the actions.
 * 
 * @author Thomas Nill
 *
 */
public class AttributeWithValueContainer extends PathEntryContainer<AttributeWithValue> {
    private static final Logger log = LoggerFactory
            .getLogger(AttributeWithValueContainer.class);

    /**
     * constructor of parent class
     */
    public AttributeWithValueContainer() {
        super();
    }

    @Override
    public AttributeWithValue searchTheBestMatchingEntity(XmlElementPath path) {
        Assert.notNull(path, "The path should not be null");
        log.debug("searchTheBestMatchingEntity for the path {} ", path);
        AttributeWithValue bestMatch = null;
        for (AttributeWithValue e : this) {
            if (e.getPath().compare(path)) {
                log.debug("better element {} ", e);
                bestMatch = e;
            }
        }
        return bestMatch;
    }

    public void setValue(XmlElementPath path) {
        Assert.notNull(path, "Path should not be null");
        AttributeWithValue s = searchTheBestMatchingEntity(path);
        if (s != null) {
            s.setValue();
        }
    }

}
