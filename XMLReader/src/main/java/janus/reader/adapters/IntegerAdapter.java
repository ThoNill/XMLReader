package janus.reader.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class IntegerAdapter extends XmlAdapter<String,Integer> {
 
    @Override
    public Integer unmarshal(String v) throws Exception {
        return new Integer(v.trim());
    }

    @Override
    public String marshal(Integer v) throws Exception {
        return v.toString();
    }

}
