package janus.reader;

public class ReaderRuntimeException extends RuntimeException {

    public ReaderRuntimeException() {
    }

    public ReaderRuntimeException(String message) {
        super(message);
    }

    public ReaderRuntimeException(Throwable cause) {
        super(cause);
    }

    public ReaderRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReaderRuntimeException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
