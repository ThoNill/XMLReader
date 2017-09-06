package janus.reader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

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
    private Writer writer;

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

    private void writeTabs(int depth)  throws IOException{
        writer.write("\n");
        for (int i = 0; i < depth; i++) {
            writer.write(tab);
        }
    }

    
    
    public void write(String inFilename)  throws IOException{
        InputStream in = new FileInputStream(inFilename);
        Writer out = new OutputStreamWriter(System.out,"UTF8");
        write(in,out);
        in.close();
        out.close();
    }
    
    
/**
 * Format a XML File
 * 
 * @param inFilename
 * @param outFilename
 * @param charset
 * @throws IOException
 */
    public void write(String inFilename,String outFilename,String charset)  throws IOException{
        InputStream in = new FileInputStream(inFilename);
        Writer out = new OutputStreamWriter(new FileOutputStream(outFilename),charset);
        write(in,out);
        in.close();
        out.close();
    }
    
    /**
     * Format a INputStream
     * 
     * @param input
     * @param writer
     * @throws IOException
     */
    public void write(InputStream input,Writer writer)  throws IOException{
        try {
            this.writer = writer;
            XMLInputFactory xmlif = XMLInputFactory.newInstance();
            xmlr = xmlif.createXMLStreamReader("filename", input);
            while (xmlr.hasNext()) {
                next(xmlr);
                xmlr.next();
            }
            writer.flush();
        } catch (FileNotFoundException | XMLStreamException e) {
            throw new ReaderRuntimeException("Failed to process file", e);
        }

    }

    
    
    private void next(XMLStreamReader xmlr)  throws IOException{
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

    private void nextText(XMLStreamReader xmlr)  throws IOException{
        int start = xmlr.getTextStart();
        int length = xmlr.getTextLength();
        writer.write(new String(xmlr.getTextCharacters(), start, length).replaceAll("\\n",""));
    }

    private void nextEndElement(XMLStreamReader xmlr) throws IOException {
        if (xmlr.hasName()) {
            decDepth();
            if (!leaf) {
                writeTabs(depth+1);
            }
            writer.write("</"+ xmlr.getLocalName() + ">");
        }
    }

    private void nextStartElement(XMLStreamReader xmlr)  throws IOException{
        incDepth();
        writeTabs(depth);
        writer.write("<"+ xmlr.getLocalName());
        bearbeiteAttribute(xmlr);
        writer.write(">");
    }

    private void bearbeiteAttribute(XMLStreamReader xmlr)  throws IOException{
        for (int i = 0; i < xmlr.getAttributeCount(); i++) {
            bearbeiteAttribut(xmlr, i);
        }
    }

    private void bearbeiteAttribut(XMLStreamReader xmlr, int index)  throws IOException{
        String localName = xmlr.getAttributeLocalName(index);
        writer.write(" " + localName + "=\"" + xmlr.getAttributeValue(index)+ "\" ");
    }

}
