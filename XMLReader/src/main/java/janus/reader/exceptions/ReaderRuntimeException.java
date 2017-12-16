package janus.reader.exceptions;

import janus.reader.util.Assert;

/**
 * class for runtime exception of the reader
 * 
 * @author Thomas Nill
 *
 */
public class ReaderRuntimeException extends RuntimeException {

    /**
     * Constructor
     * 
     * @param cause
     */
    public ReaderRuntimeException( Throwable cause) {
        super(cause);
        Assert.notNull(cause, "The cause should not be null");

    }

    /**
     * Constructor
     * 
     * @param message
     * @param cause
     */
    public ReaderRuntimeException(String message, Throwable cause) {
        super(message, cause);
        Assert.notNull(cause, "The cause should not be null");
        Assert.hasText(message, "The message should not be empty");
    }


}
