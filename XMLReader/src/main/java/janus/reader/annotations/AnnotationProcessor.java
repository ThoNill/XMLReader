package janus.reader.annotations;

import janus.reader.actions.ElementNameStack;
import janus.reader.actions.TagPath;
import janus.reader.helper.ClassHelper;
import janus.reader.nls.Messages;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;

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
     * @param stack
     * @param clazzes
     */
    public void processClasses(ElementNameStack stack, Class<?>... clazzes) {
        for (Class<?> clazz : clazzes) {
            processClass(stack, clazz);
        }
    }

    /**
     * Process a single class
     * 
     * @param stack
     * @param clazzes
     */
    public void processClass(ElementNameStack stack, Class<?> clazz) {
        checkClass(clazz);
        processAllXmlPathAnnotations(stack, clazz);
    }

    protected void processAllXmlPathAnnotations(ElementNameStack stack,
            Class<?> clazz) {
        for (XmlPath cPath : clazz.getAnnotationsByType(XmlPath.class)) {
            stack.addValue(new TagPath(cPath.path()), clazz);
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
        if (Modifier.isStatic(m.getModifiers())
                && (m.isAnnotationPresent(XmlPath.class) || m
                        .isAnnotationPresent(XmlPaths.class))) {
            for (XmlPath mPath : m.getAnnotationsByType(XmlPath.class)) {
                checkStaticMethod(clazz, m);
                stack.addValue(new TagPath(mPath.path()), clazz, m.getName());
                processNonStaticMethods(stack, clazz, mPath);
            }
        }
    }

    private void checkStaticMethod(Class<?> clazz, Method m) {
        if (m.getParameterCount() != 0) {
            Messages.throwIllegalArgumentException("AnnotationProcessor.NO_PARAMETER",m.getDeclaringClass().getName(),m.getName()); //$NON-NLS-1$
        }
        if (!ClassHelper.isThisClassOrASuperClass(m.getReturnType(), clazz)) {
            Messages.throwIllegalArgumentException("AnnotationProcessor.WRONG_RETURN_TYPE",m.getDeclaringClass().getName(),m.getName(),clazz.getCanonicalName()); //$NON-NLS-1$
            
        }

    }

    private void processNonStaticMethods(ElementNameStack stack,
            Class<?> clazz, XmlPath cPath) {
        for (Method m : clazz.getMethods()) {
            processOneNonStaticMethod(stack, cPath, m);
        }
    }

    private void processOneNonStaticMethod(ElementNameStack stack,
            XmlPath cPath, Method m) {
        if (!Modifier.isStatic(m.getModifiers())
                && (m.isAnnotationPresent(XmlPath.class) || m
                        .isAnnotationPresent(XmlPaths.class))) {
            checkMethod(m);
            processAllXmlPathAnnotations(stack, cPath, m);
        }
    }

    protected void processAllXmlPathAnnotations(ElementNameStack stack,
            XmlPath cPath, Method m) {
        for (XmlPath mPath : m.getAnnotationsByType(XmlPath.class)) {
            String path = mPath.path();
            String methodName = m.getName().substring(3);
            if (path.charAt(0) == '/') {
                stack.addSetter(new TagPath(cPath.path()), new TagPath(path),
                        methodName);
            } else {
                stack.addRelativSetter(new TagPath(cPath.path()), new TagPath(
                        path), methodName);
            }
        }
    }

    private void checkClass(Class<?> clazz) {
        // Wenn es statische Methoden gibt, die nnotiert sind, ist die Kalsse
        // auch erlaubt
        for (Method m : clazz.getMethods()) {
            if (Modifier.isStatic(m.getModifiers())
                    && (m.isAnnotationPresent(XmlPath.class) || m
                            .isAnnotationPresent(XmlPaths.class))) {
                return;
            }
        }

        if (!(clazz.isAnnotationPresent(XmlPath.class) || clazz
                .isAnnotationPresent(XmlPaths.class))) {
            Messages.throwIllegalArgumentException("AnnotationProcessor.WITH_ANNOTATION",clazz.getName()); //$NON-NLS-1$
        }
    }

    private void checkMethod(Method m) {
        if (m.getParameterCount() != 1) {
            Messages.throwIllegalArgumentException("AnnotationProcessor.WRONG_PARAMETER_COUNT",m.getDeclaringClass().getName(),m.getName()); //$NON-NLS-1$
        }
        if (!m.getName().startsWith("set")) { 
            Messages.throwIllegalArgumentException("AnnotationProcessor.WRONG_FUNCTION_NAME",m.getDeclaringClass().getName(),m.getName()); //$NON-NLS-1$
        }
    }
    
   
}
