package janus.reader.path;

import janus.reader.util.Assert;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The PathEntryContainer is a Hash of PathEntrys The searching in the hash is not the
 * default.
 * 
 * @param <T>
 * 
 * @author Thomas Nill
 *
 */
public class PathEntryContainer<T extends PathEntry> extends ArrayList<T> {
    private static final Logger log = LoggerFactory
            .getLogger(PathEntryContainer.class);

    /**
     * Constructor of parent class
     */
    public PathEntryContainer() {
        super();
    }

    /**
     * put an entry into the map
     * 
     * @param entry
     */
    public void put(T entry) {
        Assert.notNull(entry, "The entry should not be null");

        log.debug(" add an entry {} of class {} ", entry.getPath(), entry
                .getClass().getName());
        add(entry);
    }

    /**
     * get the best matching entry
     * 
     * @param path
     * @return the entry with the path
     */

    public T searchTheBestMatchingEntity(XmlElementPath path) {
        Assert.notNull(path, "The path should not be null");
        log.debug("searchTheBestMatchingEntity for the path {} ", path);
        int bestDepth = 0;
        T bestMatch = null;
        for ( T e : this) {
            XmlElementPath p = e.getPath();
            if (path.compare(p) && p.getDepth() > bestDepth) {
                log.debug("better path {} ", p);
                bestMatch = e;
                bestDepth = p.getDepth();
            }
        }
        return bestMatch;
    }

}
