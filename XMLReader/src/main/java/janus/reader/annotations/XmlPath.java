package janus.reader.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * XmlPath annotation to mark the connection between xml paths an classes,
 * properties, static methods of a class
 * 
 * @author Thomas Nill
 *
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Repeatable(XmlPaths.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlPath {

    /**
     * the String representation of the path
     * 
     * @return
     */
    String path();
}
