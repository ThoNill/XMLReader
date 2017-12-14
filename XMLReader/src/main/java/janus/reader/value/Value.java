package janus.reader.value;

import janus.reader.attribute.Attribute;
import janus.reader.exceptions.ReaderRuntimeException;
import janus.reader.helper.ClassHelper;
import janus.reader.nls.Messages;
import janus.reader.path.PathEntry;
import janus.reader.path.XmlElementPath;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

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
public class Value extends PathEntry {
    private static final Logger log = LoggerFactory.getLogger(Value.class);

    private Class<?> clazz;
    private Method staticCreationMethod;
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
        this(path, clazz, current, null, objectOfClass);
    }

    /**
     * Constructor
     * 
     * @param path
     * @param clazz
     * @param current
     * @param staticMethodName
     * @param objectOfClass
     */
    public Value(XmlElementPath path, Class<?> clazz, CurrentObject current,
            String staticMethodName, CurrentObject objectOfClass) {
        super(path);
        log.debug("Create Value for path {} ", path);
        this.clazz = clazz;
        this.current = current;
        this.objectOfClass = objectOfClass;
        if (staticMethodName != null) {
            try {
                staticCreationMethod = clazz.getMethod(staticMethodName);
            } catch (NoSuchMethodException | SecurityException e) {
                log.debug("Error {}",e);
                Messages.throwIllegalArgumentException("Value.METHOD_DOESNOT_EXIST",staticMethodName);
            }
            if (!ClassHelper.isThisClassOrASuperClass(
                    staticCreationMethod.getReturnType(), clazz)) {
                Messages.throwIllegalArgumentException("Value.METHOD_WRONG_TYPE",staticMethodName,clazz.getCanonicalName());
            }
            if (!Modifier.isStatic(staticCreationMethod.getModifiers())) {
                Messages.throwIllegalArgumentException("Value.METHOD_NOT_STATIC",staticMethodName);
            }
            if (staticCreationMethod.getParameterCount() > 0) {
                Messages.throwIllegalArgumentException("Value.METHOD_HAS_PARAMETER",staticMethodName);
            }
        }
    }

    /**
     * push action create a instance of class clazz
     * 
     */
    public void push() {
        try {
            if (staticCreationMethod == null) {
                objectOfClass.setCurrent(clazz.newInstance());
            } else {
                objectOfClass.setCurrent(staticCreationMethod.invoke(null));
            }
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
