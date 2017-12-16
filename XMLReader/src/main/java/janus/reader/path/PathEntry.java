package janus.reader.path;


/**
 * A PathEntry is a Value in a {@link PathEntryContainer}
 * 
 * @author javaman
 *
 */
@FunctionalInterface
public interface PathEntry {
    /**
     * get the Path to this entry
     * @return
     */
    XmlElementPath getPath();
}
