package janus.reader.test.entities;

import janus.reader.annotations.XmlPath;
import janus.reader.test.Const;

@XmlPath(path = Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document)
public class TWrongFunctionParameterCountAnnotated extends TSuper {
    private String name;
 
    public String getName() {
        return name;
    }

    @XmlPath(path="name")
    public void setName(String name,String toMatch) {
        this.name = name;
    }

}
