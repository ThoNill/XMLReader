package janus.reader.core;

import janus.reader.annotations.XmlPath;
import janus.reader.annotations.XmlPaths;
import janus.reader.helper.ClassHelper;
import janus.reader.nls.Messages;
import janus.reader.path.XmlElementPath;
import janus.reader.util.Assert;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Processor for the XmlPath annotations
 * 
 * @author Thaoms Nill
 *
 */
public class AnnotationProcessor {

    /**
     * Constructor
     */
    public AnnotationProcessor() {
        super();
    }

    /**
     * Process a array of classes
     * 
     * @param container
     * @param clazzes
     */
    public void processClasses(ValuesAndAttributesContainer container,
            Class<?>... clazzes) {
        Assert.notNull(container, "The Container should not be null");
        Assert.noNullElements(clazzes,
                "The classes in the array should not be null");

        for (Class<?> clazz : clazzes) {
            processClass(container, clazz);
        }
    }

    /**
     * Process a single class
     * 
     * @param container
     * @param clazzes
     */
    public void processClass(ValuesAndAttributesContainer container, Class<?> clazz) {
        Assert.notNull(container, "The container should not be null");
        Assert.notNull(clazz, "The class should not be null");
        
        checkClass(clazz);
        processAllXmlPathAnnotations(container, clazz);
    }

    protected void processAllXmlPathAnnotations(
            ValuesAndAttributesContainer container, Class<?> clazz) {
        for (XmlPath cPath : clazz.getAnnotationsByType(XmlPath.class)) {
            container.addValue(new XmlElementPath(cPath.path()), clazz);
            processNonStaticMethods(container, clazz, cPath);
        }
        processStaticMethods(container, clazz);
    }

    private void processStaticMethods(ValuesAndAttributesContainer container,
            Class<?> clazz) {
        for (Method m : clazz.getMethods()) {
            processOneStaticMethod(container, clazz, m);
        }
    }

    private void processOneStaticMethod(ValuesAndAttributesContainer container,
            Class<?> clazz, Method method) {
        if (Modifier.isStatic(method.getModifiers())
                && (method.isAnnotationPresent(XmlPath.class) || method
                        .isAnnotationPresent(XmlPaths.class))) {
            for (XmlPath mPath : method.getAnnotationsByType(XmlPath.class)) {
                checkStaticMethod(clazz, method);
                container.addValue(new XmlElementPath(mPath.path()), clazz,
                        method.getName());
                processNonStaticMethods(container, clazz, mPath);
            }
        }
    }

    private void checkStaticMethod(Class<?> clazz, Method method) {
        if (method.getParameterCount() != 0) {
            Messages.throwIllegalArgumentException(
                    "AnnotationProcessor.NO_PARAMETER", method.getDeclaringClass().getName(), method.getName()); //$NON-NLS-1$
        }
        if (!ClassHelper
                .isThisClassOrASuperClass(method.getReturnType(), clazz)) {
            Messages.throwIllegalArgumentException(
                    "AnnotationProcessor.WRONG_RETURN_TYPE", method.getDeclaringClass().getName(), method.getName(), clazz.getCanonicalName()); //$NON-NLS-1$

        }

    }

    private void processNonStaticMethods(ValuesAndAttributesContainer container,
            Class<?> clazz, XmlPath cPath) {
        for (Method m : clazz.getMethods()) {
            processOneNonStaticMethod(container, cPath, m);
        }
    }

    private void processOneNonStaticMethod(ValuesAndAttributesContainer container,
            XmlPath cPath, Method method) {
        if (!Modifier.isStatic(method.getModifiers())
                && (method.isAnnotationPresent(XmlPath.class) || method
                        .isAnnotationPresent(XmlPaths.class))) {
            checkMethod(method);
            processAllXmlPathAnnotations(container, cPath, method);
        }
    }

    protected void processAllXmlPathAnnotations(
            ValuesAndAttributesContainer container, XmlPath cPath, Method method) {
        for (XmlPath mPath : method.getAnnotationsByType(XmlPath.class)) {
            String path = mPath.path();
            String methodName = method.getName().substring(3);
            if (path.charAt(0) == '/') {
                container.addAttribute(new XmlElementPath(cPath.path()),
                        new XmlElementPath(path), methodName);
            } else {
                container.addRelativAttribute(new XmlElementPath(cPath.path()),
                        new XmlElementPath(path), methodName);
            }
        }
    }

    private void checkClass(Class<?> clazz) {
        
        // Wenn es statische Methoden gibt, die nnotiert sind, ist die Kalsse
        // auch erlaubt
        for (Method method : clazz.getMethods()) {
            if (Modifier.isStatic(method.getModifiers())
                    && (method.isAnnotationPresent(XmlPath.class) || method
                            .isAnnotationPresent(XmlPaths.class))) {
                return;
            }
        }

        if (!(clazz.isAnnotationPresent(XmlPath.class) || clazz
                .isAnnotationPresent(XmlPaths.class))) {
            Messages.throwIllegalArgumentException(
                    "AnnotationProcessor.WITH_ANNOTATION", clazz.getName()); //$NON-NLS-1$
        }
    }

    private void checkMethod(Method method) {
        
        if (method.getParameterCount() != 1) {
            Messages.throwIllegalArgumentException(
                    "AnnotationProcessor.WRONG_PARAMETER_COUNT", method.getDeclaringClass().getName(), method.getName()); //$NON-NLS-1$
        }
        if (!method.getName().startsWith("set")) {
            Messages.throwIllegalArgumentException(
                    "AnnotationProcessor.WRONG_FUNCTION_NAME", method.getDeclaringClass().getName(), method.getName()); //$NON-NLS-1$
        }
    }

}
