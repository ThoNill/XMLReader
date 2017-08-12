package janus.reader;

import java.lang.reflect.Method;

import javax.xml.bind.annotation.adapters.XmlAdapter;

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

    public SetAction createSetAction(Method handle) {
        Value v = this;

        return new SetAction() {
            Method m = handle;

            @Override
            public void setValue(String value) {
                try {
                    m.invoke(v.getValue(), value);
                } catch (Throwable e) {
                    throw new RuntimeException(" Kann die Methode "
                            + m.getName() + "("  + m.getParameterTypes()[0].getTypeName()+") nicht auf " + v.getValue()
                            + " anwenden");
                }
            }
        };

    }

    public SetAction createSetAction(Method handle,
            XmlAdapter<String, ?> adapter) {
        Value v = this;

        return new SetAction() {
            Method m = handle;
            XmlAdapter<String, ?> a = adapter;

            @Override
            public void setValue(String value) {
                Object o = "";
                try {
                    o = a.unmarshal(value);
                    m.invoke(v.getValue(), o);
                } catch (Throwable e) {
                    throw new RuntimeException(" Kann die Methode "
                            + m.getName()+ "("  + m.getParameterTypes()[0].getTypeName() + " mit dem Objecttyp " + o.getClass() + " nicht auf " + v.getValue()
                            + " anwenden");
                }
            }
        };

    }

    public SetAction createSetAction(String name) {
        try {
            if (name == null) {
                throw new IllegalArgumentException(
                        "seter Funktionsname muss != null sein");
            }
            String methodName = "set" + name;
            Method method = sucheDieMethode(this.getClazz(), methodName,
                    String.class);
            Class<?> targetClass = method.getParameterTypes()[0];
            if (targetClass != String.class) {
                return createSetAction(method, AdapterMap.getAdapter(targetClass));
            }
            return createSetAction(method);
        } catch (Exception e) {
            throw new RuntimeException("Die Klasse " + getClazz().getName()
                    + " hat keine Methode set" + name + " oder sie ist privat",
                    e);
        }
    }

    public SetAction createSetAction(String name,
            XmlAdapter<String, ?> adapter, Class targetClass) {
        try {
            if (name == null) {
                throw new IllegalArgumentException(
                        "seter Funktionsname muss != null sein");
            }
            String methodName = "set" + name;
            return createSetAction(
                    sucheDieMethode(this.getClazz(), methodName, targetClass),
                    adapter);
        } catch (Exception e) {
            throw new RuntimeException("Die Klasse " + getClazz().getName()
                    + " hat keine Methode set" + name + " oder sie ist privat",
                    e);
        }
    }

    public Method sucheDieMethode(Class<?> clazz, String name,
            Class<?> targetClass) throws Exception {
        Method bestMethod = null;
        Method usableMethod = null;
        for (Method method : clazz.getMethods()) {
            if (name.equals(method.getName())
                    && method.getParameterCount() == 1) {
                if (method.getParameterTypes()[0] == targetClass) {
                    bestMethod = method;
                }
                if (AdapterMap
                        .hasAdapterForClass(method.getParameterTypes()[0])) {
                    usableMethod = method;
                }
            }
        }
        if (bestMethod != null) {
            return bestMethod;
        }
        return usableMethod;
    }

}
