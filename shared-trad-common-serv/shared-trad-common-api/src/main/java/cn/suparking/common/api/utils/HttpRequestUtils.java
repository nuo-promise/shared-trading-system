package cn.suparking.common.api.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.suparking.common.api.exception.SpkCommonException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 外部api请求工具类.
 */
@Slf4j
public class HttpRequestUtils {

    /**
     * 请求外部api --> get请求.
     *
     * @param url       请求路径
     * @param paramsMap 请求参数
     * @return String
     */
    public static JSONObject sendGet(final String url, final Map<String, Object> paramsMap) {
        return sendGet(url, paramsMap, 15000);
    }

    /**
     * 请求外部api --> get请求.
     *
     * @param url       请求路径
     * @param paramsMap 请求参数
     * @param timeout   超时时间
     * @return String
     */
     private static JSONObject sendGet(final String url, final Map<String, Object> paramsMap, final int timeout) {
        try {
            String params = httpHeaderParams(paramsMap);
            log.info("[GET]请求外部api ======> 请求路径 [{}]    请求参数 [{}]", url, params);
            HttpResponse response = HttpRequest.get(url + params).timeout(timeout).execute();
            String body = response.body();
            log.info("[GET]请求外部api ======> 请求结果 [{}]", body);
            return JSON.parseObject(body);
        } catch (Exception e) {
            log.error("请求外部api ======> 请求异常 [{}]", ExceptionMsgUtils.exceptionMsg(e));
            throw new SpkCommonException("请求失败");
        }
    }

    /**
     * 请求外部api --> post请求.
     *
     * @param url       请求路径
     * @param paramsMap 请求参数
     * @return String
     */
    public static JSONObject sendPost(final String url, final Map<String, Object> paramsMap) {
        return sendPost(url, paramsMap, 15000);
    }

    /**
     * 请求外部api --> post请求.
     *
     * @param url       请求路径
     * @param paramsMap 请求参数
     * @param timeout   超时时间
     * @return String
     */
    private static JSONObject sendPost(final String url, final Map<String, Object> paramsMap, final int timeout) {
        try {
            String params = JSONObject.toJSONString(paramsMap);
            log.info("[POST]请求外部api ======> 请求路径 [{}]    请求参数 [{}]", url, params);
            HttpResponse response = HttpRequest.post(url).body(params).timeout(timeout).execute();
            String body = response.body();
            log.info("[POST]请求外部api ======> 请求结果 [{}]", body);
            return JSON.parseObject(body);
        } catch (Exception e) {
            log.error("请求外部api ======> 请求异常 [{}]", ExceptionMsgUtils.exceptionMsg(e));
            throw new SpkCommonException("请求失败");
        }
    }

    /**
     * 将参数拼接成请求头格式参数.
     *
     * @param paramsMap 请求参数
     * @return String
     */
    private static String httpHeaderParams(final Map<String, Object> paramsMap) {
        StringBuilder sb = new StringBuilder("?");
        paramsMap.keySet().stream()
                .forEach(key -> sb.append(key).append("=").append(paramsMap.get(key)).append("&"));
        return sb.substring(0, sb.length() - 1);
    }
}
