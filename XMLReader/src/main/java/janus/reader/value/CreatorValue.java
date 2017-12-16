package janus.reader.value;

import janus.reader.path.XmlElementPath;
import janus.reader.util.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A value that creates the value object
 * with the help of a {@link Creator}
 * 
 * @author Thomas Nill
 *
 */
public class CreatorValue extends Value {
    private static final Logger log = LoggerFactory.getLogger(CreatorValue.class);

    private Creator creator;

    /**
     * Constructor
     *  
     * @param path
     * @param clazz
     * @param current
     * @param objectOfClass
     * @param creator
     */
    public CreatorValue(XmlElementPath path, Class<?> clazz, CurrentObject current,
            CurrentObject objectOfClass,Creator creator) {
        super(path, clazz, current,objectOfClass);
        Assert.notNull(creator, "The creator should not be null");

        this.creator = creator;
   }


    @Override
    public Object createNewObject() throws Exception{
        log.debug("call the creator {}",getPath());
        return creator.createObject();
    }

}
