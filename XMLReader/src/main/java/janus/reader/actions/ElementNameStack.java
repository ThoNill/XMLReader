package janus.reader.actions;

import janus.reader.adapters.AdapterMap;
import janus.reader.annotations.XmlPath;
import janus.reader.annotations.XmlPaths;
import janus.reader.nls.Messages;

import java.lang.reflect.Method;
import java.util.ArrayDeque;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A stack of the Element Names in a XML document
 * 
 * if the cursor of a stax-Reader is at a position in the document the started
 * elements form a stack of element names that are current open.
 * 
 * At the starttag of a element, the element name is pushed at the top of the
 * stack At the endtag the element is closed and disappears with a pop from the
 * stack
 * 
 * Attributes nodes push and pop {@literal @}attributname to the stack.
 * 
 * The path of the stack is the concatenated list of element names, with a
 * {@literal /} as a delimiter
 * 
 * at the time of push or pop a action will bee called that is connected to the
 * current path. If no action exists, nothing happens.
 * 
 * The currentObject hold the top object that is completed after a endtag.
 * 
 * @author Thomas Nill
 *
 */
public class ElementNameStack extends ArrayDeque<String> {
    private static final Logger log = LoggerFactory
            .getLogger(ElementNameStack.class);

    private static final long serialVersionUID = 773837341597279034L;
    private CurrentObject current;
    private ValueMap valueMap = new ValueMap();
    private SetterMap setterMap = new SetterMap();
    private SetFromValueMap setFromValueMap = new SetFromValueMap();

    /**
     * constructor with a empty configuration
     * 
     * @param current
     */
    public ElementNameStack(CurrentObject current) {
        this(current, new ValueMap());
    }

    /**
     * full configured with a NamedActionMap
     * 
     * @param current
     * @param map
     */
    public ElementNameStack(CurrentObject current, ValueMap map) {
        super();
        this.current = current;
        this.valueMap = map;
    }

    /**
     * create a path, that represent the current state of the stack, as a
     * String, with delimiter {@literal /}
     * 
     * @return
     */
    public TagPath getCurrentPath() {
        StringBuilder builder = new StringBuilder();
        Object[] a = this.toArray();
        int pos = a.length - 1;
        while (pos >= 0) {
            Object s = a[pos];
            builder.append("/");
            builder.append(s.toString());
            pos--;
        }
        return new TagPath(builder.toString());
    }

    /**
     * Created Object for a Class, perhaps it is not fully instantiated
     *
     * @param name
     * @return
     */
    public Object getValueObject(TagPath name) {
        Value value = valueMap.get(name);
        if (value != null) {
            return value.getValue();
        }
        return null;
    }

    /**
     * Created Object for a Class, perhaps it is not fully instantiated with a
     * Exception if the value or Object does not exist
     * 
     * @param name
     * @return
     */
    public Object getValueObjectWithException(TagPath name) {
        Object obj = getValueObject(name);
        if (obj == null) {
            throw new IllegalArgumentException("Aa value for " + name
                    + " does not exist ");
        }
        return obj;
    }

    /**
     * push a element name on the stack, calls a push method of a {@link Action}
     * if it exists
     * 
     */
    @Override
    public void push(String item) {
        log.debug("Push a Tag " + item);
        super.push(item);
        TagPath path = getCurrentPath();
        valueMap.push(path);
    }

    /**
     * pop a element name on the stack, calls a pop method of a {@link Action}
     * if it exists
     * 
     */
    @Override
    public String pop() {
        TagPath path = getCurrentPath();

        setFromValue(path);

        String erg = super.pop();
        valueMap.pop(path);
        return erg;
    }

    public void setFromValue(TagPath path) {
        log.debug("at pop  {} ", path);
        setFromValueMap.setValue(path);
    }

    /**
     * call a setter from a attribut in a XML document
     * 
     * @param item
     * @param value
     */
    public void setAttribute(String item, String value) {
        super.push("@" + item);
        TagPath path = getCurrentPath();
        setterMap.setValue(path, value);
        super.pop();
    }

    /**
     * call a setter from a text node
     * 
     * @param value
     */
    public void setText(String value) {
        TagPath path = getCurrentPath();
        setterMap.setValue(path, value);
    }

    /**
     * Add the creation of a class instance to a path of XML Elements
     * 
     * @param path
     *            (path of XML Elements)
     * @param clazz
     *            (class of the generated instance)
     */

    public void addValue(TagPath path, Class<?> clazz) {
        Value value = new Value(path, clazz, current, createCurrentObject(path));
        addValue(value);
    }

    private CurrentObject createCurrentObject(TagPath name) {
        return name.isAbsolut() ? new SimpleCurrentObject()
                : new StackCurrentObject();
    }

    /**
     * Add the creation of a class instance to a path of XML Elements
     * 
     * @param path
     *            (path of XML Elements)
     * @param clazz
     *            (class of the generated instance)
     * @param methodName
     */

