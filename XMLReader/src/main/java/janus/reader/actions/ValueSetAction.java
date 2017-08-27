package janus.reader.actions;

@FunctionalInterface
public interface ValueSetAction {
    void setValue(Object obj, String value);
}
