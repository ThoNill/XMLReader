package janus.reader.exceptions;

/**
 * class for runtime exception of the reader
 * 
 * @author Thomas Nill
 *
 */
public class ReaderRuntimeException extends RuntimeException {

    /**
     * Constructor
     */
    public ReaderRuntimeException() {
        super();
    }

    /**
     * Constructor
     * 
     * @param message
     */
    public ReaderRuntimeException(String message) {
        super(message);
    }

    /**
     * Constructor
     * 
     * @param cause
     */
    public ReaderRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor
     * 
     * @param message
     * @param cause
     */
    public ReaderRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor
     * 
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public ReaderRuntimeException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
