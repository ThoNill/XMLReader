package janus.reader.core;

import janus.reader.adapters.AdapterMap;
import janus.reader.annotations.XmlPath;
import janus.reader.annotations.XmlPaths;
import janus.reader.attribute.Attribute;
import janus.reader.attribute.AttributeWithAdapter;
import janus.reader.attribute.AttributeWithValue;
import janus.reader.attribute.MethodAttribute;
import janus.reader.attribute.Setter;
import janus.reader.attribute.SetterAttribute;
import janus.reader.nls.Messages;
import janus.reader.path.XmlElementPath;
import janus.reader.util.Assert;
import janus.reader.value.ClassValue;
import janus.reader.value.Creator;
import janus.reader.value.CreatorValue;
import janus.reader.value.CurrentObject;
import janus.reader.value.SimpleCurrentObject;
import janus.reader.value.StackCurrentObject;
import janus.reader.value.StaticMethodValue;
import janus.reader.value.Value;
import janus.reader.value.ValueContainer;

import java.lang.reflect.Method;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * A Container of the Element Names in a XML document
 * 
 * There a creation methods for the different types of values und attributes
 * 
 * @author Thomas Nill
 *
 */

public class ValuesAndAttributesContainer extends ValuesAndAttributesBag {
    /**
     * constructor with a empty configuration
     * 
     * @param current
     */
    public ValuesAndAttributesContainer(CurrentObject current) {
        this(current, new ValueContainer());
    }

    /**
     * full configured with a NamedActionMap
     * 
     * @param current
     * @param map
     */
    public ValuesAndAttributesContainer(CurrentObject current, ValueContainer map) {
        super(current, map);
    }

    /**
     * Add the creation of a class instance to a path of XML Elements
     * 
     * @param path
     *            (path of XML Elements)
     * @param clazz
     *            (class of the generated instance)
     * @param creator
     */
    public void addValue(XmlElementPath path, Class<?> clazz, Creator creator) {
        Assert.notNull(path, THE_PATH_SHOULD_NOT_BE_NULL);
        Assert.notNull(clazz, THE_CLASS_SHOULD_NOT_BE_NULL);

        Value value = new CreatorValue(path, clazz, getCurrent(),
                createCurrentObject(path), creator);
        addValue(value);
    }

    /**
     * Add the creation of a class instance to a path of XML Elements
     * 
     * @param path
     *            (path of XML Elements)
     * @param clazz
     *            (class of the generated instance)
     */

    public void addValue(XmlElementPath path, Class<?> clazz) {
        Assert.notNull(path, THE_PATH_SHOULD_NOT_BE_NULL);
        Assert.notNull(clazz, THE_CLASS_SHOULD_NOT_BE_NULL);

        Value value = new ClassValue(path, clazz, getCurrent(),
                createCurrentObject(path));
        addValue(value);
    }

    private CurrentObject createCurrentObject(XmlElementPath name) {
        return name.isAbsolut() ? new SimpleCurrentObject()
                : new StackCurrentObject();
    }

    /**
     * Add the creation of a class instance to a path of XML Elements
     * 
     * @param path
     *            (path of XML Elements)
     * @param clazz
     *            (class of the generated instance)
     * @param methodName
     */

    public void addValue(XmlElementPath path, Class<?> clazz, String methodName) {
        Assert.notNull(path, THE_PATH_SHOULD_NOT_BE_NULL);
        Assert.notNull(clazz, THE_CLASS_SHOULD_NOT_BE_NULL);
        Assert.hasText(methodName, THE_METHOD_NAME_SHOULD_NOT_BE_EMPTY);

        Value value = new StaticMethodValue(path, clazz, getCurrent(),
                createCurrentObject(path), methodName);
        addValue(value);
    }

    /**
     * Add the setXXXX setter to a path of XML Elements
     * 
     * @param valuePath
     *            (the path of XML elements to the object instance, that will be
     *            set)
     * @param attributePath
     *            (the relative path of XML elements to a text-value, for the
     *            value attribute of the setter)
     * @param field
     *            (the name of the setter Method)
     */
    public void addRelativAttribute(XmlElementPath valuePath,
            XmlElementPath attributePath, String field) {
        Assert.notNull(valuePath, THE_PATH_SHOULD_NOT_BE_NULL);
        Assert.notNull(attributePath, THE_CLASS_SHOULD_NOT_BE_NULL);
        Assert.hasText(field, THE_FIELD_SHOULD_NOT_BE_EMPTY);

        addAttribute(valuePath, valuePath.concat(attributePath), field);
    }

