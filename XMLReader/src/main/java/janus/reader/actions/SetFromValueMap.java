package janus.reader.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The SetFromValue calls the {@link Action} and {@link Setter} actions, for a
 * path. It connects the path, with the actions.
 * 
 * @author Thomas Nill
 *
 */
public class SetFromValueMap extends PathEntryMap<SetFromValue> {
    private static final Logger log = LoggerFactory
            .getLogger(SetFromValueMap.class);

    /**
     * constructor of parent class
     */
    public SetFromValueMap() {
        super();
    }

    @Override
    protected TagPath searchTheBestMatchingPath(TagPath path) {
        log.debug("searchTheBestMatchingPath {} ", path);
        TagPath bestPath = null;
        for (TagPath p : keySet()) {
            if (p.compare(path)) {
                log.debug("better path {} ", p);
                bestPath = p;
            }
        }
        return bestPath;
    }

    public void setValue(TagPath path) {
        SetFromValue s = get(path);
        if (s != null) {
            s.setValue();
        }
    }

}
