package janus.reader.actions;

import janus.reader.annotations.XmlPath;

/**
 * set a property of a class with a {@link XmlPath} annotation 
 * where the property is an object of another class that has a {@link XmlPath} annotation
 * 
 * @author Thomas Nill
 *
 */

public class SetFromValue extends PathEntry{
     
    private TagPath valuePath;
    private TagPath setterPath;

    private ValueMap valueMap;
    private SetterMap setterMap;
    private Value value;
    private Setter setter;
       
       
    /**
     * Constructor
     * 
     * @param setterPath
     * @param valuePath
     * @param valueMap
     * @param setterMap
     */
    public SetFromValue(TagPath setterPath,TagPath valuePath,ValueMap valueMap, SetterMap setterMap) {
        super(new SetFromValuePath(setterPath, valuePath));
        this.valueMap = valueMap;
        this.setterMap = setterMap;
        this.valuePath = valuePath;
        this.setterPath = setterPath;
    }
       
    /**
     * set the value of the property
     */
    public void setValue() {
        if (value == null || setter == null) {
            value = valueMap.get(valuePath);
            setter = setterMap.get(setterPath);
        }
        if (value != null && setter != null) {
            CurrentObject current = value.getCurrent();
            Object o = current.next();
            setter.setValue(o);
            current.setCurrent(o);
        }
    }
}
