package janus.reader.actions;

import janus.reader.ReaderRuntimeException;
import janus.reader.adapters.AdapterMap;

import java.lang.reflect.Method;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A value holds a instance of a class {@value clazz}.
 * in the push action, this instance ist generated
 * in the pop action this object is transfered to the current Object
 * 
 * there ar different methods to create {@link SetAction} 
 * 
 * @author Thomas Nill
 *
 */
public class Value implements Action {
    static Logger LOG = LoggerFactory.getLogger(Value.class);

    private Class<?> clazz;
    private Object objectOfClass;
    private CurrentObject current;

    public Value(Class<?> clazz, CurrentObject current) {
        super();
        this.clazz = clazz;
        this.current = current;
    }

    /**
     * push action create a instance of class clazz
     * 
     */
    @Override
    public void push() {
        try {
            objectOfClass = clazz.newInstance();
        } catch (InstantiationException e) {
            LOG.error("The Object could not be instantiated",e);
        } catch (IllegalAccessException e) {
            LOG.error("Illegal Access",e);
        }
    }

    /**
     * pop action transfers object to current
     * 
     */
    @Override
    public void pop() {
        current.setCurrent(objectOfClass);
        objectOfClass = null;
    }
    
    public Object getValue() {
        return objectOfClass;
    }
    
    public Class<?> getClazz() {
        return clazz;
    }

    /**
     * create form a method name
     * 
     * @param name
     * @return
     */
    public SetAction createSetAction(String name) {
        try {
            if (name == null) {
                throw new IllegalArgumentException(
                        "seter Funktionsname muss != null sein");
            }
            String methodName = "set" + name;
            Method method = searchTheMethod(this.getClazz(), methodName,
                    String.class);
            Class<?> targetClass = method.getParameterTypes()[0];
            if (targetClass != String.class) {
                return createSetAction(method,
                        AdapterMap.getAdapter(targetClass));
            }
            return createSetAction(method);
        } catch (Exception e) {
            throw new ReaderRuntimeException("Die Klasse "
                    + getClazz().getName() + " hat keine Methode set" + name
                    + " oder sie ist privat", e);
        }
    }
    
    /**
     * create SetAction from a {@link Method}
     * 
     * @param handle
     * @return
     */
    private SetAction createSetAction(Method handle) {
        Value v = this;

        return new SetAction() {
            Method m = handle;

            @Override
            public void setValue(String value) {
                try {
                    m.invoke(v.getValue(), value);
                } catch (Exception e) {
                    throw new ReaderRuntimeException(" Kann die Methode "
                            + m.getName() + "("
                            + m.getParameterTypes()[0].getTypeName()
                            + ") nicht auf " + v.getValue() + " anwenden",e);
                }
            }
        };

    }

    /**
     * create SetAction from a {@link Method}
     * 
     * @param handle
     * @param adapter
     * @return
     */
    private SetAction createSetAction(Method handle,
            XmlAdapter<String, ?> adapter) {
        Value v = this;

        return new SetAction() {
            Method m = handle;
            XmlAdapter<String, ?> a = adapter;

            @Override
            public void setValue(String value) {
                Object o = "";
                try {
                    o = a.unmarshal(value);
                    m.invoke(v.getValue(), o);
                } catch (Exception e) {
                    throw new ReaderRuntimeException(" Kann die Methode "
                            + m.getName() + "("
                            + m.getParameterTypes()[0].getTypeName()
                            + " mit dem Objecttyp " + o.getClass()
                            + " nicht auf " + v.getValue() + " anwenden",e);
                }
            }
        };

    }
 
    /**
     * search a method
     * 
     * @param clazz
     * @param name
     * @param targetClass
     * @return
     * @throws Exception
     */
    private Method searchTheMethod(Class<?> clazz, String name,
            Class<?> targetClass) {
        Method bestMethod = null;
        Method usableMethod = null;
        for (Method method : clazz.getMethods()) {
            if (name.equals(method.getName())
                    && method.getParameterCount() == 1) {
                if (method.getParameterTypes()[0] == targetClass) {
                    bestMethod = method;
                }
                if (AdapterMap
                        .hasAdapterForClass(method.getParameterTypes()[0])) {
                    usableMethod = method;
                }
            }
        }
        if (bestMethod != null) {
            return bestMethod;
        }
        return usableMethod;
    }

}
