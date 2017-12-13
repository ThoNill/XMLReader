package janus.reader.nls;

import janus.reader.exceptions.ReaderRuntimeException;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Internationalization of exception messages
 * 
 * @author Thomas Nill
 *
 */
public class Messages {
    private static final Logger log = LoggerFactory
            .getLogger(Messages.class);

    
    private static final String BUNDLE_NAME = "janus.reader.nls.messages"; //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
            .getBundle(BUNDLE_NAME);

    private Messages() {
        super();
    }

    /**
     * search a message 
     * 
     * @param messageName
     * @return
     */
    public static String getString(String messageName) {
        try {
            return RESOURCE_BUNDLE.getString(messageName);
        } catch (MissingResourceException e) {
            log.error("the message for {} doesnt exist {}",messageName,e);
            return '!' + messageName + '!';
        }
    }
    
    /**
     * Throw a {@link IllegalArgumentException}
     * 
     * @param patternName
     * @param arguments
     */
    public static void throwIllegalArgumentException(String patternName,Object ... arguments) {
        String pattern = Messages.getString(patternName);
        throw new IllegalArgumentException(MessageFormat.format(pattern, arguments));
    }
    
    /**
     * Throw a {@link ReaderRuntimeException}
     * 
     * @param cause
     * @param patternName
     * @param arguments
     */
    public static void throwReaderRuntimeException(Throwable cause,String patternName,Object ... arguments)  {
        String pattern = Messages.getString(patternName);
        throw new ReaderRuntimeException(MessageFormat.format(pattern, arguments),cause);
    }

    
}
