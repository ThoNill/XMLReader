package janus.reader.actions;

/**
 * The Path of the Tags
 * 
 * @author Thomas Nill
 *
 */
public class TagPath {
    private String path;
    private int depth = 0;

    /**
     * Constructor
     * 
     * @param path
     */
    public TagPath(String path) {
        super();
        this.path = path;
        this.depth = calculateDepth(path);
    }

    /**
     * Constructor
     * 
     * @param path
     */
    public TagPath(TagPath path) {
        this(path.getPath());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TagPath other = (TagPath) obj;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return path;
    }

    /**
     * is a absolute path example: /a/b/c
     * 
     * @return
     */
    public boolean isAbsolut() {
        return path.charAt(0) == '/';
    }

    /**
     * compares two paths
     * 
     * @param path
     * @return
     */
    public boolean compare(TagPath path) {
        if (isAbsolut()) {
            return this.path.equals(path.path);
        }
        return path.path.endsWith(this.path);
    }

    /**
     * get the paranet path example: the paranet path of /a/b/c is /a/b
     * 
     * @return
     */
    public TagPath parent() {
        int lastIndex = this.path.lastIndexOf('/');
        if (lastIndex >= 0) {
            return new TagPath(this.path.substring(0, lastIndex));
        }
        return null;
    }

    public int getDepth() {
        return depth;
    }

    /**
     * concatenation of paths
     * @param p
     * @return
     */
    public TagPath concat(TagPath p) {
        return new TagPath(this.path + "/" + p.path);
    }

    public String getPath() {
        return path;
    }

    /*
     * The depth of a path example: the depth of /a/b/c is 4
     */
    private int calculateDepth(String pathString) {
        int calcDepth = 0;
        for (int i = pathString.length() - 1; i >= 0; i--) {
            if (pathString.charAt(i) == '/') {
                calcDepth++;
            }
        }
        return calcDepth + 1;
    }
}
