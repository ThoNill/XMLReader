package janus.reader;

public class CurrentObject {
    private Object current;

    public Object getCurrent() {
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
