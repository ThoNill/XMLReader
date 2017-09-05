package janus.reader;

import janus.reader.actions.CurrentObject;
import janus.reader.actions.NamedActionMap;
import janus.reader.core.ElementNameStack;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Helper to generate a interface with string constants of the different paths
 * in a xml file.
 * 
 * @author Thomas Nill
 *
 */
public class Formater {
    private XMLStreamReader xmlr;
    private int lastDepth = 0;
    private int depth = 0;
    private boolean leaf = false;
    private String tab;

    public Formater(String tab) {
        super();
        this.tab = tab;
    }

    private void incDepth() {
        lastDepth = depth;
        depth++;
    }

    private void decDepth() {
        depth--;
        leaf = (lastDepth == depth);
        
    }

    private void printTabs(int depth) {
        System.out.print("\n");
        for (int i = 0; i < depth; i++) {
            System.out.print(tab);
        }
    }

    /**
     * read a xml file
     * 
     * @param filename
     */
    public void read(String filename) {

        try {
            XMLInputFactory xmlif = XMLInputFactory.newInstance();
            xmlr = xmlif.createXMLStreamReader(filename, new FileInputStream(
                    filename));
            while (xmlr.hasNext()) {
                next(xmlr);
                xmlr.next();
            }

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
        default:
            break;
        }

    }

    private void nextText(XMLStreamReader xmlr) {
        int start = xmlr.getTextStart();
        int length = xmlr.getTextLength();
        System.out.print(new String(xmlr.getTextCharacters(), start, length).replaceAll("\\n",""));
    }

    private void nextEndElement(XMLStreamReader xmlr) {
        if (xmlr.hasName()) {
            decDepth();
            if (!leaf) {
                printTabs(depth+1);
            }
            System.out.print("</"+ xmlr.getLocalName() + ">");
        }
    }

    private void nextStartElement(XMLStreamReader xmlr) {
        incDepth();
        printTabs(depth);
        System.out.print("<"+ xmlr.getLocalName());
        bearbeiteAttribute(xmlr);
        System.out.print(">");
    }

    private void bearbeiteAttribute(XMLStreamReader xmlr) {
        for (int i = 0; i < xmlr.getAttributeCount(); i++) {
            bearbeiteAttribut(xmlr, i);
        }
    }

    private void bearbeiteAttribut(XMLStreamReader xmlr, int index) {
        String localName = xmlr.getAttributeLocalName(index);
        System.out.print(" " + localName + "=\"" + xmlr.getAttributeValue(index)+ "\" ");
    }

}
