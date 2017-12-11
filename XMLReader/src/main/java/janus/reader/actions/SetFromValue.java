package janus.reader.actions;

public class SetFromValue extends PathEntry{
     
    private ValueMap valueMap;
    private SetterMap setterMap;
    private Value value;
    private Setter setter;
    private TagPath valuePath;
    private TagPath setterPath;
       
       
    public SetFromValue(TagPath setterPath,TagPath valuePath,ValueMap valueMap, SetterMap setterMap) {
        super(new SetFromValuePath(setterPath, valuePath));
        this.valueMap = valueMap;
        this.setterMap = setterMap;
        this.valuePath = valuePath;
        this.setterPath = setterPath;
    }
       
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
