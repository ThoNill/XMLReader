package janus.reader;

import janus.reader.actions.CurrentObject;
import janus.reader.actions.ElementNameStack;
import janus.reader.actions.SimpleCurrentObject;
import janus.reader.actions.TagPath;
import janus.reader.actions.ValueMap;
import janus.reader.annotations.AnnotationProcessor;
import janus.reader.exceptions.ReaderRuntimeException;
import janus.reader.nls.Messages;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLInputFactory;
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
    private ElementNameStack elementNameStack;
    private CurrentObject current;
    private XMLStreamReader xmlr;
    private ValueMap map;

    /**
     * Constructor of an uninitialized Reader
     * 
     */
    public Reader() {
        super();
        current = new SimpleCurrentObject();
        map = new ValueMap();
        elementNameStack = new ElementNameStack(current, map);
    }

    /**
     * Constructor with the initialization in annotated classes
     * 
     * @param classes
     */
    public Reader(Class<?>... classes) {
        this();
        readAnnotations(classes);
    }

    /**
     * start to read a file
     * 
     * @param filename
     *            (name of the file)
     */

    public void read(String filename) {
        XMLInputFactory xmlif = XMLInputFactory.newInstance();
        try {
            xmlr = xmlif.createXMLStreamReader(filename, new FileInputStream(
                    filename));
        } catch (FileNotFoundException | XMLStreamException e) {
            Messages.throwReaderRuntimeException(e,"Runtime.FILE_PROCESSING");
        }
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
     * @param name
     *            (path of XML Elements)
     * @param clazz
     *            (class of the generated instance)
     */
    public void addValue(TagPath name, Class<?> clazz) {
        elementNameStack.addValue(name, clazz);
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
    public void addSetter(TagPath valueName, TagPath absPath, String field) {
        elementNameStack.addSetter(valueName, absPath, field);
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
    public void addRelativSetter(TagPath valueName, TagPath relPath,
            String field) {
        elementNameStack.addRelativSetter(valueName, relPath, field);
    }

    @Override
    protected void nextText(XMLStreamReader xmlr) {
        int start = xmlr.getTextStart();
        int length = xmlr.getTextLength();
        elementNameStack.setText(new String(xmlr.getTextCharacters(), start,
                length));
    }

    @Override
    protected void nextEndElement(XMLStreamReader xmlr) {
        if (xmlr.hasName()) {
            elementNameStack.pop();
        }
    }

    @Override
    protected void nextStartElement(XMLStreamReader xmlr) {
        if (xmlr.hasName()) {
            elementNameStack.push(xmlr.getLocalName());
        }
        processAttributes(xmlr);
    }

    protected void processAttribute(XMLStreamReader xmlr, int index) {
        String localName = xmlr.getAttributeLocalName(index);
        String value = xmlr.getAttributeValue(index);
        elementNameStack.setAttribute(localName, value);

    }

    private void readAnnotations(Class<?>[] classes) {
        AnnotationProcessor p = new AnnotationProcessor();
        p.processClasses(elementNameStack, classes);
    }

    /**
     * Created Object for a Class, perhaps it is not fully instantiated
     *
     * @param name
     * @return
     */
    public Object getValueObject(TagPath name) {
        return elementNameStack.getValueObject(name);
    }

    /**
     * Created Object for a Class, perhaps it is not fully instantiated with a
     * Exception if the value or Object does not exist
     * 
     * @param name
     * @return
     */
    public Object getValueObjectWithException(TagPath name) {
        return elementNameStack.getValueObjectWithException(name);
    }

}
