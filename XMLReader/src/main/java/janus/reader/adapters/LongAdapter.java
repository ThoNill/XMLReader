package janus.reader.adapters;

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
    public Long unmarshal(String v) throws ParseException {
        return new Long(v.trim());
    }

    @Override
    public String marshal(Long v)  {
        return v.toString();
    }

}
