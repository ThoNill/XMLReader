package adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class DoubleAdapter extends XmlAdapter<String,Double> {
 
    @Override
    public Double unmarshal(String v) throws Exception {
        return new Double(v.trim());
    }

    @Override
    public String marshal(Double v) throws Exception {
        return v.toString();
    }

}
