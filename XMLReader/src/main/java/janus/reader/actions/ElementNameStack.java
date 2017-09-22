package janus.reader.actions;

import java.util.ArrayDeque;
import java.util.HashMap;

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
    private static final long serialVersionUID = 773837341597279034L;
    private NamedActionMap map;
    private CurrentObject current;
    private HashMap<String,Value> valueHash = new HashMap<>();

    /**
     * constructor with a empty configuration
     * 
     * @param current
     */
    public ElementNameStack(CurrentObject current) {
        this(current, new NamedActionMap());
    }

    /**
     * full configured with a NamedActionMap
     * 
     * @param current
     * @param map
     */
    public ElementNameStack(CurrentObject current, NamedActionMap map) {
        super();
        this.current = current;
        this.map = map;
    }

    /**
     * create a path, that represent the current state of the stack, as a
     * String, with delimiter {@literal /}
     * 
     * @return
     */
    public String getCurrentPath() {
        StringBuilder builder = new StringBuilder();
        Object[] a = this.toArray();
        int pos = a.length - 1;
        while (pos >= 0) {
            Object s = a[pos];
            builder.append("/");
            builder.append(s.toString());
            pos--;
        }
        return builder.toString();
    }

    /**
     * Created Object for a Class, perhaps it is not fully instantiated
     *
     * @param name
     * @return
     */
    public Object getValueObject(String name) {
        Value value = valueHash.get(name);
        if (value != null) {
            return value.getValue();
        }
        return null;
    }
 
    /**
     * Created Object for a Class, perhaps it is not fully instantiated
     * with a Exception if the value or Object does not exist
     * 
     * @param name
     * @return
     */
    public Object getValueObjectWithException(String name) {
        Object obj = getValueObject(name);
        if (obj == null) {
            throw new IllegalArgumentException("Aa value for " + name + " does not exist ");
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
        super.push(item);
        String path = getCurrentPath();
        map.push(path);
    }

    /**
     * pop a element name on the stack, calls a pop method of a {@link Action}
     * if it exists
     * 
     */
    @Override
    public String pop() {
        String path = getCurrentPath();
        String erg = super.pop();
        map.pop(path);
        return erg;
    }

    /**
     * call a setter from a attribut in a XML document
     * 
     */
    public void setAttribute(String item, String value) {
        super.push("@" + item);
        String path = getCurrentPath();
        map.setValue(path, value);
        super.pop();
    }

    /**
     * call a setter from a text node
     * 
     * @param value
     */
    public void setText(String value) {
        String path = getCurrentPath();
        map.setValue(path, value);
    }

    /**
     * Add the creation of a class instance to a path of XML Elements
     * 
     * @param name
     *            (path of XML Elements)
     * @param clazz
     *            (class of the generated instance)
     */

    public void addValue(String name, Class<?> clazz) {
        Value value = new Value(clazz, current);
        addAction(name, value);
        valueHash.put(name, value);
    }

    /**
     * Add the creation of a class instance to a path of XML Elements
     * 
     * @param name
     *            (path of XML Elements)
     * @param clazz
     *            (class of the generated instance)
     */

    public void addValue(String name, Class<?> clazz,String methodName) {
        Value value = new Value(clazz, current,methodName);
        addAction(name, value);
        valueHash.put(name, value);
    }

    
    /**
     * Add the setXXXX setter to a path of XML Elements
     * 
     * @param valueName
     *            (the path of XML elements to the object instance, that will be
     *            set)
     * @param absPath
     *            (the absolute path of XML elements to a text-value, for the
     *            value attribute of the setter)
     * @param field
     *            (the name of the setter Method)
     */

    public void addSetter(String valueName, String absPath, String field) {
        Value value = checkArguments(valueName, absPath, field);
        SetAction setter = value.createSetAction(field);
        addAction(absPath, setter);
    }

    /**
     * Add the setXXXX setter to a path of XML Elements
     * 
     * @param valueName
     *            (the path of XML elements to the object instance, that will be
     *            set)
     * @param relPath
     *            (the relative path of XML elements to a text-value, for the
     *            value attribute of the setter)
     * @param field
     *            (the name of the setter Method)
     */

    public void addRelativSetter(String valueName, String relPath, String field) {
        addSetter(valueName, valueName + "/" + relPath, field);
    }

    /**
     * Add a @{Action} to a path of XML Elements
     * 
     * @param name
     *            (the path of XML elements to the object instance, that will be
     *            set)
     * @param action
     *            (the action, that will be executed at the start and end-tag of
     *            the path)
     */

    public void addAction(String name, Action action) {
        map.put(new SimpleNamedAction(name, action, null));
    }

    /**
     * Add a @{SetAction} to a path of XML Elements
     * 
     * @param name
     *            (the path of XML elements to the object instance, that will be
     *            set)
     * @param action
     *            (the action, that will be executed at the start and end-tag of
     *            the path)
     */

    public void addAction(String name, SetAction action) {
        map.put(new SimpleNamedAction(name, null, action));
    }

    private Value checkArguments(String valueName, String absPath, String field) {
        if (valueName == null || absPath == null || field == null) {
            throw new IllegalArgumentException(
                    "Pfade oder Feldnamen muessen != null sein");
        }
        Object o = checkName(valueName);
        checkNamedAction(valueName, o);
        return checkValue(valueName, o);
    }

    private Object checkName(String valueName) {
        Object o = map.get(valueName);
        if (o == null) {
            throw new IllegalArgumentException("Value " + valueName
                    + " ist nicht vorhanden");
        }
        return o;
    }

    private void checkNamedAction(String valueName, Object o) {
        if (!(o instanceof NamedAction)) {
            throw new IllegalArgumentException("Object " + valueName
                    + " ist keine NamedAction sondern "
                    + o.getClass().getName());
        }
    }

    private Value checkValue(String valueName, Object o) {
        Action a = ((NamedAction) o).getAction();
        if (!(a instanceof Value)) {
            throw new IllegalArgumentException("Object " + valueName
                    + " ist kein Value sondern " + a.getClass().getName());
        }
        return (Value) a;
    }
}