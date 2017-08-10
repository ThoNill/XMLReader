package janus.reader;

import java.util.Enumeration;
import java.util.Stack;

public class StringStack extends Stack<String>{
    NamedActionMap map = new NamedActionMap();
    CurrentObject current;
    
    public StringStack(CurrentObject current) {
        super();
        this.current = current;
    }
    
    public void addValue(String name,Class clazz) {
        Value value = new Value(clazz,current);
        addAction(name, value);
    }

    public void addRelativSetter(String valueName,String relPath,String field) {
        addSetter(valueName, valueName + "/" + relPath,field);
    }

    public void addSetter(String valueName,String absPath,String field) {
        Value value = (Value)map.get(valueName).getAction();
        SetAction setter = value.createSetAction(field);
        addAction(absPath,setter);
    }

    
    public String getCurrentPath() {
        StringBuilder builder = new StringBuilder();
        Enumeration<String> e = this.elements();
        while(e.hasMoreElements()) {
            String s = e.nextElement();
            builder.append("/");
            builder.append(s);
        }
        return builder.toString();
    }
    
    public void addAction(String name,Action action) {
        map.put(new SimpleNamedAction(name, action, null));
    }
    
    public void addAction(String name,SetAction action) {
        map.put(new SimpleNamedAction(name, null,action));
    }
  
    
    public void setAttribute(String item,String value) {
        String erg = super.push("@" + item);
        String path = getCurrentPath();
        map.setValue(path,value);
        super.pop();
    }
  
    public void setText(String value) {
        String path = getCurrentPath();
        map.setValue(path,value);
    }
  
    
    public String push(String item) {
        String erg = super.push(item);
        String path = getCurrentPath();
        map.push(path);
        return erg;
    }
    
    public String pop() {
        String path = getCurrentPath();
        String erg = super.pop();
        map.pop(path);
        return erg;
    }
}
