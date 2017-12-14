package janus.reader.adapters;

import java.util.HashMap;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * a bundle of different adapters, you can add other adapters to this bundle
 * 
 * @author Thomas Nill
 *
 */
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
        super();
    }

    /**
     * add an adapter for a class targetClass
     * 
     * @param targetClass
     * @param adapter
     */
    public static void addAdapter(Class<?> targetClass,
            XmlAdapter<String, ?> adapter) {
        adapters.put(targetClass, adapter);
    }

    /**
     * get the adapter for the targetClass
     * 
     * @param targetClass
     * 
     * @return
     */
    public static XmlAdapter<String, ?> getAdapter(Class<?> targetClass) {
        return adapters.get(targetClass);
    }

    /**
     * chak if an adapter for a class targetClass exists
     * 
     * @param targetClass
     * @return
     */
    public static boolean hasAdapterForClass(Class<?> targetClass) {
        return adapters.containsKey(targetClass);
    }


}
