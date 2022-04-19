package cn.suparking.common.api.exception;

/**
 * ResourceNotFoundException.
 */
public class ResourceNotFoundException extends SpkCommonException {
    public ResourceNotFoundException(final Throwable e) {
        super(e);
    }

    public ResourceNotFoundException(final String message) {
        super(message);
    }

    public ResourceNotFoundException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
