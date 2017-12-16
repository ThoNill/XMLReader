package janus.reader.value;

import janus.reader.attribute.Attribute;
import janus.reader.exceptions.ReaderRuntimeException;
import janus.reader.path.XmlElementPath;
import janus.reader.path.XmlElementPathEntry;
import janus.reader.util.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A value holds a instance of a class {@value clazz}. in the push action, this
 * instance ist generated in the pop action this object is transfered to the
 * current Object
 * 
 * there ar different methods to create {@link Attribute}
 * 
 * @author Thomas Nill
 *
 */
public abstract class Value extends XmlElementPathEntry {
    private static final Logger log = LoggerFactory.getLogger(Value.class);

    private Class<?> clazz;
    private CurrentObject objectOfClass;
    private CurrentObject current;

    
   /**
     * Constructor
     * 
     * @param path
     * @param clazz
     * @param current
     * @param objectOfClass
     */
    public Value(XmlElementPath path, Class<?> clazz, CurrentObject current,
            CurrentObject objectOfClass) {
        super(path);
        Assert.notNull(path, "The path should not be null");
        Assert.notNull(clazz, "The class should not be null");
        Assert.notNull(current, "The current should not be null");
        Assert.notNull(objectOfClass, "The objectOfClass should not be null");

        
        
        log.debug("Create Value for path {} ", path);
        this.clazz = clazz;
        this.current = current;
        this.objectOfClass = objectOfClass;
    }

    /**
     * create the new Object that would be written in the value 
     * @return
     * @throws Exception
     */
    public abstract Object createNewObject() throws Exception;
    /**
     * push action create a instance of class clazz
     * 
     */
    public void push() {
        try {
             objectOfClass.setCurrent(createNewObject());
        } catch (Exception e) {
            throw new ReaderRuntimeException(e);
        }
    }

    /**
     * pop action transfers object to current
     * 
     */
    public void pop() {
        current.setCurrent(objectOfClass.next());
    }

    public Object getValue() {
        return objectOfClass.getCurrent();
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public CurrentObject getCurrent() {
        return objectOfClass;
    }

    
}
