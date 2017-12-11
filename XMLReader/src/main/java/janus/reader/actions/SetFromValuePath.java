package janus.reader.actions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetFromValuePath extends TagPath {
    private static final Logger  log = LoggerFactory.getLogger(SetFromValuePath.class);

    private TagPath valuePath;
    
    public SetFromValuePath(TagPath setterPath,TagPath valuePath) {
        super(setterPath);
        this.valuePath = valuePath;
    }

    
    public boolean compare(TagPath path) {
        log.debug("compare {} and {}",this,path);
        String pathString = path.getPath();
        if (pathString.endsWith(valuePath.getPath())) {
            log.debug("ends with {} ",valuePath.getPath());
            if (pathString.endsWith(getPath())) {
                log.debug("and {} ends with {}",pathString,getPath());
                return true;
            } else {
                String concat = this.getPath() + '/' + valuePath.getPath();
                boolean ok = pathString.endsWith(concat);
                if (ok) {
                    log.debug("and2 {} end with {}",pathString,concat);
                } else {    
                    log.debug("and2 {} does not end with {}",pathString,concat);
                }
                return ok;
            }
        } else {
            log.debug("do not ends with {} ",valuePath.getPath());
        }
        return false;
    }

}
