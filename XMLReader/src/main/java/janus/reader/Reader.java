package janus.reader;

import janus.reader.core.AnnotationProcessor;
import janus.reader.core.ValuesAndAttributesContainer;
import janus.reader.exceptions.ReaderRuntimeException;
import janus.reader.path.XmlElementPath;
import janus.reader.util.Assert;
import janus.reader.value.CurrentObject;
import janus.reader.value.SimpleCurrentObject;
import janus.reader.value.ValueContainer;

import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * The Reader emit the Objects of the configured Classes. If a end -tag ist
 * reached the next method, emits a instance of the Class of this element
 * 
 * The reader must be configured before it is used.
 * 
 * @author Thomas Nill
 *
 */

public class Reader extends BasisReader implements Iterator<Object> {
    private static final String THE_FIELD_SHOULD_NOT_BE_EMPTY = "The field should not be empty";
    private static final String THE_CLASS_SHOULD_NOT_BE_NULL = "The class should not be null";
    private static final String THE_PATH_SHOULD_NOT_BE_NULL = "The path should not be null";
    private ValuesAndAttributesContainer valuesAndAttributesContainer;
    private CurrentObject current;
    private ValueContainer map;

    /**
     * Constructor of an uninitialized Reader
     * 
     */
    public Reader() {
        super();
        current = new SimpleCurrentObject();
        map = new ValueContainer();
        valuesAndAttributesContainer = new ValuesAndAttributesContainer(
                current, map);
    }

    /**
     * Constructor with the initialization in annotated classes
     * 
     * @param classes
     */
    public Reader(Class<?>... classes) {
        this();
        Assert.noNullElements(classes,
                "The classes in the array should not be null");
        readAnnotations(classes);
    }

    /**
     * read the next Object from the Stax-Stream
     * 
     */
    @Override
    public Object next() {
        try {
            while ((!current.hasObject()) && xmlr.hasNext()) {
                next(xmlr);
                xmlr.next();
            }
        } catch (XMLStreamException e) {
            throw new ReaderRuntimeException(e);
        }
        if (!current.hasObject()) {
            throw new NoSuchElementException();
        }
        return current.next();
    }

    /**
     * check if there is a next object that can be read
     * 
     */
    @Override
    public boolean hasNext() {
        try {
            return xmlr.hasNext() || current.hasObject();
        } catch (XMLStreamException e) {
            throw new ReaderRuntimeException(e);
        }
    }

    /**
     * Add the creation of a class instance to a path of XML Elements
     * 
     * @param path
     *            (path of XML Elements)
     * @param clazz
     *            (class of the generated instance)
     */
    public void addValue(XmlElementPath path, Class<?> clazz) {
        Assert.notNull(path, THE_PATH_SHOULD_NOT_BE_NULL);
        Assert.notNull(clazz, THE_CLASS_SHOULD_NOT_BE_NULL);

        valuesAndAttributesContainer.addValue(path, clazz);
    }

    /**
     * Add the setXXXX setter to a path of XML Elements
     * 
     * @param valuePath
     *            (the path of XML elements to the object instance, that will be
     *            set)
     * @param setterPath
     *            (the absolute path of XML elements to a text-value, for the
     *            value attribute of the setter)
     * @param field
     *            (the name of the setter Method)
     */
    public void addSetter(XmlElementPath valuePath, XmlElementPath setterPath,
            String field) {
        Assert.notNull(valuePath, THE_PATH_SHOULD_NOT_BE_NULL);
        Assert.notNull(setterPath, THE_CLASS_SHOULD_NOT_BE_NULL);
        Assert.hasText(field, THE_FIELD_SHOULD_NOT_BE_EMPTY);

        valuesAndAttributesContainer.addAttribute(valuePath, setterPath, field);
    }

    /**
     * Add the setXXXX setter to a path of XML Elements
     * 
     * @param valuePath
     *            (the path of XML elements to the object instance, that will be
     *            set)
     * @param setterPath
     *            (the relative path of XML elements to a text-value, for the
     *            value attribute of the setter)
     * @param field
     *            (the name of the setter Method)
     */
    public void addRelativSetter(XmlElementPath valuePath,
            XmlElementPath setterPath, String field) {
        Assert.notNull(valuePath, THE_PATH_SHOULD_NOT_BE_NULL);
        Assert.notNull(setterPath, THE_CLASS_SHOULD_NOT_BE_NULL);
        Assert.hasText(field, THE_FIELD_SHOULD_NOT_BE_EMPTY);

        valuesAndAttributesContainer.addRelativAttribute(valuePath, setterPath,
                field);
    }

    /**
     * Created Object for a Class, perhaps it is not fully instantiated
     *
     * @param valuePath
     * @return
     */
    public Object getValueObject(XmlElementPath valuePath) {
        Assert.notNull(valuePath, THE_PATH_SHOULD_NOT_BE_NULL);
        
        return valuesAndAttributesContainer.getValueObject(valuePath);
    }
    
    /**
     * Created Object for a Class, perhaps it is not fully instantiated with a
     * Exception if the value or Object does not exist
     * 
     * @param valuePath
     * @return
     */
    public Object getValueObjectWithException(XmlElementPath valuePath) {
        Assert.notNull(valuePath, THE_PATH_SHOULD_NOT_BE_NULL);
        
        return valuesAndAttributesContainer.getValueObjectWithException(valuePath);
    }

    @Override
    protected void nextText(XMLStreamReader xmlr) {
        int start = xmlr.getTextStart();
        int length = xmlr.getTextLength();
        valuesAndAttributesContainer.setText(new String(xmlr
                .getTextCharacters(), start, length));
    }

    @Override
    protected void nextEndElement(XMLStreamReader xmlr) {
        if (xmlr.hasName()) {
            valuesAndAttributesContainer.pop();
        }
    }

    @Override
    protected void nextStartElement(XMLStreamReader xmlr) {
        if (xmlr.hasName()) {
            valuesAndAttributesContainer.push(xmlr.getName());
        }
        processAttributes(xmlr);
    }

    @Override
    protected void processAttribute(XMLStreamReader xmlr, int index) {
        String localName = xmlr.getAttributeLocalName(index);
        String value = xmlr.getAttributeValue(index);
        valuesAndAttributesContainer.setAttribute(localName, value);

    }

    private void readAnnotations(Class<?>[] classes) {
        AnnotationProcessor p = new AnnotationProcessor();
        p.processClasses(valuesAndAttributesContainer, classes);
    }


}
