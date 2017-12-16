package janus.reader.attribute;

import janus.reader.nls.Messages;
import janus.reader.path.XmlElementPath;
import janus.reader.path.XmlElementPathEntry;
import janus.reader.util.Assert;
import janus.reader.value.Value;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Attribute sets a property of a class to a value
 * 
 * @author Thomas Nill
 *
 */
public class MethodAttribute extends XmlElementPathEntry implements Attribute {
    private static final Logger log = LoggerFactory
            .getLogger(MethodAttribute.class);

    protected Method method;
    protected Value value;

    /**
     * Constructor
     * 
     * @param path
     * @param method
     * @param value
     */
    public MethodAttribute(XmlElementPath path, Method method, Value value) {
        super(path);
        Assert.notNull(method, "Method should not be null");
        Assert.notNull(value, "Value should not be null");
        this.method = method;
        this.value = value;
    }

    @Override
    public void setValue(Object objValue) {
        try {
            log.debug("setMethod {} of Value {} to {}", method,
                    value.getPath(), objValue);
            method.invoke(value.getValue(), objValue);
        } catch (Exception e) {
            if (value.getValue() == null) {
                Messages.throwReaderRuntimeException(e,
                        "Runtime.NOT_APPLICABLE", method.getName(),
                        method.getParameterTypes()[0].getTypeName(), "null",
                        "unknown");
            } else {
                Messages.throwReaderRuntimeException(e,
                        "Runtime.NOT_APPLICABLE", method.getName(),
                        method.getParameterTypes()[0].getTypeName(),
                        value.getValue(), value.getValue().getClass().getName());
            }
        }
    }

    /**
     * settable from a String
     * 
     * @return
     */
    @Override
    public boolean isSetableFromString() {
        return method.getParameterTypes()[0].equals(String.class);
    }

}
