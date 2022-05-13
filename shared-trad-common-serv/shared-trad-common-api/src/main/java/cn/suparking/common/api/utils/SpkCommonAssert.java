package cn.suparking.common.api.utils;

import cn.suparking.common.api.exception.ResourceNotFoundException;
import cn.suparking.common.api.exception.ValidFailException;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Objects;

/**
 * SPK Common Assert.
 */
@NoArgsConstructor
public final class SpkCommonAssert {

    /**
     * assert obj is not null.
     * @param obj {@link Object}
     * @param msg error msg
     */
    public static void notNull(final Object obj, final String msg) {
        isTrue(Objects.nonNull(obj), msg);
    }

    /**
     * assert obj is null.
     * @param obj {@link Object}
     * @param msg error msg
     */
    public static void isNull(final Object obj, final String msg) {
        isTrue(Objects.isNull(obj), msg);
    }

    /**
     * assert str is blank.
     * @param str {@link String}
     * @param msg error msg
     */
    public static void notBlank(final String str, final String msg) {
        isTrue(StringUtils.isNotBlank(str), msg);
    }

    /**
     * assert collection is not empty.
     *
     * @param collection obj
     * @param message    error message
     */
    public static void notEmpty(final Collection<?> collection, final String message) {
        isTrue(!CollectionUtils.isEmpty(collection), message);
    }

    /**
     * assert flag is true.
     * @param flag {@link Boolean}
     * @param msg error message
     */
    private static void isTrue(final Boolean flag, final String msg) {
        if (!Boolean.TRUE.equals(flag)) {
            throw new ValidFailException(msg);
        }
    }

    /**
     * throw ResourceNotFoundException with default message.
     *
     * @param e exception
     */
    public static void throwException(final Exception e) {
        throw new ResourceNotFoundException("the validation ExistProviderMethod invoked error", e);
    }
}

