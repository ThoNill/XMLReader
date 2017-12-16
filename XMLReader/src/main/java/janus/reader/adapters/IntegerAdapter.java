package janus.reader.adapters;

import janus.reader.util.Assert;

import java.text.ParseException;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adapter for integer Values
 * 
 * @author Thomas Nill
 *
 */

public class IntegerAdapter extends XmlAdapter<String, Integer> {

    @Override
    public Integer unmarshal(String value) throws ParseException {
        Assert.notNull(value, "Parameter should not be null");
        return new Integer(value.trim());
    }

    @Override
    public String marshal(Integer value) {
        Assert.notNull(value, "Parameter should not be null");
        return value.toString();
    }

}
