package janus.reader.adapters;

import java.util.HashMap;

import javax.xml.bind.annotation.adapters.XmlAdapter;

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
    
    private AdapterMap() {
    }
     

    public static void addAdapter(Class<?> targetClass,
            XmlAdapter<String, ?> adapter) {
        adapters.put(targetClass, adapter);
    }

    public static XmlAdapter<String,?> getAdapter(Class<?> targetClass) {
        return adapters.get(targetClass);
    }

    public static boolean hasAdapterForClass(Class<?> targetClass) {
        return adapters.containsKey(targetClass);
    }

    public static Object unmashal(Class<?> targetClass, String text) {
        try {
            XmlAdapter<String, ?> adapter = getAdapter(targetClass);
            return adapter.unmarshal(text);
        } catch (Exception ex) {
            throw new AdapterException(
                    "Error within Adapter or Adapter not found for " + targetClass.getName(), ex);
        }
    }

}