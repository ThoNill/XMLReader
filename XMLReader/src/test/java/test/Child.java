package test;

import janus.reader.annotations.XmlPath;

@XmlPath(path="child")
public class Child {

    private String name;

    public String getName() {
        return name;
    }

    @XmlPath(path="name")
    public void setName(String name) {
        this.name = name;
    }
    
}
