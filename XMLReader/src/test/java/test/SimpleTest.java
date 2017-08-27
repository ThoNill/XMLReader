package test;
import static org.junit.Assert.fail;
import janus.reader.Reader;
import janus.reader.TagReader;
import janus.reader.actions.CurrentObject;
import janus.reader.actions.NamedActionMap;
import janus.reader.actions.SetAction;
import janus.reader.actions.Value;
import janus.reader.core.StringStack;

import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamException;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SimpleTest {
    static Logger LOG = LoggerFactory.getLogger(Value.class);


    
    private static final String KONTOAUSZUG_XML = "src/test/resources/kontoauszug.xml";

    @Test
    public void testCurrent() {
        CurrentObject current = new CurrentObject();
        Value value = new Value(TObject.class, current);
        value.push();
        SetAction action = value.createSetAction((s,v) -> ((TObject) s).setName(v));
        action.setValue("Test");
        
        value.pop();
        TObject testObject = (TObject)current.next();
        
        Assert.assertEquals("Test", testObject.getName());
        
        
    }
    
    @Test
    public void methodHandle() {
        CurrentObject current = new CurrentObject();
        Value value = new Value(TObject.class, current);
        value.push();
        SetAction action = value.createSetAction("Name");
        action.setValue("Test");
        
        value.pop();
        TObject testObject = (TObject)current.next();
        
        Assert.assertEquals("Test", testObject.getName());
        
        
    }
    
    @Test
    public void stringStack() {
        
        CurrentObject current = new CurrentObject();
        Value value = new Value(TObject.class, current);
        NamedActionMap map = new NamedActionMap();
        
        StringStack stack = new StringStack(current,map);
        stack.addAction("/first/is", value);
        stack.addAction("/first/is/name", value.createSetAction("Name"));
        
        stack.push("first");
        stack.push("is");
        stack.push("name");
        stack.setText("Test");
        stack.pop();
        stack.pop();
        
        TObject testObject = (TObject)current.next();
        
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
        TObject testObject = (TObject)current.next();
        Assert.assertEquals(66, testObject.getNummer());
        Assert.assertEquals(888l, testObject.getlValue());
        Assert.assertEquals(6.6d, testObject.getdValue(),0.01);
        Assert.assertEquals(6.6f, testObject.getfValue(),0.01);
        Assert.assertEquals(true, testObject.isbValue());
        
    }
    

    
    @Test
    public void reader() throws FileNotFoundException, XMLStreamException {
        Reader reader = new Reader();
        
        reader.read(KONTOAUSZUG_XML);
        
    }

    @Test
    public void tagReader() throws FileNotFoundException, XMLStreamException {
        TagReader reader = new TagReader();
        
        reader.read(KONTOAUSZUG_XML);
        reader.next();
        System.out.print(reader.source("reader","Const"));
        
    }
    
    @Test
    public void readerGesetzt() throws FileNotFoundException, XMLStreamException {
        Reader reader = new Reader();
        
        reader.addValue(Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,TObject.class);
        reader.read(KONTOAUSZUG_XML);
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TObject);
    }

    @Test
    public void readerGesetztMitName() throws FileNotFoundException, XMLStreamException {
        Reader reader = new Reader();
        
        reader.addValue(Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,TObject.class);
        reader.addSetter(Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,Const.TxId_Refs_TxDtls_NtryDtls_Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,"Name");
        
        reader.read(KONTOAUSZUG_XML);
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TObject);
        Assert.assertEquals("ICFSCTJJMMTT0000000DE00000000012346", ((TObject)o).getName());
    }

    
    @Test
    public void readerGesetztMitAttribut() throws FileNotFoundException, XMLStreamException {
        Reader reader = new Reader();
        
        reader.addValue(Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,TObject.class);
        reader.addSetter(Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,Const.AtCcy_Amt_Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,"Name");
        
        reader.read(KONTOAUSZUG_XML);
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TObject);
        Assert.assertEquals("EUR", ((TObject)o).getName());
    }

    @Test
    public void readerGesetztSuperKlasse() throws FileNotFoundException, XMLStreamException {
       
        Reader reader = new Reader();
        
        reader.addValue(Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,TObject.class);
        reader.addSetter(Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,Const.AtCcy_Amt_Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,"Verwendungszweck");
        
        reader.read(KONTOAUSZUG_XML);
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TObject);
        Assert.assertEquals("EUR", ((TObject)o).getVerwendungszweck());
    }
    
    @Test
    public void readAnnotierteKlasse() throws FileNotFoundException, XMLStreamException {
        Reader reader = new Reader(TAnnotated.class);
        reader.read(KONTOAUSZUG_XML);
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TAnnotated);
        Assert.assertEquals("EUR",((TAnnotated)o).getName());
    }
 
    @Test
    public void falschAnnotierteKlasse() throws FileNotFoundException, XMLStreamException {
        try {
            Reader reader = new Reader(TObject.class);
            fail("Keine Ausnahme");
        } catch(Exception ex) {
            LOG.info("Expected exception",ex);
        }
    }
    
    @Test
    public void falschAnnotierteMethode() throws FileNotFoundException, XMLStreamException {
        try {
            Reader reader = new Reader(TFalschAnnotiert.class);
            fail("Keine Ausnahme");
        } catch(Exception ex) {
            LOG.info("Expected exception",ex);
        }
    }
 

}
