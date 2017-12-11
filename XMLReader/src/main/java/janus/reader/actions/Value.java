package janus.reader.actions;

import janus.reader.adapters.AdapterMap;
import janus.reader.annotations.XmlPath;
import janus.reader.annotations.XmlPaths;
import janus.reader.exceptions.ReaderRuntimeException;
import janus.reader.helper.ClassHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A value holds a instance of a class {@value clazz}.
 * in the push action, this instance ist generated
 * in the pop action this object is transfered to the current Object
 * 
 * there ar different methods to create {@link Setter} 
 * 
 * @author Thomas Nill
 *
 */
public class Value extends PathEntry implements Action {
    static Logger LOG = LoggerFactory.getLogger(Value.class);

    private Class<?> clazz;
    private Method staticCreationMethod;
    private CurrentObject objectOfClass;
    private CurrentObject current;

    public Value(TagPath path,Class<?> clazz, CurrentObject current,CurrentObject objectOfClass) {
        this(path,clazz,current,null,objectOfClass);
    }

    public Value(TagPath path,Class<?> clazz, CurrentObject current,String staticMethodName,CurrentObject objectOfClass) {
        super(path);
        LOG.debug("Create Value for path {} " ,path);
        this.clazz = clazz;
        this.current = current;
        this.objectOfClass = objectOfClass;
        if (staticMethodName != null) {
            try {
                staticCreationMethod = clazz.getMethod(staticMethodName);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new IllegalArgumentException(
                        "the method " + staticMethodName + " does not exist or is privat/protected",e);
            }
            if (!ClassHelper.isThisClassOrASuperClass(staticCreationMethod.getReturnType(),clazz)) {
                throw new IllegalArgumentException(
                        "the method " + staticMethodName + " create a Object that is not of type " + clazz.getCanonicalName());
            }
            if (!Modifier.isStatic(staticCreationMethod.getModifiers())) {
                throw new IllegalArgumentException(
                        "the method " + staticMethodName + " is not static ");
            }
            if (staticCreationMethod.getParameterCount()> 0) {
                throw new IllegalArgumentException(
                        "the method " + staticMethodName + " has parameters ");
            }
        }
    }

    /**
     * push action create a instance of class clazz
     * 
     */
    @Override
    public void push() {
        try {
            if (staticCreationMethod == null) {
               objectOfClass.setCurrent(clazz.newInstance());
            } else {
                objectOfClass.setCurrent(staticCreationMethod.invoke(null)); 
            }
        } catch (InstantiationException e) {
            LOG.error("The Object could not be instantiated",e);
        } catch (IllegalAccessException e) {
            LOG.error("Illegal Access",e);
        } catch (IllegalArgumentException e) {
            LOG.error("The method " + ((staticCreationMethod==null) ? "NN" : staticCreationMethod.getName()) + " is called with wrong arguments",e);
        } catch (InvocationTargetException e) {
            LOG.error("The method " + ((staticCreationMethod==null) ? "NN" :  staticCreationMethod.getName()) + " can not been invoced",e);
        }
    }

    /**
     * pop action transfers object to current
     * 
     */
    @Override
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
