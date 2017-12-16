package janus.reader.attribute;

import janus.reader.annotations.XmlPath;
import janus.reader.path.XmlElementPath;
import janus.reader.path.XmlElementPathEntry;
import janus.reader.util.Assert;
import janus.reader.value.CurrentObject;
import janus.reader.value.Value;
import janus.reader.value.ValueContainer;

/**
 * set a property of a class with a {@link XmlPath} annotation where the
 * property is an object of another class that has a {@link XmlPath} annotation
 * 
 * @author Thomas Nill
 *
 */

public class AttributeWithValue extends XmlElementPathEntry {

    private XmlElementPath valuePath;
    private XmlElementPath setterPath;

    private ValueContainer valueContainer;
    private AttributeContainer attributeContainer;
    private Value value;
    private Attribute attribute;

    /**
     * Constructor
     * 
     * @param setterPath
     * @param valuePath
     * @param valueContainer
     * @param attributeContainer
     */
    public AttributeWithValue(XmlElementPath setterPath, XmlElementPath valuePath,
            ValueContainer valueContainer, AttributeContainer attributeContainer) {
        super(new AttributeWithValuePath(setterPath, valuePath));
        Assert.notNull(valueContainer, "ValueContainer should not be null");
        Assert.notNull(attributeContainer, "AttributeContainer should not be null");
        
        this.valueContainer = valueContainer;
        this.attributeContainer = attributeContainer;
        this.valuePath = valuePath;
        this.setterPath = setterPath;
    }

    /**
     * set the value of the property
     */
    public void setValue() {
        if (value == null || attribute == null) {
            value = valueContainer.searchTheBestMatchingEntity(valuePath);
            attribute = attributeContainer.searchTheBestMatchingEntity(setterPath);
        }
        if (value != null && attribute != null) {
            CurrentObject current = value.getCurrent();
            Object o = current.next();
            attribute.setValue(o);
            current.setCurrent(o);
        }
    }
}
