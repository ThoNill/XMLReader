package janus.reader.util;

/**
 * assertions like in spring
 * 
 * @author javaman
 *
 */
public class Assert {
    
    /**
     * no object of this class should be created
     */
    private Assert() {
        super();
    }
    
    /**
     * test if not null
     * 
     * @param obj
     * @param errorMessage
     */
    public static void notNull(Object obj, String errorMessage) {
        if (obj == null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    /**
     * test if arry of objects, all are not null
     * 
     * @param objects
     * @param errorMessage
     */
    public static void noNullElements(Object[] objects, String errorMessage) {
        if (objects == null) {
            throw new IllegalArgumentException(errorMessage);
        }
        for (Object o : objects) {
            notNull(o, errorMessage);
        }
    }

    /**
     * test if text is not null and not empty
     * 
     * @param text
     * @param errorMessage
     */
    public static void hasText(String text, String errorMessage) {
        notNull(text, errorMessage);
        if ("".equals(text.trim())) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

}
