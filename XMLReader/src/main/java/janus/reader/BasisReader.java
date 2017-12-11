package janus.reader;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

/**
 * The BasisReader is a baseclass for reading XML Files with a StaxReader
 * 
 * @author Thomas Nill
 *
 */

public abstract class BasisReader {

    /**
     * Constructor for super class
     */
    protected BasisReader() {
        super();
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

    protected abstract void nextText(XMLStreamReader xmlr);

    protected abstract void nextEndElement(XMLStreamReader xmlr);

    protected abstract void nextStartElement(XMLStreamReader xmlr);

}