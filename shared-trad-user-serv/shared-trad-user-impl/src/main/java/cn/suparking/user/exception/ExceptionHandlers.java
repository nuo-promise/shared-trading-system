package cn.suparking.user.exception;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.exception.SpkCommonCode;
import cn.suparking.common.api.exception.SpkCommonException;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller Method Resolver.
 */
@Slf4j
@ResponseBody
@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(Exception.class)
    protected SpkCommonResult handleExceptionHandler(final Exception exception) {
        log.error(exception.getMessage(), exception);
        String message;
        if (exception instanceof SpkCommonException) {
            SpkCommonException spkCommonException = (SpkCommonException) exception;
            message = spkCommonException.getMessage();
        } else {
            message = "The System is busy, please try again later";
        }
        return SpkCommonResult.error(message);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    protected SpkCommonResult handleDuplicateKeyException(final DuplicateKeyException exception) {
        log.error("duplicate key exception ", exception);
        return SpkCommonResult.error(SpkCommonResultMessage.UNIQUE_INDEX_CONFLICT_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    protected SpkCommonResult handleNullPointException(final NullPointerException exception) {
        log.error("null pointer exception ", exception);
        return SpkCommonResult.error(SpkCommonCode.NOT_FOUND_EXCEPTION, SpkCommonResultMessage.NOT_FOUND_EXCEPTION);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected SpkCommonResult handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        log.warn("http request method not supported", e);
        StringBuilder sb = new StringBuilder();
        sb.append(e.getMethod());
        sb.append(
                " method is not supported for this request. Supported methods are ");
        Objects.requireNonNull(e.getSupportedHttpMethods()).forEach(t -> sb.append(t).append(" "));
        return SpkCommonResult.error(sb.toString());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected SpkCommonResult handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.warn("method argument not valid", e);
        BindingResult bindingResult = e.getBindingResult();
        String errorMsg = bindingResult.getFieldErrors().stream()
                .map(f -> f.getField().concat(": ").concat(Optional.ofNullable(f.getDefaultMessage()).orElse("")))
                .collect(Collectors.joining("| "));
        return SpkCommonResult.error(String.format("Request error! invalid argument [%s]", errorMsg));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected SpkCommonResult handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        log.warn("missing servlet request parameter", e);
        return SpkCommonResult.error(String.format("%s parameter is missing", e.getParameterName()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected SpkCommonResult handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        log.warn("method argument type mismatch", e);
        return SpkCommonResult.error(String.format("%s should be of type %s", e.getName(), Objects.requireNonNull(e.getRequiredType()).getName()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected SpkCommonResult handleConstraintViolationException(final ConstraintViolationException e) {
        log.warn("constraint violation exception", e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        return SpkCommonResult.error(violations.stream()
                .map(v -> v.getPropertyPath().toString().concat(": ").concat(v.getMessage()))
                .collect(Collectors.joining("| ")));
    }

    @ExceptionHandler(SpkCommonException.class)
    protected SpkCommonResult handleShenyuException(final SpkCommonException exception) {
        log.error("shared user exception ", exception);
        return SpkCommonResult.error(SpkCommonCode.ERROR, exception.getMessage());

    }
}