    public void addValue(TagPath path, Class<?> clazz, String methodName) {
        Value value = new Value(path, clazz, current, methodName,
                createCurrentObject(path));
        addValue(value);
    }

    /**
     * Add the setXXXX setter to a path of XML Elements
     * 
     * @param valuePath
     *            (the path of XML elements to the object instance, that will be
     *            set)
     * @param absPath
     *            (the absolute path of XML elements to a text-value, for the
     *            value attribute of the setter)
     * @param field
     *            (the name of the setter Method)
     */

    public void addSetter(TagPath valuePath, TagPath absPath, String field) {
        if (absPath.startsWith(valuePath)) {
            Value value = checkArguments(valuePath, absPath, field);
            Setter setter = createSetAction(value, absPath, field);
            addSetter(setter);
        }
    }

    /**
     * Add the setXXXX setter to a path of XML Elements
     * 
     * @param valuePath
     *            (the path of XML elements to the object instance, that will be
     *            set)
     * @param relPath
     *            (the relative path of XML elements to a text-value, for the
     *            value attribute of the setter)
     * @param field
     *            (the name of the setter Method)
     */

    public void addRelativSetter(TagPath valuePath, TagPath relPath,
            String field) {
        addSetter(valuePath, valuePath.concat(relPath), field);
    }

    /**
     * Add a @{Action} to a path of XML Elements
     * 
     * @param value
     */

    public void addValue(Value value) {
        valueMap.put(value);
    }

    /**
     * Add a @{SetAction} to a path of XML Elements
     * 
     * @param setter
     */

    public void addSetter(Setter setter) {
        setterMap.put(setter);
    }

    private Value checkArguments(TagPath valueName, TagPath absPath,
            String field) {
        if (valueName == null || absPath == null || field == null) {
            throw new IllegalArgumentException(
                    "Pfade oder Feldnamen muessen != null sein");
        }
        checkName(valueName);
        return checkValue(valueName);
    }

    private Object checkName(TagPath valueName) {
        Object o = valueMap.get(valueName);
        if (o == null) {
            throw new IllegalArgumentException("Value " + valueName
                    + " ist nicht vorhanden");
        }
        return o;
    }

    private Value checkValue(TagPath valueName) {
        return valueMap.get(valueName);
    }

    /**
     * create a Setter form a method name
     * 
     * @param value
     * @param valuePath
     * @param name
     * 
     * @return
     */
    public Setter createSetAction(Value value, TagPath valuePath, String name) {
        try {
            if (name == null) {
                throw new IllegalArgumentException(
                        "seter Funktionsname muss != null sein");
            }
            String methodName = "set" + name;
            Method method = searchTheMethod(value.getClazz(), methodName,
                    String.class);
            Class<?> targetClass = method.getParameterTypes()[0];
            if (targetClass.isAnnotationPresent(XmlPath.class)) {
                XmlPath xpath = targetClass.getAnnotation(XmlPath.class);
                return createSetFromValue(value, valuePath,
                        new TagPath(xpath.path()), method);
            }
            if (targetClass.equals(String.class)) {
                return createSetAction(value, valuePath, method);
            } else {
                return createSetAction(value, valuePath, method,
                        AdapterMap.getAdapter(targetClass));
            }

        } catch (Exception e) {
            Messages.throwReaderRuntimeException(e, "Runtime.NOT_METHOD", value
                    .getClazz().getName(), name);
            return null;
        }
    }

    /**
     * create SetAction from a {@link Method}
     * 
     * @param handle
     * @return
     */

    private Setter createSetAction(Value value, TagPath rValuePath,
            Method handle) {
        return new Setter(rValuePath, handle, value);
    }

    private Setter createSetFromValue(Value value, TagPath setterPath,
            TagPath valuePath, Method handle) {
        log.debug(" createSetFromValuAction Setter {} From {} ", setterPath,
                valuePath);
        SetFromValue setFromValue = new SetFromValue(setterPath, valuePath,
                valueMap, setterMap);
        setFromValueMap.put(setFromValue);
        return new Setter(setterPath, handle, value);
    }

    /**
     * create SetAction from a {@link Method}
     * 
     * @param handle
     * @param adapter
     * @return
     */
    // NOSONAR because this method is USED
    @SuppressWarnings("squid:UnusedPrivateMethod")
    private Setter createSetAction(Value value, TagPath rValuePath,
            Method handle, XmlAdapter<String, ?> adapter) {
        return new SetWithAdapter(rValuePath, handle, value, adapter);
    }

    /**
     * search a method
     * 
     * 
     * @param clazz
     * @param name
     * @param targetClass
     * @return
     * @throws Exception
     */
    // NOSONAR because this method is USED
    @SuppressWarnings("squid:UnusedPrivateMethod")
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
                if (AdapterMap.hasAdapterForClass(parameterType)) {
                    usableMethod = method;
                }
                if (parameterType.isAnnotationPresent(XmlPath.class)
                        || parameterType.isAnnotationPresent(XmlPaths.class)) {
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
