package janus.reader.annotations;

import janus.reader.core.ElementNameStack;
import java.lang.reflect.Method;

public class AnnotationProcessor {

    public void processClasses(ElementNameStack stack, Class<?>... clazzes) {
        for (Class<?> clazz : clazzes) {
            processClass(stack, clazz);
        }
    }

    public void processClass(ElementNameStack stack, Class<?> clazz) {
        checkClass(clazz);
        processAllXmlPathAnnotations(stack, clazz);
    }

    protected void processAllXmlPathAnnotations(ElementNameStack stack,
            Class<?> clazz) {
        for (XmlPath cPath : clazz.getAnnotationsByType(XmlPath.class)) {
            stack.addValue(cPath.path(), clazz);
            for (Method m : clazz.getMethods()) {
                processMethod(stack, cPath, m);
            }
        }
    }

    private void processMethod(ElementNameStack stack, XmlPath cPath, Method m) {
        if (m.isAnnotationPresent(XmlPath.class)) {
            checkMethod(m);
            processAllXmlPathAnnotations(stack, cPath, m);
        }
    }

    protected void processAllXmlPathAnnotations(ElementNameStack stack,
            XmlPath cPath, Method m) {
        for (XmlPath mPath : m.getAnnotationsByType(XmlPath.class)) {
            stack.addSetter(cPath.path(), mPath.path(), m.getName()
                    .substring(3));
        }
    }

    private void checkClass(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(XmlPath.class)) {
            throw new IllegalArgumentException("Die Klasse " + clazz.getName()
                    + " muss mit XmlPath annotiert sein");
        }
    }

    private void checkMethod(Method m) {
        if (m.getParameterCount() != 1) {
            throw new IllegalArgumentException(
                    "In der Klasse "
                            + m.getDeclaringClass().getName()
                            + " darf die Methode "
                            + m.getName()
                            + " nicht mit XmlPath annotiert sein, da die Parameterzahl != 1 ist ");
        }
        if (!m.getName().startsWith("set")) {
            throw new IllegalArgumentException(
                    "In der Klasse "
                            + m.getDeclaringClass().getName()
                            + " darf die Methode "
                            + m.getName()
                            + " nicht mit XmlPath annotiert sein, da der Parametername != setXXX ist ");
        }
    }
}
