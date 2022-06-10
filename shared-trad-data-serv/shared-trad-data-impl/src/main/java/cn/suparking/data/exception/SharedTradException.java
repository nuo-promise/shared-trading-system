package cn.suparking.data.exception;

public class SharedTradException extends RuntimeException {
    private static final long serialVersionUID = 8068509879445395353L;

    /**
     * Instantiates a new Spk exception.
     * @param throwable the throwable
     */
    public SharedTradException(final Throwable throwable) {
        super(throwable);
    }

    /**
     * Instantiates a new Spk exception.
     * @param message the message
     */
    public SharedTradException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new Spk exception.
     * @param message the message
     * @param throwable the throwable
     */
    public SharedTradException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
