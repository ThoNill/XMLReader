package janus.reader;

import janus.reader.core.ValuesAndAttributesContainer;
import janus.reader.nls.Messages;
import janus.reader.path.XmlElementPath;
import janus.reader.value.SimpleCurrentObject;
import janus.reader.value.ValueMap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Helper to generate a interface with string constants of the different paths
 * in a xml file.
 * 
 * @author Thomas Nill
 *
 */
public class TagReader extends BasisReader {
    private HashMap<XmlElementPath, XmlElementPath> tags;
    private ValuesAndAttributesContainer s;
    private XMLStreamReader xmlr;

    /**
     * Constructor of an uninitialized Reader
     * 
     */
    public TagReader() {
        super();
        tags = new HashMap<>();
        SimpleCurrentObject current = new SimpleCurrentObject();
        ValueMap map = new ValueMap();
        s = new ValuesAndAttributesContainer(current, map);
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

        } catch (FileNotFoundException | XMLStreamException e) {
            Messages.throwReaderRuntimeException(e, "Runtime.FILE_PROCESSING");
        }

    }

    /**
     * read the next Object from the Stax-Stream
     * 
     * @return the next Object in the Stream
     */
    public void read() throws XMLStreamException {
        try {
            while (xmlr.hasNext()) {
                next(xmlr);
                xmlr.next();
            }
        } catch (XMLStreamException e) {
            Messages.throwReaderRuntimeException(e, "Runtime.FILE_PROCESSING");
        }

    }

    /**
     * create the source text for a class in a package und classname
     * 
     * @param packageName
     * @param className
     * @return
     */
    public String source(String packageName, String className) {
        StringBuilder builder = new StringBuilder();
        builder.append("package " + packageName + ";\n");
        builder.append("public interface " + className + " {\n");
        for (XmlElementPath name : tags.keySet()) {
            String[] umgekehrt = name.toString().substring(1).split("\\/");
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

    @Override
    protected void nextText(XMLStreamReader xmlr) {
        int start = xmlr.getTextStart();
        int length = xmlr.getTextLength();
        s.setText(new String(xmlr.getTextCharacters(), start, length));
    }

    @Override
    protected void nextEndElement(XMLStreamReader xmlr) {
        if (xmlr.hasName()) {
            s.pop();
        }
    }

    @Override
    protected void nextStartElement(XMLStreamReader xmlr) {
        if (xmlr.hasName()) {
            s.push(xmlr.getLocalName());
            XmlElementPath pfad = s.getCurrentPath();
            tags.put(pfad, pfad);
        }
        processAttributes(xmlr);
    }

    protected void processAttribute(XMLStreamReader xmlr, int index) {
        String localName = xmlr.getAttributeLocalName(index);
        s.push("@" + localName);
        XmlElementPath pfad = s.getCurrentPath();
        tags.put(pfad, pfad);
        s.pop();

    }

}
