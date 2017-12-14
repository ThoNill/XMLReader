package janus.reader.attribute;

import janus.reader.path.XmlElementPath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The XmlElementPath to a property of a class This is a path to the setter and a path
 * to a value class
 * 
 * @author Thomas Nill
 *
 */
public class AttributeWithValuePath extends XmlElementPath {
    private static final Logger log = LoggerFactory
            .getLogger(AttributeWithValuePath.class);

    private XmlElementPath valuePath;

    /**
     * Constructor
     * 
     * @param setterPath
     * @param valuePath
     */
    public AttributeWithValuePath(XmlElementPath setterPath, XmlElementPath valuePath) {
        super(setterPath);
        this.valuePath = valuePath;
    }

    @Override
    public boolean compare(XmlElementPath path) {
        log.debug("compare {} and {}", this, path);
        String pathString = path.getPath();
        if (pathString.endsWith(valuePath.getPath())) {
            log.debug("ends with {} ", valuePath.getPath());
            if (pathString.endsWith(getPath())) {
                log.debug("and {} ends with {}", pathString, getPath());
                return true;
            } else {
                String concat = this.getPath() + '/' + valuePath.getPath();
                boolean ok = pathString.endsWith(concat);
                if (ok) {
                    log.debug("and2 {} end with {}", pathString, concat);
                } else {
                    log.debug("and2 {} does not end with {}", pathString,
                            concat);
                }
                return ok;
            }
        } else {
            log.debug("do not ends with {} ", valuePath.getPath());
        }
        return false;
    }

}
