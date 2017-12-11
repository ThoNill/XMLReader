package janus.reader.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Array of XmlPath annotations
 * 
 * @author Thomas Nill
 *
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface XmlPaths {
    /**
     * the array entries
     * 
     * @return
     */
    XmlPath[] value();
}
