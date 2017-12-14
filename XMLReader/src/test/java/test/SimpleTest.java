package test;

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

public class SimpleTest {
    private static final Logger log = LoggerFactory.getLogger(Value.class);

    private static final String KONTOAUSZUG_XML = "src/test/resources/kontoauszug.xml";
    private static final String KONTOAUSZUG_XML2 = "src/test/resources/kontoauszug2.xml";
    private static final String CHILDS_XML = "src/test/resources/childs.xml";
    private static final String WRONG_XML = "src/test/resources/wrongChilds.xml";

    private static final String CHILDSANDCITY_XML = "src/test/resources/childsAndCity.xml";

    @Test
    public void testClassHelper() {
        Assert.assertFalse(ClassHelper.isThisClassOrASuperClass(Void.class,
                Object.class));
        Assert.assertFalse(ClassHelper.isThisClassOrASuperClass(Object.class,
                Void.class));
        Assert.assertTrue(ClassHelper.isThisClassOrASuperClass(TObject.class,
                Object.class));
        Assert.assertTrue(ClassHelper.isThisClassOrASuperClass(Double.class,
                Number.class));
        Assert.assertFalse(ClassHelper.isThisClassOrASuperClass(Double.class,
                TObject.class));
    }

    @Test
    public void testMessages() {
        try {
            IllegalArgumentException cause = new IllegalArgumentException();
            Messages.throwReaderRuntimeException(cause,
                    "Runtime.FILE_PROCESSING");
            fail("Ausnahme erwartet");
        } catch (Exception ex) {
            log.error("Ausnahme erwartet");
        }
    }

    @Test
    public void testStartWithPaths() {
        XmlElementPath a = new XmlElementPath("/aa/bb/cc");
        XmlElementPath b = new XmlElementPath("/a");
        XmlElementPath c = new XmlElementPath("/aa");
        XmlElementPath d = new XmlElementPath("/aa/bb");
        Assert.assertTrue(a.startsWith(a));
        Assert.assertTrue(a.startsWith(c));
        Assert.assertTrue(a.startsWith(d));
        Assert.assertFalse(a.startsWith(b));
    }

    @Test
    public void testEndWithPaths() {
        XmlElementPath a = new XmlElementPath("/aa/bb/cc");
        XmlElementPath b = new XmlElementPath("/c");
        XmlElementPath c1 = new XmlElementPath("/cc");
        XmlElementPath d1 = new XmlElementPath("/bb/cc");
        XmlElementPath c2 = new XmlElementPath("cc");
        XmlElementPath d2 = new XmlElementPath("bb/cc");
        XmlElementPath d3 = new XmlElementPath("/ee/aa/bb/cc");
        XmlElementPath d4 = new XmlElementPath("/ee/ee/ee");
        Assert.assertTrue(a.endsWith(a));
        Assert.assertFalse(a.endsWith(c1));
        Assert.assertFalse(a.endsWith(d1));
        Assert.assertTrue(a.endsWith(c2));
        Assert.assertTrue(a.endsWith(d2));
        Assert.assertFalse(a.endsWith(d3));
        Assert.assertFalse(a.endsWith(d4));
    }

    @Test
    public void testNonExistentMessages() {
        try {
            IllegalArgumentException cause = new IllegalArgumentException();
            Messages.throwReaderRuntimeException(cause, "NotExists");
            fail("Ausnahme erwartet");
        } catch (Exception ex) {
            log.error("Ausnahme erwartet");
        }
    }

    @Test
    public void compareTest() {
        XmlElementPath absolut = new XmlElementPath("/a/b/c");
        XmlElementPath relativ = new XmlElementPath("b/c");

        Assert.assertTrue(absolut.isAbsolut());
        Assert.assertFalse(relativ.isAbsolut());

        Assert.assertTrue(relativ.compare(absolut));
        Assert.assertFalse(new XmlElementPath("a").compare(absolut));
        Assert.assertTrue(absolut.compare(absolut));
        Assert.assertFalse(new XmlElementPath("/a").compare(absolut));

    }

