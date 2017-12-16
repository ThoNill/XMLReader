package janus.reader.test.entities;

import janus.reader.annotations.XmlPath;

@XmlPath(path = "xsi:child")
public class ChildWithPrefix {

    private String name;

    public String getName() {
        return name;
    }

    @XmlPath(path = "name")
    public void setName(String name) {
        this.name = name;
    }

}
