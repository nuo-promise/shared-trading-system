package cn.suparking.common.api.exception;

/**
 * ValidFailException.
 */
public class ValidFailException extends SpkCommonException {
    public ValidFailException(final Throwable e) {
        super(e);
    }

    public ValidFailException(final String message) {
        super(message);
    }

    public ValidFailException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
