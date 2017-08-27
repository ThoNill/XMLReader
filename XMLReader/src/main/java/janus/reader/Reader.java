package janus.reader;

import janus.reader.actions.Action;
import janus.reader.actions.CurrentObject;
import janus.reader.actions.NamedActionMap;
import janus.reader.actions.SetAction;
import janus.reader.annotations.AnnotationProcessor;
import janus.reader.core.StringStack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class Reader implements Iterator<Object> {
    private StringStack s;
    private CurrentObject current;
    private XMLStreamReader xmlr;
    private NamedActionMap map;

    public Reader() {
        super();
        current = new CurrentObject();
        map = new NamedActionMap();
        s = new StringStack(current, map);
    }

    public Reader(Class<?>... classes) {
        this();
        readAnnotations(classes);
    }

    private void readAnnotations(Class<?>[] classes) {
        AnnotationProcessor p = new AnnotationProcessor();
        p.processClasses(s, classes);
    }

    public void read(String filename) {
        XMLInputFactory xmlif = XMLInputFactory.newInstance();
        try {
            xmlr = xmlif.createXMLStreamReader(filename, new FileInputStream(
                    filename));
        } catch (FileNotFoundException | XMLStreamException e) {
            throw new ReaderRuntimeException("Failed to process file", e);
        }
    }

    private void next(XMLStreamReader xmlr) {
        switch (xmlr.getEventType()) {
        case XMLStreamConstants.START_ELEMENT:
            nextStartElement(xmlr);
            break;
        case XMLStreamConstants.END_ELEMENT:
            nextEndElement(xmlr);
            break;
        case XMLStreamConstants.SPACE:
        case XMLStreamConstants.CHARACTERS:
            nextText(xmlr);
            break;
        case XMLStreamConstants.PROCESSING_INSTRUCTION:
        case XMLStreamConstants.CDATA:
        case XMLStreamConstants.COMMENT:
        case XMLStreamConstants.ENTITY_REFERENCE:
        case XMLStreamConstants.START_DOCUMENT:
        default: break;    
        }
    }

    private void nextText(XMLStreamReader xmlr) {
        int start = xmlr.getTextStart();
        int length = xmlr.getTextLength();
        s.setText(new String(xmlr.getTextCharacters(), start, length));
    }

    private void nextEndElement(XMLStreamReader xmlr) {
        if (xmlr.hasName()) {
            s.pop();
        }
    }

    private void nextStartElement(XMLStreamReader xmlr) {
        if (xmlr.hasName()) {
            s.push(xmlr.getLocalName());
        }
        verarbeiteAttribute(xmlr);
    }

    private void verarbeiteAttribute(XMLStreamReader xmlr) {
        for (int i = 0; i < xmlr.getAttributeCount(); i++) {
            verarbeiteAttribut(xmlr, i);
        }
    }

    private void verarbeiteAttribut(XMLStreamReader xmlr, int index) {
        String localName = xmlr.getAttributeLocalName(index);
        String value = xmlr.getAttributeValue(index);
        s.setAttribute(localName, value);

    }

    public void addValue(String name, Class<?> clazz) {
        s.addValue(name, clazz);
    }

    public void addSetter(String valueName, String relPath, String field) {
        s.addSetter(valueName, relPath, field);
    }

    public void addRelativSetter(String valueName, String relPath, String field) {
        s.addRelativSetter(valueName, relPath, field);
    }

    public void addAction(String name, Action action) {
        s.addAction(name, action);
    }

    public void addAction(String name, SetAction action) {
        s.addAction(name, action);
    }

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

    @Override
    public boolean hasNext() {
        try {
            return xmlr.hasNext() || current.hasObject();
        } catch (XMLStreamException e) {
            throw new ReaderRuntimeException(e);
        }
    }
}
