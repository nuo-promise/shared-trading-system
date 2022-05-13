package cn.suparking.common.api.utils;

import java.util.Arrays;

/**
 * ExceptionMsgUtils.
 */
public class ExceptionMsgUtils {

    /**
     * 获取异常信息内容.
     *
     * @param e 异常对象
     * @return String
     */
    public static String exceptionMsg(final Exception e) {
        StringBuffer sb = new StringBuffer();
        Arrays.stream(e.getStackTrace()).forEach(item -> sb.append(item.toString()).append("\n"));
        return sb.toString();
    }
}
