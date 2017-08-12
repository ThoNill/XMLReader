package janus.reader;

import java.util.HashMap;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import adapters.BooleanAdapter;
import adapters.DoubleAdapter;
import adapters.FloatAdapter;
import adapters.IntegerAdapter;
import adapters.LongAdapter;

public class AdapterMap {
    private static HashMap<Class<?>, XmlAdapter<String, ?>> adapters = null;

    static {
        adapters = new HashMap<>();
        addAdapter(Integer.class, new IntegerAdapter());
        addAdapter(int.class, new IntegerAdapter());
        addAdapter(Long.class, new LongAdapter());
        addAdapter(long.class, new LongAdapter());
        addAdapter(Double.class, new DoubleAdapter());
        addAdapter(double.class, new DoubleAdapter());
        addAdapter(Float.class, new FloatAdapter());
        addAdapter(float.class, new FloatAdapter());
        addAdapter(Boolean.class, new BooleanAdapter());
        addAdapter(boolean.class, new BooleanAdapter());
        
    }

    public static void addAdapter(Class<?> targetClass,
            XmlAdapter<String, ?> adapter) {
        adapters.put(targetClass, adapter);
    }

    public static XmlAdapter<String, ?> getAdapter(Class<?> targetClass) {
        XmlAdapter<String, ?> adapter = adapters.get(targetClass);
        return adapter;
    }

    public static boolean hasAdapterForClass(Class<?> targetClass) {
        return adapters.containsKey(targetClass);
    }

    public static Object unmashal(Class<?> targetClass, String text)
            throws Exception {
        XmlAdapter<String, ?> adapter = getAdapter(targetClass);
        return adapter.unmarshal(text);
    }

}
