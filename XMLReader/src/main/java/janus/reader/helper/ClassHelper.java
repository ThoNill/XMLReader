package janus.reader.helper;

import janus.reader.util.Assert;


/**
 * Helper Method for subclassing
 * 
 * @author Thomas Nill
 *
 */
public class ClassHelper {

    private ClassHelper() {
        super();
    }

    /**
     * check that the method return object of type c
     * 
     * @param clazz
     * @param c
     * @return
     */
    public static boolean isThisClassOrASuperClass(Class<?> clazz, Class<?> c) {
        Assert.notNull(clazz, "The class should not be null");
        Assert.notNull(c, "The class should not be null");

        if (clazz.equals(Void.class)) {
            return false;
        }
        if (clazz.equals(c)) {
            return true;
        }
        Class<?> superClass = clazz.getSuperclass();
        if (superClass == null) {
            return false;
        }
        return isThisClassOrASuperClass(superClass, c);
    }

}
