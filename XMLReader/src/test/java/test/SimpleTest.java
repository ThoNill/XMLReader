package test;

import static org.junit.Assert.fail;
import janus.reader.Formater;
import janus.reader.Reader;
import janus.reader.TagReader;
import janus.reader.actions.CurrentObject;
import janus.reader.actions.ElementNameStack;
import janus.reader.actions.NamedActionMap;
import janus.reader.actions.SetAction;
import janus.reader.actions.Value;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleTest {
    static private Logger log = LoggerFactory.getLogger(Value.class);

    private static final String KONTOAUSZUG_XML = "src/test/resources/kontoauszug.xml";
    private static final String KONTOAUSZUG_XML2 = "src/test/resources/kontoauszug2.xml";
    
    @Test
    public void methodHandle() {
        CurrentObject current = new CurrentObject();
        Value value = new Value(TObject.class, current);
        value.push();
        SetAction action = value.createSetAction("Name");
        action.setValue("Test");

        value.pop();
        TObject testObject = (TObject) current.next();

        Assert.assertEquals("Test", testObject.getName());

    }

    @Test
    public void stringStack() {

        CurrentObject current = new CurrentObject();
        Value value = new Value(TObject.class, current);
        NamedActionMap map = new NamedActionMap();

        ElementNameStack stack = new ElementNameStack(current, map);
        stack.addAction("/first/is", value);
        stack.addAction("/first/is/name", value.createSetAction("Name"));

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

        CurrentObject current = new CurrentObject();
        Value value = new Value(TObject.class, current);
        SetAction iSetter = value.createSetAction("Nummer");
        SetAction bSetter = value.createSetAction("bValue");
        SetAction fSetter = value.createSetAction("fValue");
        SetAction dSetter = value.createSetAction("dValue");
        SetAction lSetter = value.createSetAction("lValue");

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
    public void reader()  {
        Reader reader = new Reader();

        reader.read(KONTOAUSZUG_XML);

    }

    @Test
    public void tagReader()  {
        try {
            TagReader reader = new TagReader();
            
            reader.read(KONTOAUSZUG_XML);
            reader.next();
            System.out.print(reader.source("reader", "Const"));
        } catch (XMLStreamException e) {
            log.error("Unerwartete Ausnahem,",e);
            fail("Unerwartete Ausnahme");
        }

    }

    @Test
    public void readerGesetzt()  {
        Reader reader = new Reader();

        reader.addValue(Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,
                TObject.class);
        reader.read(KONTOAUSZUG_XML);
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TObject);
    }

    @Test
    public void readerGesetztMitName() {
        Reader reader = new Reader();

        reader.addValue(Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,
                TObject.class);
        reader.addSetter(
                Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,
                Const.TxId_Refs_TxDtls_NtryDtls_Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,
                "Name");

        reader.read(KONTOAUSZUG_XML);
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TObject);
        Assert.assertEquals("ICFSCTJJMMTT0000000DE00000000012346",
                ((TObject) o).getName());
    }

    @Test
    public void readerGesetztMitAttribut()  {
        Reader reader = new Reader();

        reader.addValue(Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,
                TObject.class);
        reader.addSetter(Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,
                Const.AtCcy_Amt_Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,
                "Name");

        reader.read(KONTOAUSZUG_XML);
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TObject);
        Assert.assertEquals("EUR", ((TObject) o).getName());
    }

    @Test
    public void readerGesetztSuperKlasse()  {

        Reader reader = new Reader();

        reader.addValue(Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,
                TObject.class);
        reader.addSetter(Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,
                Const.AtCcy_Amt_Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,
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

        reader.addValue(Const.BkToCstmrDbtCdtNtfctn_Document,
                TObject.class);
        reader.addSetter(Const.BkToCstmrDbtCdtNtfctn_Document,
                Const.MsgId_GrpHdr_BkToCstmrDbtCdtNtfctn_Document,
                "Verwendungszweck");

        reader.addValue(Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,
                TObject.class);
        reader.addSetter(Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,
                Const.AtCcy_Amt_Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,
                "Verwendungszweck");

        reader.read(KONTOAUSZUG_XML);
        reader.next();
        
        Object ov = reader.getValueObject(Const.BkToCstmrDbtCdtNtfctn_Document);
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
    public void readStatischAnnotierteKlasse()  {
        Reader reader = new Reader(TStaticAnnotated.class);
        reader.read(KONTOAUSZUG_XML);
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TStaticAnnotated);
        Assert.assertEquals("EUR", ((TStaticAnnotated) o).getName());
        Assert.assertEquals("Thomas", ((TStaticAnnotated) o).getVorName());
    }

    
    @Test
    public void falschAnnotierteKlasse()  {
        try {
            new Reader(TObject.class);
            fail("Keine Ausnahme");
        } catch (Exception ex) {
            log.info("Expected exception", ex);
        }
    }

    
    @Test
    public void falschAnnotierteMethode()  {
        try {
            new Reader(TFalschAnnotiert.class);
            fail("Keine Ausnahme");
        } catch (Exception ex) {
            log.info("Expected exception", ex);
        }
    }

    
    @Test
    public void format()  {
        Formater reader = new Formater("   ");
        try {
            reader.write(KONTOAUSZUG_XML);
        } catch (IOException ex) {
            log.info("unexpected Exception",ex);
            fail("unexpected Exception");
        }
    }

}
