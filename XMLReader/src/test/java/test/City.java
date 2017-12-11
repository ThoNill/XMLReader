package test;

import janus.reader.annotations.XmlPath;

@XmlPath(path = "city")
public class City {

    private String name;

    public String getName() {
        return name;
    }

    @XmlPath(path = "name")
    public void setName(String name) {
        this.name = name;

    }

    @Override
    public String toString() {
        return "City [name=" + name + "]";
    }

}
