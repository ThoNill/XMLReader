package test;

import janus.reader.annotations.XmlPath;

@XmlPath(path = Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document)
public class TWrongAnnotated extends TSuper {
    private String name;
    
    public String getName() {
        return name;
    }

    @XmlPath(path = "name")
    public void addName(String name) {
        this.name = name;
    }

}
