package janus.reader.path;

import java.util.HashMap;

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
public class PathEntryMap<T extends PathEntry> extends HashMap<XmlElementPath, T> {
    private static final Logger log = LoggerFactory
            .getLogger(PathEntryMap.class);

    /**
     * Constructor of parent class
     */
    public PathEntryMap() {
        super();
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

    public T get(XmlElementPath path) {
        log.debug("get an entry {} ", path);
        XmlElementPath bestPath = searchTheBestMatchingPath(path);
        if (bestPath != null) {
            return super.get(bestPath);
        }
        return null;
    }

    /**
     * search the best matching XmlElementPath in the keyset
     * 
     * @param path
     * @return the best matching path
     */
    protected XmlElementPath searchTheBestMatchingPath(XmlElementPath path) {
        log.debug("searchTheBestMatchingPath {} ", path);
        int bestDepth = 0;
        XmlElementPath bestPath = null;
        for (XmlElementPath p : keySet()) {
            if (p.compare(path) && p.getDepth() > bestDepth) {
                log.debug("better path {} ", p);
                bestPath = p;
                bestDepth = p.getDepth();
            }
        }
        return bestPath;
    }

}
