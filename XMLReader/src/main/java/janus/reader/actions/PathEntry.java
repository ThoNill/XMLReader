package janus.reader.actions;

/**
 * A PathEntry is a Value in a {@link PathEntryMap}
 * 
 * @author javaman
 *
 */
public class PathEntry {
    private TagPath path;

    /**
     * Constructor with a String representation of a Path examples: /x, /x/y,
     * z/y
     * 
     * @param path
     */
    public PathEntry(TagPath path) {
        super();
        this.path = path;
    }

    public TagPath getPath() {
        return path;
    }

 
}
