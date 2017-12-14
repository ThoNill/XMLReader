package janus.reader.path;

/**
 * A PathEntry is a Value in a {@link PathEntryMap}
 * 
 * @author javaman
 *
 */
public class PathEntry {
    private XmlElementPath path;

    /**
     * Constructor with a String representation of a Path examples: /x, /x/y,
     * z/y
     * 
     * @param path
     */
    public PathEntry(XmlElementPath path) {
        super();
        this.path = path;
    }

    public XmlElementPath getPath() {
        return path;
    }

 
}
