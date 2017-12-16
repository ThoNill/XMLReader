package janus.reader.attribute;

import janus.reader.nls.Messages;
import janus.reader.path.XmlElementPath;
import janus.reader.util.Assert;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Attribute that sets adapter property where the vale is adapter string. The string value ist
 * first translatet to adapter class with an adapter
 * 
 * @author javaman
 *
 */
// @FunctionalInterface
public class AttributeWithAdapter implements Attribute {
    private static final Logger log = LoggerFactory
            .getLogger(AttributeWithAdapter.class);

    private XmlAdapter<String, ?> adapter;
    private Attribute delegate=null;

    /**
     * Constructor
     * 
     * @param delegate
     * @param adapter
     */
    public AttributeWithAdapter(Attribute delegate,
            XmlAdapter<String, ?> adapter) {
        super();
        Assert.notNull(delegate, "Attribute should not be null");
        Assert.notNull(adapter, "Adapter should not be null");
        this.delegate = delegate;
        this.adapter = adapter;
    }

    @Override
    public void setValue(Object objValue) {
        log.debug("setValue {}",delegate);
        Object o = "";
        try {
            o = adapter.unmarshal(objValue.toString());
            delegate.setValue(o);
        } catch (Exception e) {
            Messages.throwReaderRuntimeException(e, "Runtime.NOT_APPLICABLE","method","type","value",o.getClass());
        }
    }

    @Override
    public XmlElementPath getPath() {
        return delegate.getPath();
    }

    @Override
    public boolean isSetableFromString() {
        return true;
    }

}
