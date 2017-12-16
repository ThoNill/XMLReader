package janus.reader.core;

import janus.reader.attribute.Attribute;
import janus.reader.attribute.AttributeContainer;
import janus.reader.attribute.AttributeWithValue;
import janus.reader.attribute.AttributeWithValueContainer;
import janus.reader.path.XmlElementPath;
import janus.reader.util.Assert;
import janus.reader.value.CurrentObject;
import janus.reader.value.Value;
import janus.reader.value.ValueContainer;

import java.util.ArrayDeque;

import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A bag with values and attributes and a current object
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
 * The path of the container is the concatenated list of element names, with a
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

public class ValuesAndAttributesBag extends ArrayDeque<QName> {

    protected static final String THE_VALUE_SHOULD_NOT_BE_NULL = "The value should not be null";
    protected static final String THE_FIELD_SHOULD_NOT_BE_EMPTY = "The field should not be empty";
    protected static final String THE_METHOD_NAME_SHOULD_NOT_BE_EMPTY = "The methodName should not be empty";
    protected static final String THE_CLASS_SHOULD_NOT_BE_NULL = "The class should not be null";
    private static final String THE_ITEM_SHOULD_NOT_BE_EMPTY = "The item should not be empty";
    protected static final String THE_PATH_SHOULD_NOT_BE_NULL = "The path should not be null";
    protected static final String THE_MAP_SHOULD_NOT_BE_NULL = "The map should not be null";
    protected static final String THE_CURRENT_SHOULD_NOT_BE_NULL = "The current should not be null";
    protected static final Logger log = LoggerFactory
                .getLogger(ValuesAndAttributesContainer.class);
    private static final long serialVersionUID = 773837341597279034L;
   
    private CurrentObject current;
    private ValueContainer valueContainer = new ValueContainer();
    private AttributeContainer attributeContainer = new AttributeContainer();
    private AttributeWithValueContainer attributeWithValueContainer = new AttributeWithValueContainer();

   
    /**
     * full configured with a ValueContainer
     * 
     * @param current
     * @param map
     */
    public ValuesAndAttributesBag(CurrentObject current, ValueContainer map) {
        super();
        Assert.notNull(current, THE_CURRENT_SHOULD_NOT_BE_NULL);
        Assert.notNull(map, THE_MAP_SHOULD_NOT_BE_NULL);
        
        this.current = current;
        this.valueContainer = map;
    }

    

    /**
     * push a element name on the stack, calls a push method of a {@link Value}
     * if it exists
     * 
     */
    @Override
    public void push(QName item) {
        Assert.notNull(item, THE_ITEM_SHOULD_NOT_BE_EMPTY);
        
        log.debug("Push a Tag " + item);
        super.push(item);
        XmlElementPath path = getCurrentPath();
        valueContainer.push(path);
    }

    /**
     * pop a element name on the stack, calls a pop method of a {@link Value}
     * if it exists
     * 
     */
    @Override
    public QName pop() {
        XmlElementPath path = getCurrentPath();
    
        setFromValue(path);
    
        QName erg = super.pop();
        valueContainer.pop(path);
        return erg;
    }

    /**
     * Created Object for a Class, perhaps it is not fully instantiated
     *
     * @param path
     * @return
     */
    public Object getValueObject(XmlElementPath path) {
        Assert.notNull(path, THE_PATH_SHOULD_NOT_BE_NULL);
        
        Value value = valueContainer.searchTheBestMatchingEntity(path);
        if (value != null) {
            return value.getValue();
        }
        return null;
    }

    /**
     * Created Object for a Class, perhaps it is not fully instantiated with a
     * Exception if the value or Object does not exist
     * 
     * @param path
     * @return
     */
    public Object getValueObjectWithException(XmlElementPath path) {
        Assert.notNull(path, THE_PATH_SHOULD_NOT_BE_NULL);
    
        Object obj = getValueObject(path);
        if (obj == null) {
            throw new IllegalArgumentException("Aa value for " + path
                    + " does not exist ");
        }
        return obj;
    }

    
    public void setFromValue(XmlElementPath path) {
        Assert.notNull(path, THE_PATH_SHOULD_NOT_BE_NULL);
    
        log.debug("at pop  {} ", path);
        attributeWithValueContainer.setValue(path);
    }

    /**
     * call a setter from a attribut in a XML document
     * 
     * @param item
     * @param value
     */
    public void setAttribute(String item, String value) {
        Assert.hasText(item, THE_ITEM_SHOULD_NOT_BE_EMPTY);
    
        this.push(new QName("@" + item));
        XmlElementPath path = getCurrentPath();
        attributeContainer.setValue(path, value);
        this.pop();
    }

    /**
     * call a setter from a text node
     * 
     * @param value
     */
    public void setText(String value) {
        XmlElementPath path = getCurrentPath();
        attributeContainer.setValue(path, value);
    }



    protected Value checkArguments(XmlElementPath valuePath, XmlElementPath attributePath, String field) {
        Assert.notNull(valuePath, THE_PATH_SHOULD_NOT_BE_NULL);
        Assert.notNull(attributePath, THE_CLASS_SHOULD_NOT_BE_NULL);
        Assert.hasText(field, THE_FIELD_SHOULD_NOT_BE_EMPTY);
    
        checkName(valuePath);
        return checkValue(valuePath);
    }

    private Object checkName(XmlElementPath valueName) {
        
        Object o = valueContainer.searchTheBestMatchingEntity(valueName);
        if (o == null) {
            throw new IllegalArgumentException("Value " + valueName
                    + " ist nicht vorhanden");
        }
        return o;
    }

    private Value checkValue(XmlElementPath valueName) {
        return valueContainer.searchTheBestMatchingEntity(valueName);
    }

    /**
     * create a path, that represent the current state of the stack, as a
     * String, with delimiter {@literal /}
     * 
     * @return
     */
    public XmlElementPath getCurrentPath() {
        Object[] qObjects = this.toArray();
        QName[] qNames = new QName[qObjects.length];
        for(int i=0;i<qNames.length;i++) {
            // exchange the direction 
            qNames[qNames.length-1-i] = (QName)qObjects[i];
        }
        return new XmlElementPath(qNames);
    }

    protected CurrentObject getCurrent() {
        return current;
    }


    protected ValueContainer getValueMap() {
        return valueContainer;
    }


    protected AttributeContainer getAttributeMap() {
        return attributeContainer;
    }



    /**
     * Add a @{Value} to a path of XML Elements
     * 
     * @param value
     */

    public void addValue(Value value) {
        this.valueContainer.put(value);
    }

    /**
     * Add a @{Attribute} to a path of XML Elements
     * 
     * @param attribute
     */
    public void addAttribute(Attribute attribute) {
        attributeContainer.put(attribute);
    }

    protected void addAttributeWithValue(AttributeWithValue attributeWithValue) {
        attributeWithValueContainer.put(attributeWithValue);
    }


}