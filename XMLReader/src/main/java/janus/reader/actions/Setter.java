package janus.reader.actions;

import janus.reader.annotations.XmlPath;
import janus.reader.annotations.XmlPaths;
import janus.reader.exceptions.ReaderRuntimeException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Setter sets a property of a class to a value
 * 
 * @author Thomas Nill
 *
 */
// @FunctionalInterface
public class Setter extends PathEntry {
    private static final    Logger log = LoggerFactory.getLogger(Setter.class);

    Method m;
    Value v;

    /**
     * Constructor
     * 
     * @param path
     * @param m
     * @param v
     */
    public Setter(TagPath path, Method m, Value v) {
        super(path);
        this.m = m;
        this.v = v;
    }

  
    public void setValue(Object value) {
        try {
            log.debug("setMethod {} to Value {} ", m, value);
            if (m.getParameters()[0].getType().isAssignableFrom(
                    value.getClass())) {
                m.invoke(v.getValue(), value);
            } else {
                log.debug("The Method {} is not asignable from {} ", m,
                        value.getClass());
            }
        } catch (Exception e) {
            throw new ReaderRuntimeException(" Kann die Methode " + m.getName()
                    + "(" + m.getParameterTypes()[0].getTypeName() + "  "
                    + value + ") nicht auf " + v.getValue() + " anwenden", e);
        }
    }

   /**
    * settable from a String
    * 
    * @return
    */
    public boolean isSetableFromString() {
        return m.getParameterTypes()[0].equals(String.class);
    }

    /**
     * The list of TagPath of the value (class) of the property
     * @return
     */
    public List<TagPath> getValuePaths() {
        List<TagPath> tagPathList = new ArrayList<>();
        Class<?> parameterType = m.getParameterTypes()[0];
        if (parameterType.isAnnotationPresent(XmlPath.class)
                || parameterType.isAnnotationPresent(XmlPaths.class)) {
            for (XmlPath cPath : parameterType
                    .getAnnotationsByType(XmlPath.class)) {
                tagPathList.add(new TagPath(cPath.path()));
            }

        }
        return tagPathList;
    }
}
