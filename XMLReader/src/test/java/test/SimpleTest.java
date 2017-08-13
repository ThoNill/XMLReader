package test;
import java.awt.image.TileObserver;
import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamException;

import janus.reader.CurrentObject;
import janus.reader.NamedActionMap;
import janus.reader.Reader;
import janus.reader.SetAction;
import janus.reader.StringStack;
import janus.reader.TagReader;
import janus.reader.Value;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;


public class SimpleTest {

    
    @Test
    public void testCurrent() {
        CurrentObject current = new CurrentObject();
        Value value = new Value(TObject.class, current);
        value.push();
        SetAction action = value.createSetAction((s,v) -> ((TObject) s).setName(v));
        action.setValue("Test");
        
        value.pop();
        TObject testObject = (TObject)current.getCurrent();
        
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
        TObject testObject = (TObject)current.getCurrent();
        
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
        
        TObject testObject = (TObject)current.getCurrent();
        
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
        TObject testObject = (TObject)current.getCurrent();
        Assert.assertEquals(66, testObject.getNummer());
        Assert.assertEquals(888l, testObject.getlValue());
        Assert.assertEquals(6.6d, testObject.getdValue(),0.01);
        Assert.assertEquals(6.6f, testObject.getfValue(),0.01);
        Assert.assertEquals(true, testObject.isbValue());
        
    }
    

    
    @Test
    public void reader() throws FileNotFoundException, XMLStreamException {
        Reader reader = new Reader();
        
        reader.read("resources/kontoauszug.xml");
        reader.next();
        
    }

    @Test
    public void tagReader() throws FileNotFoundException, XMLStreamException {
        TagReader reader = new TagReader();
        
        reader.read("resources/kontoauszug.xml");
        reader.next();
        System.out.print(reader.source("reader","Const"));
        
    }
    
    @Test
    public void readerGesetzt() throws FileNotFoundException, XMLStreamException {
        Reader reader = new Reader();
        
        reader.addValue(Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,TObject.class);
        reader.read("resources/kontoauszug.xml");
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TObject);
    }

    @Test
    public void readerGesetztMitName() throws FileNotFoundException, XMLStreamException {
        Reader reader = new Reader();
        
        reader.addValue(Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,TObject.class);
        reader.addSetter(Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,Const.TxId_Refs_TxDtls_NtryDtls_Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document,"Name");
        
        reader.read("resources/kontoauszug.xml");
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
        
        reader.read("resources/kontoauszug.xml");
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
        
        reader.read("resources/kontoauszug.xml");
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TObject);
        Assert.assertEquals("EUR", ((TObject)o).getVerwendungszweck());
    }
    
    @Test
    public void readAnnotierteKlasse() throws FileNotFoundException, XMLStreamException {
        Reader reader = new Reader(TAnnotated.class);
        reader.read("resources/kontoauszug.xml");
        Object o = reader.next();
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof TAnnotated);
        Assert.assertEquals("EUR",((TAnnotated)o).getName());
    }
    

}
