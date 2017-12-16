package janus.reader.value;

import janus.reader.path.XmlElementPath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A value that creates its object with a default constructor
 * 
 * @author Thomas Nill
 *
 */
public class ClassValue extends Value {
    private static final Logger log = LoggerFactory.getLogger(ClassValue.class);
  
    /**
     * Constructor
     * 
     * @param path
     * @param clazz
     * @param current
     * @param objectOfClass
     */
    public ClassValue(XmlElementPath path, Class<?> clazz, CurrentObject current,
            CurrentObject objectOfClass) {
        super(path, clazz, current,objectOfClass);
    }

    @Override
    public Object createNewObject() throws Exception{
        log.debug("create an object of class {} ",getClazz());
        return getClazz().newInstance();
    }

}
