package janus.reader.test;

import static org.junit.Assert.fail;
import janus.reader.Formater;
import janus.reader.Reader;
import janus.reader.TagReader;
import janus.reader.adapters.BooleanAdapter;
import janus.reader.adapters.DoubleAdapter;
import janus.reader.adapters.FloatAdapter;
import janus.reader.adapters.IntegerAdapter;
import janus.reader.adapters.LongAdapter;
import janus.reader.attribute.Attribute;
import janus.reader.core.ValuesAndAttributesContainer;
import janus.reader.exceptions.ReaderRuntimeException;
import janus.reader.helper.ClassHelper;
import janus.reader.nls.Messages;
import janus.reader.path.XmlElementPath;
import janus.reader.test.entities.Child;
import janus.reader.test.entities.City;
import janus.reader.test.entities.DeepChild;
import janus.reader.test.entities.DeepCity;
import janus.reader.test.entities.LinkChild;
import janus.reader.test.entities.TAnnotated;
import janus.reader.test.entities.TObject;
import janus.reader.test.entities.TStaticAnnotated;
import janus.reader.test.entities.TWrongAnnotated;
import janus.reader.test.entities.TWrongFunctionNameAnnotated;
import janus.reader.test.entities.TWrongFunctionParameterCountAnnotated;
import janus.reader.test.entities.TWrongStaticFunctionParameterCountAnnotated;
import janus.reader.test.entities.TWrongStaticFunctionReturnWrongTypeAnnotated;
import janus.reader.value.CurrentObject;
import janus.reader.value.SimpleCurrentObject;
import janus.reader.value.StackCurrentObject;
import janus.reader.value.Value;
import janus.reader.value.ValueMap;

