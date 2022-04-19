package cn.suparking.common.api.exception;

/**
 * Spk Common Exception.
 */
public class SpkCommonException extends RuntimeException {

    /**
     * Instantiates a new Spk Common Exception.
     * @param e {@link Throwable}
     */
    public SpkCommonException(final Throwable e) {
        super(e);
    }

    /**
     * Instantiates a new Spk Common Exception.
     * @param message message
     */
    public SpkCommonException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new Spk Common Exception.
     * @param message message
     * @param throwable {@link Throwable}
     */
    public SpkCommonException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