    @Test
    public void parentPathTest() {
        XmlElementPath child = new XmlElementPath("/a/b/cc");
        XmlElementPath parent = new XmlElementPath("/a/b");

        Assert.assertEquals(parent, child.parent());

    }

    @Test
    public void methodHandle() {
        CurrentObject current = new SimpleCurrentObject();
        Value value = new Value(new XmlElementPath("/value"), TObject.class, current,
                new SimpleCurrentObject());
        value.push();

        ValuesAndAttributesContainer stack = new ValuesAndAttributesContainer(current);

        Attribute action = stack.createSetAction(value,
                new XmlElementPath("/value/first"), "Name");
        action.setValue("Test");

        value.pop();
        TObject testObject = (TObject) current.next();

        Assert.assertEquals("Test", testObject.getName());

    }

    @Test
    public void methodHandleWithWrongValue() {
        CurrentObject current = new SimpleCurrentObject();
        Value value = new Value(new XmlElementPath("/value"), TObject.class, current,
                new SimpleCurrentObject());
        value.push();
        ValuesAndAttributesContainer stack = new ValuesAndAttributesContainer(current);

        Attribute action = stack.createSetAction(value,
                new XmlElementPath("/value/first"), "fValue");

        try {
            action.setValue("Test");
            fail("Ausnahme erwartet");
        } catch (ReaderRuntimeException e) {
            log.error("Erwartete Ausnahem", e);
        }
        value.pop();
    }

    @Test
    public void methodHandleWithEmptyValue() {
        CurrentObject current = new SimpleCurrentObject();
        Value value = new Value(new XmlElementPath("/value"), TObject.class, current,
                new SimpleCurrentObject());
        ValuesAndAttributesContainer stack = new ValuesAndAttributesContainer(current);

        Attribute action = stack.createSetAction(value,
                new XmlElementPath("/value/first"), "Name");

        try {
            action.setValue("Test");
            fail("Ausnahme erwartet");
        } catch (ReaderRuntimeException e) {
            log.error("Erwartete Ausnahem", e);
        }
    }

    
    @Test
    public void methodHandleWithStack() {
        CurrentObject current = new SimpleCurrentObject();
        Value value = new Value(new XmlElementPath("/value"), TObject.class, current,
                new StackCurrentObject());
        value.push();

        ValuesAndAttributesContainer stack = new ValuesAndAttributesContainer(current);

        Attribute action = stack.createSetAction(value, new XmlElementPath(
                "/value/first/is"), "Name");
        action.setValue("Test");

        value.pop();
        TObject testObject = (TObject) current.next();

        Assert.assertEquals("Test", testObject.getName());

    }

    @Test
    public void stringStack() {

        CurrentObject current = new SimpleCurrentObject();
        Value value = new Value(new XmlElementPath("/first/is"), TObject.class,
                current, new SimpleCurrentObject());
        ValueMap map = new ValueMap();

        ValuesAndAttributesContainer stack = new ValuesAndAttributesContainer(current, map);
        stack.addValue(value);
        stack.addSetter(stack.createSetAction(value, new XmlElementPath("name"),
                "Name"));

        stack.push("first");
        stack.push("is");
        stack.push("name");
        stack.setText("Test");
        stack.pop();
        stack.pop();

        TObject testObject = (TObject) current.next();

        Assert.assertEquals("Test", testObject.getName());

    }

    @Test
    public void adapterMarshallTests() {
        DoubleAdapter dAdapter = new DoubleAdapter();
        Assert.assertEquals("2.0", dAdapter.marshal(new Double(2.0)));

        FloatAdapter fAdapter = new FloatAdapter();
        Assert.assertEquals("2.0", fAdapter.marshal(new Float(2.0)));

        IntegerAdapter iAdapter = new IntegerAdapter();
        Assert.assertEquals("2", iAdapter.marshal(new Integer(2)));

        LongAdapter lAdapter = new LongAdapter();
        Assert.assertEquals("2", lAdapter.marshal(new Long(2L)));

        BooleanAdapter bAdapter = new BooleanAdapter();
        Assert.assertEquals("true", bAdapter.marshal(Boolean.TRUE));

    }

