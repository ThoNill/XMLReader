package janus.reader.value;

import janus.reader.attribute.Attribute;
import janus.reader.helper.ClassHelper;
import janus.reader.nls.Messages;
import janus.reader.path.XmlElementPath;
import janus.reader.util.Assert;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A value that creates its object with a static method of the class
 * 
 * there ar different methods to create {@link Attribute}
 * 
 * @author Thomas Nill
 *
 */
public class StaticMethodValue extends Value {
    private static final Logger log = LoggerFactory.getLogger(StaticMethodValue.class);

    private Method staticCreationMethod;

    /**
     * Constructor
     * 
     * @param path
     * @param clazz
     * @param current
     * @param staticMethodName
     * @param objectOfClass
     */
    public StaticMethodValue(XmlElementPath path, Class<?> clazz, CurrentObject current,
            CurrentObject objectOfClass,String staticMethodName) {
        super(path, clazz, current,objectOfClass);
        Assert.hasText(staticMethodName, "The methodname should not be empty");

        
        
        log.debug("Create Value for path {} ", path);
        if (staticMethodName != null) {
            try {
                staticCreationMethod = clazz.getMethod(staticMethodName);
            } catch (NoSuchMethodException | SecurityException e) {
                log.debug("Error {}",e);
                Messages.throwIllegalArgumentException("Value.METHOD_DOESNOT_EXIST",staticMethodName);
            }
            if (!ClassHelper.isThisClassOrASuperClass(
                    staticCreationMethod.getReturnType(), clazz)) {
                Messages.throwIllegalArgumentException("Value.METHOD_WRONG_TYPE",staticMethodName,clazz.getCanonicalName());
            }
            if (!Modifier.isStatic(staticCreationMethod.getModifiers())) {
                Messages.throwIllegalArgumentException("Value.METHOD_NOT_STATIC",staticMethodName);
            }
            if (staticCreationMethod.getParameterCount() > 0) {
                Messages.throwIllegalArgumentException("Value.METHOD_HAS_PARAMETER",staticMethodName);
            }
        }
    }


    @Override
    public Object createNewObject() throws Exception{
        return staticCreationMethod.invoke(null);
    }

}
