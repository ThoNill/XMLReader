package janus.reader;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

public class Value implements Action {
    private Class clazz;
    private Object value;
    private CurrentObject current;

    public Value(Class clazz, CurrentObject current) {
        super();
        this.clazz = clazz;
        this.current = current;
    }

    @Override
    public void push() {
        try {
            value = clazz.newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Object getValue() {
        return value;
    }

    public Class getClazz() {
        return clazz;
    }

    @Override
    public void pop() {
        current.setCurrent(value);
        value = null;
    }

    public SetAction createSetAction(ValueSetAction setAction) {
        Value v = this;

        return new SetAction() {
            ValueSetAction s = setAction;

            @Override
            public void setValue(String value) {
                s.setValue(v.getValue(), value);
            }
        };
    }

    public SetAction createSetAction(MethodHandle handle) {
        Value v = this;

        return new SetAction() {
            MethodHandle h = handle;

            @Override
            public void setValue(String value) {
                try {
                    h.invoke(v.getValue(), value);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        };

    }

    public SetAction createSetAction(String name) {
        try {
            return createSetAction(MethodHandles.lookup().unreflect(
                    this.getClazz().getDeclaredMethod("set" + name,
                            String.class)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
