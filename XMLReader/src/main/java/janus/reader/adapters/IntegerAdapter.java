package janus.reader.adapters;

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
    public Integer unmarshal(String v) throws ParseException {
        return new Integer(v.trim());
    }

    @Override
    public String marshal(Integer v)  {
        return v.toString();
    }

}
