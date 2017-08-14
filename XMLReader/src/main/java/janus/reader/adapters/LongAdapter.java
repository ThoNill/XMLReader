package janus.reader.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LongAdapter extends XmlAdapter<String, Long> {

    @Override
    public Long unmarshal(String v) throws Exception {
        return new Long(v.trim());
    }

    @Override
    public String marshal(Long v) throws Exception {
        return v.toString();
    }

}