import java.io.IOException;
import java.text.ParseException;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLStreamException;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlTest {
    private static final Logger log = LoggerFactory.getLogger(Value.class);

    private static final String KONTOAUSZUG_XML = "src/test/resources/kontoauszug.xml";
    private static final String KONTOAUSZUG_XML2 = "src/test/resources/kontoauszug2.xml";
    private static final String CHILDS_XML = "src/test/resources/childs.xml";
    private static final String WRONG_XML = "src/test/resources/wrongChilds.xml";

    private static final String CHILDSANDCITY_XML = "src/test/resources/childsAndCity.xml";
    private static final String DEEPCHILDS_XML = "src/test/resources/deepChildsAndCity.xml";
   
    @Test
    public void readerInitialization() {
        Reader reader = new Reader();

        reader.read(KONTOAUSZUG_XML);

    }

    @Test
    public void tagReader() {
        try {
            TagReader reader = new TagReader();

            reader.read(KONTOAUSZUG_XML);
            reader.read();
            System.out.print(reader.source("reader", "Const"));
        } catch (XMLStreamException e) {
            log.error("Unerwartete Ausnahme,", e);
            fail("Unerwartete Ausnahme");
        }

    }

    @Test
    public void reader() {
        Reader reader = new Reader();

        reader.addValue(new XmlElementPath(
                Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document),
                TObject.class);
        reader.read(KONTOAUSZUG_XML);
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TObject);
    }

    @Test
    public void readerWithName() {
        Reader reader = new Reader();

        reader.addValue(new XmlElementPath(
                Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document),
                TObject.class);
        reader.addSetter(
                new XmlElementPath(Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document),
                new XmlElementPath(
                        Const.TxId_Refs_TxDtls_NtryDtls_Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document),
                "Name");

        reader.read(KONTOAUSZUG_XML);
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TObject);
        Assert.assertEquals("ICFSCTJJMMTT0000000DE00000000012346",
                ((TObject) o).getName());
    }

    @Test
    public void readerWithAttribute() {
        Reader reader = new Reader();

        reader.addValue(new XmlElementPath(
                Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document),
                TObject.class);
        reader.addSetter(new XmlElementPath(
                Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document), new XmlElementPath(
                Const.AtCcy_Amt_Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document),
                "Name");

        reader.read(KONTOAUSZUG_XML);
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TObject);
        Assert.assertEquals("EUR", ((TObject) o).getName());
    }

    @Test
    public void readerWithSuperClass() {

        Reader reader = new Reader();

        reader.addValue(new XmlElementPath(
                Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document),
                TObject.class);
        reader.addSetter(new XmlElementPath(
                Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document), new XmlElementPath(
                Const.AtCcy_Amt_Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document),
                "Verwendungszweck");

        reader.read(KONTOAUSZUG_XML);

        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TObject);
        Assert.assertEquals("EUR", ((TObject) o).getVerwendungszweck());

    }

    @Test
    public void readerParent() {

        Reader reader = new Reader();

        reader.addValue(new XmlElementPath(Const.BkToCstmrDbtCdtNtfctn_Document),
                TObject.class);
        reader.addSetter(new XmlElementPath(Const.BkToCstmrDbtCdtNtfctn_Document),
                new XmlElementPath(Const.MsgId_GrpHdr_BkToCstmrDbtCdtNtfctn_Document),
                "Verwendungszweck");

        reader.addValue(new XmlElementPath(
                Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document),
                TObject.class);
        reader.addSetter(new XmlElementPath(
                Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document), new XmlElementPath(
                Const.AtCcy_Amt_Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document),
                "Verwendungszweck");

        reader.read(KONTOAUSZUG_XML);
        reader.next();

        Object ov = reader.getValueObject(new XmlElementPath(
                Const.BkToCstmrDbtCdtNtfctn_Document));
        Assert.assertNotNull(ov);
        Assert.assertTrue(ov instanceof TObject);
        Assert.assertEquals("MSG1", ((TObject) ov).getVerwendungszweck());

    }

    @Test
    public void readAnnotatedClass() {
        Reader reader = new Reader(TAnnotated.class);
        reader.read(KONTOAUSZUG_XML);
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TAnnotated);
        Assert.assertEquals("EUR", ((TAnnotated) o).getName());
    }

    @Test
    public void readAnnotatedClass2() {
        Reader reader = new Reader(TAnnotated.class);
        reader.read(KONTOAUSZUG_XML2);
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TAnnotated);
        Assert.assertEquals("Thomas", ((TAnnotated) o).getName());
    }

    @Test
    public void readStaticAnnotatedClass() {
        Reader reader = new Reader(TStaticAnnotated.class);
        reader.read(KONTOAUSZUG_XML);
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TStaticAnnotated);
        Assert.assertEquals("EUR", ((TStaticAnnotated) o).getName());
        Assert.assertEquals("Thomas", ((TStaticAnnotated) o).getVorName());
    }

   
    @Test
    public void format() {
        Formater reader = new Formater("   ");
        try {
            reader.write(KONTOAUSZUG_XML);
            reader.write(KONTOAUSZUG_XML, "test.erg", "UTF-8");
        } catch (IOException ex) {
            log.info("unexpected Exception", ex);
            fail("unexpected Exception");
        }
    }

    @Test
    public void readWrongXml() {
        Reader reader = new Reader(Child.class);
        try {
            reader.read(WRONG_XML);
            while (reader.hasNext()) {
                reader.next();
            }
            Assert.fail("Exception erwartet");
        } catch (ReaderRuntimeException e) {
            log.error("Erwartete Ausnahme", e);
        } catch (Exception e) {
            log.error("Unerwartete Ausnahme", e);
            Assert.fail("Unerwartete Exception");
        }
    }

    @Test
    public void readWrongXml2() {
        TagReader reader = new TagReader();
        try {
            reader.read(WRONG_XML);
            reader.read();
            Assert.fail("Exception erwartet");
        } catch (ReaderRuntimeException e) {
            log.error("Erwartete Ausnahme", e);
        } catch (Exception e) {
            log.error("Unerwartete Ausnahme", e);
            Assert.fail("Unerwartete Exception");
        }
    }

    @Test
    public void readChilds() {
        Reader reader = new Reader(Child.class);
        reader.read(CHILDS_XML);
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof Child);
        Assert.assertEquals("Vera", ((Child) o).getName());

        Assert.assertTrue(reader.hasNext());
        o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof Child);
        Assert.assertEquals("Thomas", ((Child) o).getName());

        Assert.assertTrue(reader.hasNext());
        o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof Child);
        Assert.assertEquals("Hans", ((Child) o).getName());

        try {
            o = reader.next();
            Assert.fail("Exception erwartet");
        } catch (NoSuchElementException e) {
            log.error("Erwartete Ausnahme", e);
        } catch (Exception e) {
            log.error("Unerwartete Ausnahme", e);
            Assert.fail("Unerwartete Exception");
        }
        Assert.assertFalse(reader.hasNext());
    }

    @Test
    public void readLinkChilds() {
        Reader reader = new Reader(LinkChild.class);
        reader.read(CHILDS_XML);
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof LinkChild);
        Assert.assertEquals("Vera", ((LinkChild) o).getName());

        o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof LinkChild);
        Assert.assertEquals("Thomas", ((LinkChild) o).getName());

        o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof LinkChild);
        Assert.assertEquals("Hans", ((LinkChild) o).getName());

    }

    @Test
    public void readLinkChildsAndCity() {
        Reader reader = new Reader(LinkChild.class, City.class);
        reader.read(CHILDSANDCITY_XML);

        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof City);
        Assert.assertEquals("A", ((City) o).getName());

        o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof City);
        Assert.assertEquals("C", ((City) o).getName());

        o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof City);
        Assert.assertEquals("D", ((City) o).getName());

        o = reader.next();
        Assert.assertTrue(o instanceof LinkChild);
        Assert.assertEquals("Vera", ((LinkChild) o).getName());
        Assert.assertEquals("C", ((LinkChild) o).getCity().getName());
        Assert.assertEquals("D", ((LinkChild) o).getSecond().getName());

        o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof LinkChild);
        Assert.assertEquals("Thomas", ((LinkChild) o).getName());

        o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof LinkChild);
        Assert.assertEquals("Hans", ((LinkChild) o).getName());
        Assert.assertEquals("A", ((LinkChild) o).getCity().getName());

        Assert.assertEquals("Vera", ((LinkChild) o).getChild().getChild()
                .getName());
    }


    @Test
    public void readDeepChildsAndCity() {
        Reader reader = new Reader(DeepChild.class, DeepCity.class);
        reader.read(DEEPCHILDS_XML);

        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof DeepCity);
        Assert.assertEquals("A", ((DeepCity) o).getName());

        o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof DeepCity);
        Assert.assertEquals("C", ((DeepCity) o).getName());

        o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof DeepCity);
        Assert.assertEquals("D", ((DeepCity) o).getName());

        o = reader.next();
        Assert.assertTrue(o instanceof DeepChild);
        Assert.assertEquals("Vera", ((DeepChild) o).getName());
        Assert.assertEquals("C", ((DeepChild) o).getCity().getName());
        Assert.assertEquals("D", ((DeepChild) o).getSecond().getName());

        o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof DeepChild);
        Assert.assertEquals("Thomas", ((DeepChild) o).getName());

        o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof DeepChild);
        Assert.assertEquals("Hans", ((DeepChild) o).getName());
        Assert.assertEquals("A", ((DeepChild) o).getCity().getName());

        Assert.assertEquals("Vera", ((DeepChild) o).getChild().getChild()
                .getName());
    }

}
