package janus.reader.adapters;

import janus.reader.util.Assert;

import java.text.ParseException;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adapter for float values
 * 
 * @author Thomas Nill
 *
 */
public class FloatAdapter extends XmlAdapter<String, Float> {

    @Override
    public Float unmarshal(String value) throws ParseException {
        Assert.notNull(value, "Parameter should not be null");
        return new Float(value.trim());
    }

    @Override
    public String marshal(Float value) {
        Assert.notNull(value, "Parameter should not be null");
        return value.toString();
    }

}
