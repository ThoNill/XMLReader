package janus.reader.adapters;

import janus.reader.nls.Messages;

import java.text.MessageFormat;
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

    /**
     * get the adapter for a specific class targetClass und unmashal the string
     * value
     * 
     * @param targetClass
     * @param text
     * @return
     */
    public static Object unmashal(Class<?> targetClass, String text) {
        try {
            XmlAdapter<String, ?> adapter = getAdapter(targetClass);
            return adapter.unmarshal(text);
        } catch (Exception ex) {
            String pattern = Messages.getString("Adapter.NOT_FOUND");
            throw new AdapterException(MessageFormat.format(pattern, targetClass.getName()),ex);
        }
    }

}
