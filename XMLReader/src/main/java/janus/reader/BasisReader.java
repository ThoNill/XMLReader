package janus.reader;

import janus.reader.nls.Messages;
import janus.reader.util.Assert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * The BasisReader is a baseclass for reading XML Files with a StaxReader
 * 
 * @author Thomas Nill
 *
 */

public abstract class BasisReader {

    protected XMLStreamReader xmlr;

    /**
     * Constructor for super class
     */
    protected BasisReader() {
        super();
    }
    /**
     * start to read a file
     * 
     * @param filename
     *            (name of the file)
     */
    public void read(String filename) {
        Assert.hasText(filename, "The filename should not be empty");
        
        XMLInputFactory xmlif = XMLInputFactory.newInstance();
        try {
            xmlr = xmlif.createXMLStreamReader(filename, new FileInputStream(
                    filename));
        } catch (FileNotFoundException | XMLStreamException e) {
            Messages.throwReaderRuntimeException(e, "Runtime.FILE_PROCESSING");
        }
    }

    protected void next(XMLStreamReader xmlr) {
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
    
    protected void processAttributes(XMLStreamReader xmlr) {
        for (int i = 0; i < xmlr.getAttributeCount(); i++) {
            processAttribute(xmlr, i);
        }
    }

    protected abstract void processAttribute(XMLStreamReader xmlr, int i);

    protected abstract void nextText(XMLStreamReader xmlr);

    protected abstract void nextEndElement(XMLStreamReader xmlr);

    protected abstract void nextStartElement(XMLStreamReader xmlr);


}