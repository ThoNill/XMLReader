package janus.reader.actions;

/**
 * The Path of the Tags
 */
public class TagPath {
    private String path;
    private int depth= 0;
 

    
    public TagPath(String path) {
        super();
        this.path = path;
        this.depth = calculateDepth(path);
    }

    public TagPath(TagPath path) {
        this(path.getPath());
    }

    private int calculateDepth(String pathString) {
 
        int count=0;
        for(int i=pathString.length()-1;i>=0;i--) {
            if (pathString.charAt(i)=='/') {
                count++;
            }
        }
        return count+1;
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
        if (isAbsolut()) {
            return this.path.equals(path.path);
        }
        return path.path.endsWith(this.path);
    }
    
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
    
    public TagPath concat(TagPath p) {
        return new TagPath(this.path + "/" + p.path);
    }

    public String getPath() {
        return path;
    }

}
