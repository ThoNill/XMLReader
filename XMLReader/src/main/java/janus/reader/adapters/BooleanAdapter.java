package janus.reader.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class BooleanAdapter extends XmlAdapter<String,Boolean> {
 
    @Override
    public Boolean unmarshal(String v) throws Exception {
        return new Boolean(" true yes ok 1 ".indexOf(v.trim().toLowerCase())>0);
    }

    @Override
    public String marshal(Boolean v) throws Exception {
        return v.toString();
    }

}
