package janus.reader.actions;

/**
 * The Path of the Tags
 */
public class TagPath {
    private String path;

    public TagPath(String path) {
        super();
        this.path = path;
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

    public boolean isAbsolut() {
        return path.charAt(0) == '/';
    }

    public boolean compare(TagPath path) {
        if (path.isAbsolut()) {
            return this.path.equals(path.path);
        }
        return this.path.endsWith(path.path);
    }
    
    public TagPath parent() {
        int lastIndex = this.path.lastIndexOf('/');
        if (lastIndex >= 0) {
            return new TagPath(this.path.substring(0, lastIndex));
        }
        return null;
    }

}
