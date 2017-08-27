package janus.reader.actions;

public class CurrentObject {
    private Object current;

    public Object next() {
        Object o = current;
        current = null;
        return o;
    }

    public void setCurrent(Object current) {
        this.current = current;
    }

    public boolean hasObject() {
        return current != null;
    }

}
