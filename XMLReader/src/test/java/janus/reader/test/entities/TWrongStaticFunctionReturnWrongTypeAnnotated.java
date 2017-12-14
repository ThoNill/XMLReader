package janus.reader.test.entities;

import janus.reader.annotations.XmlPath;
import janus.reader.test.Const;

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
