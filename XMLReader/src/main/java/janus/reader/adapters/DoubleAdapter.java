package janus.reader.adapters;

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
    public Double unmarshal(String v) throws ParseException {
        return new Double(v.trim());
    }

    @Override
    public String marshal(Double v) {
        return v.toString();
    }

}
