package janus.reader.attribute;

import janus.reader.nls.Messages;
import janus.reader.path.XmlElementPath;
import janus.reader.value.Value;

import java.lang.reflect.Method;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Attribute that sets a property where the vale is a string. The string value ist
 * first translatet to a class with an adapter
 * 
 * @author javaman
 *
 */
// @FunctionalInterface
public class AttributeWithAdapter extends Attribute {
    private static final Logger log = LoggerFactory
            .getLogger(AttributeWithAdapter.class);

    private XmlAdapter<String, ?> a;

    /**
     * Constructor
     * 
     * @param path
     * @param m
     * @param v
     * @param a
     */
    public AttributeWithAdapter(XmlElementPath path, Method m, Value v,
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

}
