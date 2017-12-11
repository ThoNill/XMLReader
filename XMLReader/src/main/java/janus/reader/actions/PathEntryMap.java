package janus.reader.actions;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The PathEntryMap is a Hash of PathEntrys The searching in the hash is not the
 * default.
 * 
 * @param <T>
 * 
 * @author Thomas Nill
 *
 */
public class PathEntryMap<T extends PathEntry> extends HashMap<TagPath, T> {
    private static final Logger log = LoggerFactory
            .getLogger(PathEntryMap.class);

    /**
     * Constructor of parent class
     */
    public PathEntryMap() {
        super();
    }

    /**
     * Constructor *
     * 
     * @param initialCapacity
     * @param loadFactor
     */
    public PathEntryMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    /**
     * Constructor
     * 
     * @param initialCapacity
     */
    public PathEntryMap(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Constructor
     * 
     * @param m
     */
    public PathEntryMap(Map<? extends TagPath, T> m) {
        super(m);
    }

    /**
     * put an entry into the map
     * 
     * @param entry
     */
    public void put(T entry) {
        log.debug(" add an entry {} of class {} ", entry.getPath(), entry
                .getClass().getName());
        put(entry.getPath(), entry);
    }

    /**
     * get the best matching entry
     * 
     * @param path
     * @return the entry with the path
     */

    public T get(TagPath path) {
        log.debug("get an entry {} ", path);
        TagPath bestPath = searchTheBestMatchingPath(path);
        if (bestPath != null) {
            return super.get(bestPath);
        }
        return null;
    }

    /**
     * search the best matching TagPath in the keyset
     * 
     * @param path
     * @return the best matching path
     */
    protected TagPath searchTheBestMatchingPath(TagPath path) {
        log.debug("searchTheBestMatchingPath {} ", path);
        int bestDepth = 0;
        TagPath bestPath = null;
        for (TagPath p : keySet()) {
            if (p.compare(path) && p.getDepth() > bestDepth) {
                log.debug("better path {} ", p);
                bestPath = p;
                bestDepth = p.getDepth();
            }
        }
        return bestPath;
    }

}
