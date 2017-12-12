package janus.reader.actions;

import janus.reader.exceptions.ReaderRuntimeException;
import janus.reader.nls.Messages;

import java.lang.reflect.Method;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Setter that sets a property where the vale is a string. The string value ist
 * first translatet to a class with an adapter
 * 
 * @author javaman
 *
 */
// @FunctionalInterface
public class SetWithAdapter extends Setter {
    private static final Logger log = LoggerFactory
            .getLogger(SetWithAdapter.class);

    private XmlAdapter<String, ?> a;

    /**
     * Constructor
     * 
     * @param path
     * @param m
     * @param v
     * @param a
     */
    public SetWithAdapter(TagPath path, Method m, Value v,
            XmlAdapter<String, ?> a) {
        super(path, m, v);
        this.a = a;
    }

    @Override
    public void setValue(Object value) {
        log.debug("setValue 2");
        Object o = "";
        try {
            o = a.unmarshal(value.toString());
            m.invoke(v.getValue(), o);
        } catch (Exception e) {
            Messages.throwReaderRuntimeException(e, "Runtime.NOT_APPLICABLE",m.getName(),m.getParameterTypes()[0].getTypeName(),v.getValue(),o.getClass());
        }
    }

    /**
     * is this class setable from a String class
     * 
     * @return
     */
    @Override
    public boolean isSetableFromString() {
        return true;
    }

}
