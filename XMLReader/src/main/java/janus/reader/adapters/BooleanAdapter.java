package janus.reader.adapters;

import janus.reader.util.Assert;

import java.text.ParseException;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adapter for boolean values
 * 
 * @author Thomas Nill
 *
 */

public class BooleanAdapter extends XmlAdapter<String, Boolean> {

    @Override
    public Boolean unmarshal(String value) throws ParseException {
        Assert.notNull(value, "Parameter should not be null");
        return new Boolean(
                "true yes ok 1 ".indexOf(value.trim().toLowerCase()) >= 0);
    }

    @Override
    public String marshal(Boolean value) {
        Assert.notNull(value, "Parameter should not be null");
        return value.toString();
    }

}
