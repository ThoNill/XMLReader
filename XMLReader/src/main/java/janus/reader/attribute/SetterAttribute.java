package janus.reader.attribute;

import janus.reader.nls.Messages;
import janus.reader.path.XmlElementPath;
import janus.reader.path.XmlElementPathEntry;
import janus.reader.util.Assert;
import janus.reader.value.Value;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Set an attribut with a {@link Setter}
 * 
 * @author Thomas Nill
 *
 */
public class SetterAttribute  extends XmlElementPathEntry implements Attribute {
    private static final Logger log = LoggerFactory
            .getLogger(SetterAttribute.class);

    protected Setter  setter;
    protected Value value;

    /**
     * Constructor
     * 
     * @param path
     * @param setter
     * @param value
     */
    public SetterAttribute(XmlElementPath path, Setter   setter, Value value) {
        super(path);
        Assert.notNull(setter, "Creator should not be null");
        Assert.notNull(value, "Value should not be null");
        this.setter = setter;
        this.value = value;
    }

    @Override
    public void setValue(Object objValue) {
        try {
            log.debug("setMethod {} of Value {} to {}", setter,
                    value.getPath(), objValue);
            setter.setValue(value.getValue(), objValue);
         } catch (Exception e) {
            if (value.getValue() == null) {
                Messages.throwReaderRuntimeException(e,
                        "Runtime.SET_ERROR", setter.toString(),
                         "null",
                        "unknown");
            } else {
                Messages.throwReaderRuntimeException(e,
                        "Runtime.SET_ERROR", setter.toString(),
                         value.getValue().getClass().getName());
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
      return true;
    }

}
