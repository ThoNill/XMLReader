package janus.reader.actions;

/**
 * An action with a name
 * 
 * @author javaman
 *
 */
public class PathEntry  {
    private TagPath path;
    
    public PathEntry(TagPath path) {
        super();
        this.path = path;
    }

    public TagPath getPath() {
        return path;
    }

    public void setPath(TagPath path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "PathEntry [path=" + path + "]";
    }
    
    
}
