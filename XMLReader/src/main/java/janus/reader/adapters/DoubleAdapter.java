package janus.reader.adapters;

import janus.reader.util.Assert;

import java.text.ParseException;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adapter for double values
 * 
 * @author Thomas Nill
 *
 */
public class DoubleAdapter extends XmlAdapter<String, Double> {

    @Override
    public Double unmarshal(String value) throws ParseException {
        Assert.notNull(value, "Parameter should not be null");
        return new Double(value.trim());
    }

    @Override
    public String marshal(Double value) {
        Assert.notNull(value, "Parameter should not be null");
        return value.toString();
    }

}
