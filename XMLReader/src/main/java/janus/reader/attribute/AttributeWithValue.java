package janus.reader.attribute;

import janus.reader.annotations.XmlPath;
import janus.reader.path.PathEntry;
import janus.reader.path.XmlElementPath;
import janus.reader.value.CurrentObject;
import janus.reader.value.Value;
import janus.reader.value.ValueMap;

/**
 * set a property of a class with a {@link XmlPath} annotation where the
 * property is an object of another class that has a {@link XmlPath} annotation
 * 
 * @author Thomas Nill
 *
 */

public class AttributeWithValue extends PathEntry {

    private XmlElementPath valuePath;
    private XmlElementPath setterPath;

    private ValueMap valueMap;
    private AttributeMap attributeMap;
    private Value value;
    private Attribute attribute;

    /**
     * Constructor
     * 
     * @param setterPath
     * @param valuePath
     * @param valueMap
     * @param attributeMap
     */
    public AttributeWithValue(XmlElementPath setterPath, XmlElementPath valuePath,
            ValueMap valueMap, AttributeMap attributeMap) {
        super(new AttributeWithValuePath(setterPath, valuePath));
        this.valueMap = valueMap;
        this.attributeMap = attributeMap;
        this.valuePath = valuePath;
        this.setterPath = setterPath;
    }

    /**
     * set the value of the property
     */
    public void setValue() {
        if (value == null || attribute == null) {
            value = valueMap.get(valuePath);
            attribute = attributeMap.get(setterPath);
        }
        if (value != null && attribute != null) {
            CurrentObject current = value.getCurrent();
            Object o = current.next();
            attribute.setValue(o);
            current.setCurrent(o);
        }
    }
}