    @Test
    public void adapterUnmarshallTests() {
        try {
            DoubleAdapter dAdapter = new DoubleAdapter();
            Assert.assertEquals(new Double(2.0), dAdapter.unmarshal("2.0"));

            FloatAdapter fAdapter = new FloatAdapter();
            Assert.assertEquals(new Float(2.0), fAdapter.unmarshal("2.0"));

            IntegerAdapter iAdapter = new IntegerAdapter();
            Assert.assertEquals(new Integer(2), iAdapter.unmarshal("2"));

            LongAdapter lAdapter = new LongAdapter();
            Assert.assertEquals(new Long(2L), lAdapter.unmarshal("2"));

            BooleanAdapter bAdapter = new BooleanAdapter();
            Assert.assertEquals(Boolean.TRUE, bAdapter.unmarshal("true"));
            Assert.assertEquals(Boolean.FALSE, bAdapter.unmarshal("false"));
        } catch (ParseException e) {
            log.error("Unerwartete Ausnahme {}", e);
            fail("Unerwartete Ausnahme");
        }

    }

    @Test
    public void adapter() {

        CurrentObject current = new SimpleCurrentObject();
        Value value = new Value(new XmlElementPath("/first"), TObject.class, current,
                new SimpleCurrentObject());

        ValuesAndAttributesContainer stack = new ValuesAndAttributesContainer(current);

        Attribute iSetter = stack.createSetAction(value, new XmlElementPath("/first/n"),
                "Nummer");
        Attribute bSetter = stack.createSetAction(value, new XmlElementPath("/first/b"),
                "bValue");
        Attribute fSetter = stack.createSetAction(value, new XmlElementPath("/first/f"),
                "fValue");
        Attribute dSetter = stack.createSetAction(value, new XmlElementPath("/first/d"),
                "dValue");
        Attribute lSetter = stack.createSetAction(value, new XmlElementPath("/first/l"),
                "lValue");

        value.push();
        iSetter.setValue("66");
        lSetter.setValue("888");
        fSetter.setValue("6.6");
        dSetter.setValue("6.6");
        bSetter.setValue("true");

        value.pop();
        TObject testObject = (TObject) current.next();
        Assert.assertEquals(66, testObject.getNummer());
        Assert.assertEquals(888l, testObject.getlValue());
        Assert.assertEquals(6.6d, testObject.getdValue(), 0.01);
        Assert.assertEquals(6.6f, testObject.getfValue(), 0.01);
        Assert.assertEquals(true, testObject.isbValue());

    }

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
    public void wrongAnnotatedClass() {
        try {
            new Reader(TObject.class);
            fail("Keine Ausnahme");
        } catch (Exception ex) {
            log.info("Expected exception", ex);
        }
    }

    @Test
    public void wrongAnnotatedMethod() {
        try {
            new Reader(TWrongAnnotated.class);
            fail("Keine Ausnahme");
        } catch (Exception ex) {
            log.info("Expected exception", ex);
        }
    }

    @Test
    public void wrongFunctionName() {
        try {
            new Reader(TWrongFunctionNameAnnotated.class);
            fail("Keine Ausnahme");
        } catch (Exception ex) {
            log.info("Expected exception", ex);
        }
    }

    @Test
    public void wrongFunctionParameterCountName() {
        try {
            new Reader(TWrongFunctionParameterCountAnnotated.class);
            fail("Keine Ausnahme");
        } catch (Exception ex) {
            log.info("Expected exception", ex);
        }
    }

    @Test
    public void wrongStaticFunctionParameterCount() {
        try {
            new Reader(TWrongStaticFunctionParameterCountAnnotated.class);
            fail("Keine Ausnahme");
        } catch (Exception ex) {
            log.info("Expected exception", ex);
        }
    }

    @Test
    public void wrongStaticFunctionReturnType() {
        try {
            new Reader(TWrongStaticFunctionReturnWrongTypeAnnotated.class);
            fail("Keine Ausnahme");
        } catch (Exception ex) {
            log.info("Expected exception", ex);
        }
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

}
