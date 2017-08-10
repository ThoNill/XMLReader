package janus.reader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class Reader implements Iterator<Object>{
    private StringStack s;
    private CurrentObject current;
    private XMLStreamReader xmlr; 
    
    public Reader() {
        super();
        current = new CurrentObject();
        s = new StringStack(current);
    }
    
    

    public void read(String filename) throws FileNotFoundException,
            XMLStreamException {
        XMLInputFactory xmlif = XMLInputFactory.newInstance();
        xmlr = xmlif.createXMLStreamReader(filename,
                new FileInputStream(filename));
    }   



    private void next(XMLStreamReader xmlr) {
        switch (xmlr.getEventType()) {
        case XMLStreamConstants.START_ELEMENT:
            if (xmlr.hasName()) {
                s.push(xmlr.getLocalName());
            }
            verarbeiteAttribute(xmlr);
            break;
        case XMLStreamConstants.END_ELEMENT:
            if (xmlr.hasName()) {
                s.pop();
            }
            break;
        case XMLStreamConstants.SPACE:
        case XMLStreamConstants.CHARACTERS:
            int start = xmlr.getTextStart();
            int length = xmlr.getTextLength();
            s.setText(new String(xmlr.getTextCharacters(), start, length));
            break;
        case XMLStreamConstants.PROCESSING_INSTRUCTION:
            break;
        case XMLStreamConstants.CDATA:
   /*         System.out.print("<![CDATA[");
            start = xmlr.getTextStart();
            length = xmlr.getTextLength();
            System.out
                    .print(new String(xmlr.getTextCharacters(), start, length));
            System.out.print("]]>");
     */       break;
        case XMLStreamConstants.COMMENT:
            break;
        case XMLStreamConstants.ENTITY_REFERENCE:
            break;
        case XMLStreamConstants.START_DOCUMENT:
            break;
        }
     }

    private void verarbeiteAttribute(XMLStreamReader xmlr) {
        for (int i = 0; i < xmlr.getAttributeCount(); i++) {
            verarbeiteAttribut(xmlr, i);
        }
    }

    private void verarbeiteAttribut(XMLStreamReader xmlr, int index) {
        String prefix = xmlr.getAttributePrefix(index);
        String namespace = xmlr.getAttributeNamespace(index);
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
    public Object next()  {
        try {
            while ((!current.hasObject()) && xmlr.hasNext()) {
                next(xmlr);
                xmlr.next();
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        return current.getCurrent();
    }

    @Override
    public boolean hasNext() {
        try {
            return xmlr.hasNext();
        } catch (XMLStreamException e) {
           throw new RuntimeException(e);
        }
    }
}

