package janus.reader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class TagReader {
    private HashMap<String,String> tags;
    private StringStack s;
    private CurrentObject current;
    private XMLStreamReader xmlr; 
    
    public TagReader() {
        super();
        tags = new HashMap<>();
        current = new CurrentObject();
        s = new StringStack(current);
    }
    
    

    public void read(String filename) throws FileNotFoundException,
            XMLStreamException {
        XMLInputFactory xmlif = XMLInputFactory.newInstance();
        xmlr = xmlif.createXMLStreamReader(filename,
                new FileInputStream(filename));
    }   
    
    public Object next() throws XMLStreamException {
        while ((!current.hasObject()) && xmlr.hasNext()) {
            next(xmlr);
            xmlr.next();
        }
        return current.getCurrent();
    }
    



    private void next(XMLStreamReader xmlr) {
        switch (xmlr.getEventType()) {
        case XMLStreamConstants.START_ELEMENT:
            if (xmlr.hasName()) {
                s.push(xmlr.getLocalName());
                String pfad = s.getCurrentPath();
                tags.put(pfad,pfad);
            }
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
            break;
        case XMLStreamConstants.COMMENT:
            break;
        case XMLStreamConstants.ENTITY_REFERENCE:
            break;
        case XMLStreamConstants.START_DOCUMENT:
            break;
        }
      
    }

    private void printAttributes(XMLStreamReader xmlr) {
        for (int i = 0; i < xmlr.getAttributeCount(); i++) {
            printAttribute(xmlr, i);
        }
    }

    private void printAttribute(XMLStreamReader xmlr, int index) {
        String prefix = xmlr.getAttributePrefix(index);
        String namespace = xmlr.getAttributeNamespace(index);
        String localName = xmlr.getAttributeLocalName(index);
        String value = xmlr.getAttributeValue(index);
        s.push("@"+localName);
        String pfad = s.getCurrentPath();
        tags.put(pfad,pfad);
        s.pop();

    }


    public String source(String packageName,String className) {
        StringBuilder builder = new StringBuilder();
        builder.append("package " + packageName +";\n");
        builder.append("public interface " + className +" {\n");
        for(String name : tags.keySet()) {
            String umgekehrt[] = name.substring(1).split("\\/");
            builder.append("public final String ");
            for(int i=umgekehrt.length-1;i>=0;i--) {
                builder.append(umgekehrt[i]);
                if (i>0) {
                    builder.append("_");
                }
            }
            builder.append(" = \"" + name + "\";\n");
        }
        builder.append("}\n\n");
        return builder.toString();
    }
}
