package janus.reader.adapters;

/**
 * Exeption for the unmashal of values
 * 
 * @author javaman
 *
 */
public class AdapterException extends RuntimeException {

    public AdapterException() {
        super();
    }

    public AdapterException(String message) {
        super(message);
    }

    public AdapterException(Throwable cause) {
        super(cause);
    }

    public AdapterException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdapterException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
