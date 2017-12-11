package janus.reader;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

public abstract class BasisReader {

    public BasisReader() {
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