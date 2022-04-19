package cn.suparking.common.api.utils;

import cn.suparking.common.api.exception.SpkCommonException;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.util.Optional;

/**
 * sh512 Encryption.
 */
public class ShaUtils {

    /**
     * sh512 Encryption string.
     *
     * @param src the src
     * @return the string
     */
    public static String shaEncryption(final String src) {
        return Optional.ofNullable(src).map(item -> {
            if (StringUtils.isEmpty(src)) {
                return null;
            }
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
                messageDigest.update(item.getBytes());
                byte[] byteBuffer = messageDigest.digest();
                StringBuffer strHexString = new StringBuffer();
                for (byte b:byteBuffer) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                return strHexString.toString();
            } catch (Exception e) {
                throw new SpkCommonException(e);
            }
        }).orElse(null);
    }
}
