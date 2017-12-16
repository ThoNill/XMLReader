package janus.reader.adapters;

import janus.reader.util.Assert;

import java.text.ParseException;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adapter for long Values
 * 
 * @author Thomas Nill
 *
 */
public class LongAdapter extends XmlAdapter<String, Long> {

    @Override
    public Long unmarshal(String value) throws ParseException {
        Assert.notNull(value, "Parameter should not be null");
        return new Long(value.trim());
    }

    @Override
    public String marshal(Long value) {
        Assert.notNull(value, "Parameter should not be null");
        return value.toString();
    }

}
