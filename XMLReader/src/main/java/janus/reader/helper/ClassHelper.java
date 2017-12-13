package janus.reader.helper;

import org.omg.CORBA.Object;

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
