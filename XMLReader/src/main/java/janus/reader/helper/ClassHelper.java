package janus.reader.helper;

public class ClassHelper {

    /**
     * check that the method return object of type c
     * 
     */
    public static boolean isThisClassOrASuperClass(Class<?> clazz,Class<?> c) {
        if (clazz.equals(Void.class)) {
            return false;
        }
        
        if (clazz.equals(c)) {
            return true;
        }
        Class<?> superClass = clazz.getSuperclass();
        if (superClass.equals(Object.class)) {
            return false;
        }
        return isThisClassOrASuperClass(superClass, c);
    }

}
