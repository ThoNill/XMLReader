package janus.reader.actions;

import janus.reader.exceptions.ReaderRuntimeException;

import java.lang.reflect.Method;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Action that sets a attribute
 * 
 * @author javaman
 *
 */
//@FunctionalInterface
public class SetWithAdapter extends Setter {
    static Logger LOG = LoggerFactory.getLogger(SetWithAdapter.class);

    
    private XmlAdapter<String, ?> a;

    public SetWithAdapter(TagPath path, Method m, Value v,XmlAdapter<String, ?> a) {
        super(path,m,v);
        this.a = a;
    }
    
    @Override
    public void setValue(Object value) {
        LOG.debug("setValue 2");
        Object o = "";
        try {
            o = a.unmarshal(value.toString());
            m.invoke(v.getValue(), o);
        } catch (Exception e) {
            throw new ReaderRuntimeException(" Kann die Methode "
                    + m.getName() + "("
                    + m.getParameterTypes()[0].getTypeName()
                    + " mit dem Objecttyp " + o.getClass()
                    + " nicht auf " + v.getValue() + " anwenden",e);
        }
    }
    
    public boolean isSetableFromString() {
        return true;
    }

}
