package janus.reader;

import java.util.Enumeration;
import java.util.Stack;

public class StringStack extends Stack<String> {
    NamedActionMap map;
    CurrentObject current;

    public StringStack(CurrentObject current) {
        this(current,new NamedActionMap());
    }

    
    public StringStack(CurrentObject current,NamedActionMap map) {
        super();
        this.current = current;
        this.map = map;
    }

    public void addValue(String name, Class clazz) {
        Value value = new Value(clazz, current);
        addAction(name, value);
    }

    public void addRelativSetter(String valueName, String relPath, String field) {
        addSetter(valueName, valueName + "/" + relPath, field);
    }

    public void addSetter(String valueName, String absPath, String field) {
        Value value =checkArguments(valueName, absPath, field);
        SetAction setter = value.createSetAction(field);
        addAction(absPath, setter);
    }

    public String getCurrentPath() {
        StringBuilder builder = new StringBuilder();
        Enumeration<String> e = this.elements();
        while (e.hasMoreElements()) {
            String s = e.nextElement();
            builder.append("/");
            builder.append(s);
        }
        return builder.toString();
    }

    public void addAction(String name, Action action) {
        map.put(new SimpleNamedAction(name, action, null));
    }

    public void addAction(String name, SetAction action) {
        map.put(new SimpleNamedAction(name, null, action));
    }

    public void setAttribute(String item, String value) {
        super.push("@" + item);
        String path = getCurrentPath();
        map.setValue(path, value);
        super.pop();
    }

    public void setText(String value) {
        String path = getCurrentPath();
        map.setValue(path, value);
    }

    @Override
    public String push(String item) {
        String erg = super.push(item);
        String path = getCurrentPath();
        map.push(path);
        return erg;
    }

    @Override
    public String pop() {
        String path = getCurrentPath();
        String erg = super.pop();
        map.pop(path);
        return erg;
    }
    
    private Value checkArguments(String valueName, String absPath,
            String field) {
        if (valueName == null || absPath == null || field == null) {
            throw new IllegalArgumentException(
                    "Pfade oder Feldnamen müssen != null sein");
        }
        Object o = map.get(valueName);
        if (o == null) {
            throw new IllegalArgumentException("Value " + valueName
                    + " ist nicht vorhanden");
        }
        if (!(o instanceof NamedAction)) {
            throw new IllegalArgumentException("Object " + valueName
                    + " ist keine NamedAction sondern "
                    + o.getClass().getName());
        }
        o = ((NamedAction) o).getAction();
        if (!(o instanceof Value)) {
            throw new IllegalArgumentException("Object " + valueName
                    + " ist kein Value sondern " + o.getClass().getName());
        }
        return (Value)o;
    }
}
