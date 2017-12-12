package janus.reader;

import janus.reader.exceptions.ReaderRuntimeException;
import janus.reader.nls.Messages;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

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
public class Formater extends BasisReader {
    private XMLStreamReader xmlr;
    private int lastDepth = 0;
    private int depth = 0;
    private boolean leaf = false;
    private String tab;
    private Writer writer;

    /**
     * Formater with tab as tabulator String
     *
     * @param tab
     */
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
        leaf = lastDepth == depth;

    }

    private void writeTabs(int depth) throws IOException {
        writer.write("\n");
        for (int i = 0; i < depth; i++) {
            writer.write(tab);
        }
    }

    /**
     * Write to System.out
     * 
     * @param inFilename
     *            Name of the file that is reading
     * 
     */
    public void write(String inFilename) throws IOException {
        InputStream in = new FileInputStream(inFilename);
        Writer out = new OutputStreamWriter(System.out, "UTF8");
        write(in, out);
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
    public void write(String inFilename, String outFilename, String charset)
            throws IOException {
        InputStream in = new FileInputStream(inFilename);
        Writer out = new OutputStreamWriter(new FileOutputStream(outFilename),
                charset);
        write(in, out);
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
    public void write(InputStream input, Writer writer) throws IOException {
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
            Messages.throwReaderRuntimeException(e,"Runtime.FILE_PROCESSING");
        }

    }

    protected void nextText(XMLStreamReader xmlr) {
        int start = xmlr.getTextStart();
        int length = xmlr.getTextLength();
        try {
            writer.write(new String(xmlr.getTextCharacters(), start, length)
                    .replaceAll("\\n", ""));
        } catch (IOException e) {
            throw new ReaderRuntimeException(e);
        }
    }

    protected void nextEndElement(XMLStreamReader xmlr) {
        try {
            if (xmlr.hasName()) {
                decDepth();
                if (!leaf) {
                    writeTabs(depth + 1);
                }
                writer.write("</" + xmlr.getLocalName() + ">");
            }
        } catch (IOException e) {
            throw new ReaderRuntimeException(e);
        }
    }

    protected void nextStartElement(XMLStreamReader xmlr)  {
        try {
            incDepth();
            writeTabs(depth);
            writer.write("<" + xmlr.getLocalName());
            bearbeiteAttribute(xmlr);
            writer.write(">");
        } catch (IOException e) {
            throw new ReaderRuntimeException(e);
        }
    }

    private void bearbeiteAttribute(XMLStreamReader xmlr) throws IOException {
        for (int i = 0; i < xmlr.getAttributeCount(); i++) {
            bearbeiteAttribut(xmlr, i);
        }
    }

    private void bearbeiteAttribut(XMLStreamReader xmlr, int index)
            throws IOException {
        String localName = xmlr.getAttributeLocalName(index);
        writer.write(" " + localName + "=\"" + xmlr.getAttributeValue(index)
                + "\" ");
    }

    @Override
    protected void processAttribute(XMLStreamReader xmlr, int i) {
      //  no implementation needed
    }

}
