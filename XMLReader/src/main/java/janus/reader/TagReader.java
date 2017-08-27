package janus.reader;

import janus.reader.actions.CurrentObject;
import janus.reader.actions.NamedActionMap;
import janus.reader.core.StringStack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class TagReader {
    private HashMap<String, String> tags;
    private NamedActionMap map;
    private StringStack s;
    private CurrentObject current;
    private XMLStreamReader xmlr;

    public TagReader() {
        super();
        tags = new HashMap<>();
        current = new CurrentObject();
        map = new NamedActionMap();
        s = new StringStack(current, map);
    }

    public void read(String filename)  {

        try {
            XMLInputFactory xmlif = XMLInputFactory.newInstance();
        xmlr = xmlif.createXMLStreamReader(filename, new FileInputStream(
                filename));
    } catch (FileNotFoundException | XMLStreamException e) {
        throw new ReaderRuntimeException("Failed to process file", e);
    }

    }

    public Object next() throws XMLStreamException {
        while ((!current.hasObject()) && xmlr.hasNext()) {
            next(xmlr);
            xmlr.next();
        }
        return current.next();
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
        default:
            break;
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
            String pfad = s.getCurrentPath();
            tags.put(pfad, pfad);
        }
        bearbeiteAttribute(xmlr);
    }

    private void bearbeiteAttribute(XMLStreamReader xmlr) {
        for (int i = 0; i < xmlr.getAttributeCount(); i++) {
            bearbeiteAttribut(xmlr, i);
        }
    }

    private void bearbeiteAttribut(XMLStreamReader xmlr, int index) {
        String localName = xmlr.getAttributeLocalName(index);
        s.push("@" + localName);
        String pfad = s.getCurrentPath();
        tags.put(pfad, pfad);
        s.pop();

    }

    public String source(String packageName, String className) {
        StringBuilder builder = new StringBuilder();
        builder.append("package " + packageName + ";\n");
        builder.append("public interface " + className + " {\n");
        for (String name : tags.keySet()) {
            String[] umgekehrt = name.substring(1).split("\\/");
            builder.append(" String ");
            for (int i = umgekehrt.length - 1; i >= 0; i--) {
                builder.append(umgekehrt[i].replaceAll("\\@", "At"));
                if (i > 0) {
                    builder.append("_");
                }
            }
            builder.append(" = \"" + name + "\";\n");
        }
        builder.append("}\n\n");
        return builder.toString();
    }
}
