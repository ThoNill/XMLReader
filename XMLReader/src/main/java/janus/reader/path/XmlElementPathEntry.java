package janus.reader.path;

import janus.reader.util.Assert;

/**
 * A PathEntry is a Value in a {@link PathEntryContainer}
 * 
 * @author javaman
 *
 */
public class XmlElementPathEntry implements PathEntry{
    private XmlElementPath path;

    /**
     * Constructor with a String representation of a Path examples: /x, /x/y,
     * z/y
     * 
     * @param path
     */
    public XmlElementPathEntry(XmlElementPath path) {
        super();
        Assert.notNull(path, "The path should not be null");

        this.path = path;
    }

    @Override
    public XmlElementPath getPath() {
        return path;
    }

 
}
