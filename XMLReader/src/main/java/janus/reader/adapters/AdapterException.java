package janus.reader.adapters;

/**
 * Exeption for the unmashal of values
 * 
 * @author javaman
 *
 */
public class AdapterException extends RuntimeException {

    /**
     * Constructor
     */
    public AdapterException() {
        super();
    }

    /**
     * Constructor
     * 
     * @param message
     */
    public AdapterException(String message) {
        super(message);
    }

    /**
     * Constructor
     * 
     * @param cause
     */
    public AdapterException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor
     * 
     * @param message
     * @param cause
     */
    public AdapterException(String message, Throwable cause) {
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
    public AdapterException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
