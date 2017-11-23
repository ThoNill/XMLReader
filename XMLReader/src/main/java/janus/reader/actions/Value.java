package janus.reader.actions;

import janus.reader.adapters.AdapterMap;
import janus.reader.annotations.XmlPath;
import janus.reader.annotations.XmlPaths;
import janus.reader.exceptions.ReaderRuntimeException;
import janus.reader.helper.ClassHelper;

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
 * there ar different methods to create {@link SetAction} 
 * 
 * @author Thomas Nill
 *
 */
public class Value implements Action {
    static Logger LOG = LoggerFactory.getLogger(Value.class);

    private Class<?> clazz;
    private Method staticCreationMethod;
    private CurrentObject objectOfClass;
    private CurrentObject current;

    public Value(Class<?> clazz, CurrentObject current,CurrentObject objectOfClass) {
        this(clazz,current,null,objectOfClass);
    }

    public Value(Class<?> clazz, CurrentObject current,String staticMethodName,CurrentObject objectOfClass) {
        super();
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
            if(targetClass.equals(String.class) || targetClass.isAnnotationPresent(XmlPath.class) || targetClass.isAnnotationPresent(XmlPaths.class)) {
                return createSetAction(method);     
            } else {
                return createSetAction(method,
                        AdapterMap.getAdapter(targetClass));
            }
           
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
            public void setValue(Object value) {
                try {
                    LOG.debug("setValue 1");
                    if (m.getParameters()[0].getType().isAssignableFrom(value.getClass())) {
                        m.invoke(v.getValue(), value);
                    }
                } catch (Exception e) {
                    throw new ReaderRuntimeException(" Kann die Methode "
                            + m.getName() + "("
                            + m.getParameterTypes()[0].getTypeName()
                            + "  " + value + ") nicht auf " + v.getValue() + " anwenden",e);
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

  private SetAction createSetAction(Method handle, XmlAdapter<String, ?> adapter) {
        Value v = this;
 
        return new SetAction() {
            Method m = handle;
            XmlAdapter<String, ?> a = adapter;
 
            @Override
            public void setValue(Object value) {
                LOG.debug("setValue 2");
                Object o = "";
                try {
                    o = a.unmarshal(value.toString());
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

     /** search a method
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
                Class<?> parameterType = method.getParameterTypes()[0];
                if (parameterType == targetClass) {
                    bestMethod = method;
                }
                if (AdapterMap
                        .hasAdapterForClass(parameterType)) {
                    usableMethod = method;
                }
                if (parameterType.isAnnotationPresent(XmlPath.class) || parameterType.isAnnotationPresent(XmlPaths.class)) {
                    usableMethod = method;
                }
            }
        }
        if (bestMethod != null) {
            return bestMethod;
        }
        return usableMethod;
    }

    public CurrentObject getCurrent() {
        return objectOfClass;
    }

}
