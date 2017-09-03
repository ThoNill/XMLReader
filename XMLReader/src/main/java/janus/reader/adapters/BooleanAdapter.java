package janus.reader.adapters;

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
    public Boolean unmarshal(String v) throws ParseException {
        return new Boolean(
                "true yes ok 1 ".indexOf(v.trim().toLowerCase()) >= 0);
    }

    @Override
    public String marshal(Boolean v) {
        return v.toString();
    }

}
