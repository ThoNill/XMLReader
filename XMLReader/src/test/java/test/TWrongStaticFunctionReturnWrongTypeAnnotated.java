package test;

import janus.reader.annotations.XmlPath;

@XmlPath(path = Const.Ntry_Ntfctn_BkToCstmrDbtCdtNtfctn_Document)
public class TWrongStaticFunctionReturnWrongTypeAnnotated extends TSuper {
   
    public TWrongStaticFunctionReturnWrongTypeAnnotated() {
        super();
    }

    @XmlPath(path="name")
    public static Double createObject() {
        return new Double(2.0);
    }

}
