package test;

import static org.junit.Assert.fail;
import janus.reader.Formater;
import janus.reader.Reader;
import janus.reader.TagReader;
import janus.reader.actions.CurrentObject;
import janus.reader.actions.ElementNameStack;
import janus.reader.actions.Setter;
import janus.reader.actions.SimpleCurrentObject;
import janus.reader.actions.StackCurrentObject;
import janus.reader.actions.TagPath;
import janus.reader.actions.Value;
import janus.reader.actions.ValueMap;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleTest {
    private static final Logger log = LoggerFactory.getLogger(Value.class);

    private static final String KONTOAUSZUG_XML = "src/test/resources/kontoauszug.xml";
    private static final String KONTOAUSZUG_XML2 = "src/test/resources/kontoauszug2.xml";
    private static final String CHILDS_XML = "src/test/resources/childs.xml";
    private static final String CHILDSANDCITY_XML = "src/test/resources/childsAndCity.xml";

    @Test
    public void compareTest() {
        TagPath absolut = new TagPath("/a/b/c");
        TagPath relativ = new TagPath("b/c");

        Assert.assertTrue(absolut.isAbsolut());
        Assert.assertFalse(relativ.isAbsolut());

        Assert.assertTrue(relativ.compare(absolut));
        Assert.assertFalse(new TagPath("a").compare(absolut));
        Assert.assertTrue(absolut.compare(absolut));
        Assert.assertFalse(new TagPath("/a").compare(absolut));

    }

    @Test
    public void parentPathTest() {
        TagPath child = new TagPath("/a/b/cc");
        TagPath parent = new TagPath("/a/b");

        Assert.assertEquals(parent, child.parent());

    }

    @Test
    public void methodHandle() {
        CurrentObject current = new SimpleCurrentObject();
        Value value = new Value(new TagPath("/value"), TObject.class, current,
                new SimpleCurrentObject());
        value.push();

        ElementNameStack stack = new ElementNameStack(current);

        Setter action = stack.createSetAction(value,
                new TagPath("/value/first"), "Name");
        action.setValue("Test");

        value.pop();
        TObject testObject = (TObject) current.next();

        Assert.assertEquals("Test", testObject.getName());

    }

    @Test
    public void methodHandleWithStack() {
        CurrentObject current = new SimpleCurrentObject();
        Value value = new Value(new TagPath("/value"), TObject.class, current,
                new StackCurrentObject());
        value.push();

        ElementNameStack stack = new ElementNameStack(current);

        Setter action = stack.createSetAction(value, new TagPath(
                "/value/first/is"), "Name");
        action.setValue("Test");

        value.pop();
        TObject testObject = (TObject) current.next();

        Assert.assertEquals("Test", testObject.getName());

    }

    @Test
    public void stringStack() {

        CurrentObject current = new SimpleCurrentObject();
        Value value = new Value(new TagPath("/first/is"), TObject.class,
                current, new SimpleCurrentObject());
        ValueMap map = new ValueMap();

        ElementNameStack stack = new ElementNameStack(current, map);
        stack.addValue(value);
        stack.addSetter(stack.createSetAction(value, new TagPath("name"),
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
    public void adapter() {

        CurrentObject current = new SimpleCurrentObject();
        Value value = new Value(new TagPath("/first"), TObject.class, current,
                new SimpleCurrentObject());

        ElementNameStack stack = new ElementNameStack(current);

        Setter iSetter = stack.createSetAction(value, new TagPath("/first/n"),
                "Nummer");
        Setter bSetter = stack.createSetAction(value, new TagPath("/first/b"),
                "bValue");
        Setter fSetter = stack.createSetAction(value, new TagPath("/first/f"),
                "fValue");
        Setter dSetter = stack.createSetAction(value, new TagPath("/first/d"),
                "dValue");
        Setter lSetter = stack.createSetAction(value, new TagPath("/first/l"),
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
    public void reader() {
        Reader reader = new Reader();

        reader.read(KONTOAUSZUG_XML);

    }

    @Ignore
    @Test
    public void tagReader() {
        try {
            TagReader reader = new TagReader();

            reader.read(KONTOAUSZUG_XML);
            reader.next();
            System.out.print(reader.source("reader", "Const"));
        } catch (XMLStreamException e) {
            log.error("Unerwartete Ausnahem,", e);
            fail("Unerwartete Ausnahme");
        }

    }

    @Test
    public void readerGesetzt() {
        Reader reader = new Reader();

        reader.addValue(new TagPath(
                Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document),
                TObject.class);
        reader.read(KONTOAUSZUG_XML);
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TObject);
    }

    @Test
    public void readerGesetztMitName() {
        Reader reader = new Reader();

        reader.addValue(new TagPath(
                Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document),
                TObject.class);
        reader.addSetter(
                new TagPath(Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document),
                new TagPath(
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
    public void readerGesetztMitAttribut() {
        Reader reader = new Reader();

        reader.addValue(new TagPath(
                Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document),
                TObject.class);
        reader.addSetter(new TagPath(
                Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document), new TagPath(
                Const.AtCcy_Amt_Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document),
                "Name");

        reader.read(KONTOAUSZUG_XML);
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TObject);
        Assert.assertEquals("EUR", ((TObject) o).getName());
    }

    @Test
    public void readerGesetztSuperKlasse() {

        Reader reader = new Reader();

        reader.addValue(new TagPath(
                Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document),
                TObject.class);
        reader.addSetter(new TagPath(
                Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document), new TagPath(
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

        reader.addValue(new TagPath(Const.BkToCstmrDbtCdtNtfctn_Document),
                TObject.class);
        reader.addSetter(new TagPath(Const.BkToCstmrDbtCdtNtfctn_Document),
                new TagPath(Const.MsgId_GrpHdr_BkToCstmrDbtCdtNtfctn_Document),
                "Verwendungszweck");

        reader.addValue(new TagPath(
                Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document),
                TObject.class);
        reader.addSetter(new TagPath(
                Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document), new TagPath(
                Const.AtCcy_Amt_Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document),
                "Verwendungszweck");

        reader.read(KONTOAUSZUG_XML);
        reader.next();

        Object ov = reader.getValueObject(new TagPath(
                Const.BkToCstmrDbtCdtNtfctn_Document));
        Assert.assertNotNull(ov);
        Assert.assertTrue(ov instanceof TObject);
        Assert.assertEquals("MSG1", ((TObject) ov).getVerwendungszweck());

    }

    @Test
    public void readAnnotierteKlasse() {
        Reader reader = new Reader(TAnnotated.class);
        reader.read(KONTOAUSZUG_XML);
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TAnnotated);
        Assert.assertEquals("EUR", ((TAnnotated) o).getName());
    }

    @Test
    public void readAnnotierteKlasse2() {
        Reader reader = new Reader(TAnnotated.class);
        reader.read(KONTOAUSZUG_XML2);
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TAnnotated);
        Assert.assertEquals("Thomas", ((TAnnotated) o).getName());
    }

    @Test
    public void readStatischAnnotierteKlasse() {
        Reader reader = new Reader(TStaticAnnotated.class);
        reader.read(KONTOAUSZUG_XML);
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TStaticAnnotated);
        Assert.assertEquals("EUR", ((TStaticAnnotated) o).getName());
        Assert.assertEquals("Thomas", ((TStaticAnnotated) o).getVorName());
    }

    @Test
    public void falschAnnotierteKlasse() {
        try {
            new Reader(TObject.class);
            fail("Keine Ausnahme");
        } catch (Exception ex) {
            log.info("Expected exception", ex);
        }
    }

    @Test
    public void falschAnnotierteMethode() {
        try {
            new Reader(TFalschAnnotiert.class);
            fail("Keine Ausnahme");
        } catch (Exception ex) {
            log.info("Expected exception", ex);
        }
    }

    @Ignore
    @Test
    public void format() {
        Formater reader = new Formater("   ");
        try {
            reader.write(KONTOAUSZUG_XML);
        } catch (IOException ex) {
            log.info("unexpected Exception", ex);
            fail("unexpected Exception");
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

        o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof Child);
        Assert.assertEquals("Thomas", ((Child) o).getName());

        o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof Child);
        Assert.assertEquals("Hans", ((Child) o).getName());

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