    /**
     * Add the setXXXX setter to a path of XML Elements
     * 
     * @param valuePath
     *            (the path of XML elements to the object instance, that will be
     *            set)
     * @param attributePath
     *            (the absolute path of XML elements to a text-value, for the
     *            value attribute of the setter)
     * @param field
     *            (the name of the setter Method)
     */
    public void addAttribute(XmlElementPath valuePath,
            XmlElementPath attributePath, String field) {
        Assert.notNull(valuePath, THE_PATH_SHOULD_NOT_BE_NULL);
        Assert.notNull(attributePath, THE_CLASS_SHOULD_NOT_BE_NULL);
        Assert.hasText(field, THE_FIELD_SHOULD_NOT_BE_EMPTY);

        if (attributePath.startsWith(valuePath)) {
            Value value = checkArguments(valuePath, attributePath, field);
            Attribute attribute = createAttribute(value, attributePath, field);
            addAttribute(attribute);
        }
    }

    /**
     * create SetAction from a {@link Method}
     * 
     * @param value
     * @param rValuePath
     * @param handle
     * @return
     */
    public <T, V> Attribute addAttribute(Value value,
            XmlElementPath rValuePath, Setter<T, V> handle) {
        return new SetterAttribute(rValuePath, handle, value);
    }

    /**
     * create a Attribute form a method name
     * 
     * @param value
     * @param valuePath
     * @param fieldName
     * 
     * @return
     */
    public Attribute createAttribute(Value value, XmlElementPath valuePath,
            String fieldName) {
        try {
            Assert.notNull(value, THE_VALUE_SHOULD_NOT_BE_NULL);
            Assert.notNull(valuePath, THE_PATH_SHOULD_NOT_BE_NULL);
            Assert.hasText(fieldName, THE_FIELD_SHOULD_NOT_BE_EMPTY);

            String methodName = "set" + fieldName;
            Method method = searchTheMethod(value.getClazz(), methodName,
                    String.class);
            Class<?> targetClass = method.getParameterTypes()[0];
            if (targetClass.isAnnotationPresent(XmlPath.class)) {
                XmlPath xpath = targetClass.getAnnotation(XmlPath.class);
                return createSetFromValue(value, valuePath, new XmlElementPath(
                        xpath.path()), method);
            }
            if (targetClass.equals(String.class)) {
                return createAttribute(value, valuePath, method);
            } else {
                return createAttribute(value, valuePath, method,
                        AdapterMap.getAdapter(targetClass));
            }

        } catch (Exception e) {
            Messages.throwReaderRuntimeException(e, "Runtime.NOT_METHOD", value
                    .getClazz().getName(), fieldName);
            return null;
        }
    }

    /**
     * create SetAction from a {@link Method}
     * 
     * @param handle
     * @return
     */

    private Attribute createAttribute(Value value, XmlElementPath rValuePath,
            Method handle) {
        return new MethodAttribute(rValuePath, handle, value);
    }

    private Attribute createSetFromValue(Value value,
            XmlElementPath setterPath, XmlElementPath valuePath, Method handle) {
        log.debug(" createSetFromValuAction Attribute {} From {} ", setterPath,
                valuePath);
        AttributeWithValue attributeWithValue = new AttributeWithValue(
                setterPath, valuePath, getValueMap(), getAttributeMap());
        addAttributeWithValue(attributeWithValue);
        return new MethodAttribute(setterPath, handle, value);
    }

    /**
     * create SetAction from a {@link Method}
     * 
     * @param handle
     * @param adapter
     * @return
     */
    // NOSONAR because this method is USED
    @SuppressWarnings("squid:UnusedPrivateMethod")
    private Attribute createAttribute(Value value, XmlElementPath rValuePath,
            Method handle, XmlAdapter<String, ?> adapter) {
        Attribute delegate = new MethodAttribute(rValuePath, handle, value);
        return new AttributeWithAdapter(delegate, adapter);
    }

    /**
     * search a method
     * 
     * 
     * @param clazz
     * @param name
     * @param targetClass
     * @return
     * @throws Exception
     */
    // NOSONAR because this method is USED
    @SuppressWarnings("squid:UnusedPrivateMethod")
    private Method searchTheMethod(Class<?> clazz, String name,
            Class<?> targetClass) {
        Method bestMethod = null;
        Method usableMethod = null;
        for (Method method : clazz.getMethods()) {
            if (name.equals(method.getName())
                    && method.getParameterCount() == 1) {
                Class<?> parameterType = method.getParameterTypes()[0];
                if (parameterType == targetClass) {
                    bestMethod = method;
                }
                if (AdapterMap.hasAdapterForClass(parameterType)) {
                    usableMethod = method;
                }
                if (parameterType.isAnnotationPresent(XmlPath.class)
                        || parameterType.isAnnotationPresent(XmlPaths.class)) {
                    usableMethod = method;
                }
            }
        }
        if (bestMethod != null) {
            return bestMethod;
        }
        return usableMethod;
    }

}
