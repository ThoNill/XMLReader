package janus.reader.adapters;

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
    public Float unmarshal(String v) throws ParseException {
        return new Float(v.trim());
    }

    @Override
    public String marshal(Float v)  {
        return v.toString();
    }

}
