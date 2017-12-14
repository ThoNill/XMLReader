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
     * 
     * @param cause
     */
    public ReaderRuntimeException( Throwable cause) {
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


}
