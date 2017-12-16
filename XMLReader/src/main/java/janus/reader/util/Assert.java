package janus.reader.util;


public class Assert {

    public static void notNull(Object obj, String errorMessage) {
        if (obj == null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void noNullElements(Object[] objects, String errorMessage) {
        if (objects == null) {
            throw new IllegalArgumentException(errorMessage);
        }
        for(Object o : objects) {
            notNull(o,errorMessage);
        }
    }

    public static void hasText(String text, String errorMessage) {
        notNull(text,errorMessage);
        if ("".equals(errorMessage.trim())) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

}
