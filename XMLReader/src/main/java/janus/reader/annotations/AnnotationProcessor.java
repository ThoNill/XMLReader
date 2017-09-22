package janus.reader.annotations;

import janus.reader.actions.ElementNameStack;
import janus.reader.helper.ClassHelper;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

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
            processNonStaticMethods(stack, clazz, cPath);
        }
        processStaticMethods(stack, clazz);
    }

    private void processStaticMethods(ElementNameStack stack, Class<?> clazz) {
        for (Method m : clazz.getMethods()) {
            processOneStaticMethod(stack, clazz, m);
        }
    }

    private void processOneStaticMethod(ElementNameStack stack, Class<?> clazz,
            Method m) {
        if (Modifier.isStatic(m.getModifiers()) && (m.isAnnotationPresent(XmlPath.class) || m.isAnnotationPresent(XmlPaths.class))) {
            for (XmlPath mPath : m.getAnnotationsByType(XmlPath.class)) {
               checkStaticMethod(clazz,m); 
               stack.addValue(mPath.path(), clazz,m.getName());
               processNonStaticMethods(stack, clazz, mPath);
            }
        }
    }

    private void checkStaticMethod(Class<?> clazz,Method m) {
        if (m.getParameterCount() != 0) {
            throw new IllegalArgumentException(
                    "In der Klasse "
                            + m.getDeclaringClass().getName()
                            + " darf die Methode "
                            + m.getName()
                            + " keine Paramter haben ");
        }
        if (!ClassHelper.isThisClassOrASuperClass(m.getReturnType(), clazz)) {
            throw new IllegalArgumentException(
                    "In der Klasse "
                            + m.getDeclaringClass().getName()
                            + " muss die Methode "
                            + m.getName()
                            + " ein Object dieser Klasse " + clazz.getCanonicalName() + " zurueckgeben");
        }
       
        
    }

    private void processNonStaticMethods(ElementNameStack stack,
            Class<?> clazz, XmlPath cPath) {
        for (Method m : clazz.getMethods()) {
            processOneNonStaticMethod(stack, cPath, m);
        }
    }

    private void processOneNonStaticMethod(ElementNameStack stack, XmlPath cPath, Method m) {
        if (!Modifier.isStatic(m.getModifiers()) && (m.isAnnotationPresent(XmlPath.class) || m.isAnnotationPresent(XmlPaths.class))) {
            checkMethod(m);
            processAllXmlPathAnnotations(stack, cPath, m);
        }
    }

    protected void processAllXmlPathAnnotations(ElementNameStack stack,
            XmlPath cPath, Method m) {
        for (XmlPath mPath : m.getAnnotationsByType(XmlPath.class)) {
            String path = mPath.path();
            String methodName = m.getName().substring(3);
            if(path.charAt(0) == '/') {
               stack.addSetter(cPath.path(), path,methodName);
            } else {
                stack.addRelativSetter(cPath.path(), path,methodName);
            }
        }
    }

    private void checkClass(Class<?> clazz) {
        // Wenn es statische Methoden gibt, die nnotiert sind, ist die Kalsse auch erlaubt
        for (Method m : clazz.getMethods()) {
            if(Modifier.isStatic(m.getModifiers()) && (m.isAnnotationPresent(XmlPath.class) || m.isAnnotationPresent(XmlPaths.class))) {
                return;
            }
        }
        
        if (!(clazz.isAnnotationPresent(XmlPath.class) || clazz.isAnnotationPresent(XmlPaths.class))) {
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
