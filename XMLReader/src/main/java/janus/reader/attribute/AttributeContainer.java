package janus.reader.attribute;

import janus.reader.path.PathEntryContainer;
import janus.reader.path.XmlElementPath;
import janus.reader.util.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The AttributeContainer is a Collection of Attribute entries
 * 
 * @author Thomas Nill
 *
 */
public class AttributeContainer extends PathEntryContainer<Attribute> {
    private static final Logger log = LoggerFactory.getLogger(AttributeContainer.class);

    /**
     * constructor of parent class
     */
    public AttributeContainer() {
        super();
    }

    /**
     * call a {@link Attribute} setValue action
     * 
     * @param path
     * @param value
     */
    public void setValue(XmlElementPath path, String value) {
        Assert.notNull(path, "Path should not be null");
        log.debug(" setValue auf Path {} to {} ", path, value);
        Attribute attribute = searchTheBestMatchingEntity(path);
        if (attribute != null && attribute.isSetableFromString()) {
            log.debug(" find {} ", attribute.getPath());
            attribute.setValue(value);
        }
    }

}
