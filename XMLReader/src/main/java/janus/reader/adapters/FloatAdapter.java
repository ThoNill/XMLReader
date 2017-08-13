package janus.reader.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class FloatAdapter extends XmlAdapter<String,Float> {
 
    @Override
    public Float unmarshal(String v) throws Exception {
        return new Float(v.trim());
    }

    @Override
    public String marshal(Float v) throws Exception {
        return v.toString();
    }

}
