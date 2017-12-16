package janus.reader.attribute;

import janus.reader.path.PathEntry;

/**
 * A Attribute sets a property of a class to a value
 * 
 * @author Thomas Nill
 *
 */
public interface Attribute extends PathEntry {

    /**
     * set an attribute/property Value
     * @param objValue
     */
    void setValue(Object objValue);
    /**
     * settable from a String
     * 
     * @return
     */
    public boolean isSetableFromString();  
}
