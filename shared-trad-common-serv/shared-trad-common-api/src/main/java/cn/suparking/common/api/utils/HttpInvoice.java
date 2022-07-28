package cn.suparking.common.api.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.nio.charset.StandardCharsets;

/**
 * TODO
 *
 * @author ZDD
 * @date 2022/7/27 22:13:39
 */
public class HttpInvoice {

    public static JSONObject requestMethod(final String url, final String order) {
        HttpClient httpclient = null;
        PostMethod post = null;
        try {
            httpclient = new HttpClient();
            post = new PostMethod(url);
            //设置编码方式
            post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            //添加参数
            post.addParameter("order", order);
            //执行
            httpclient.executeMethod(post);
            //接口返回信息
            String info = new String(post.getResponseBody(), StandardCharsets.UTF_8);
            return JSONObject.parseObject(info);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭连接，释放资源
            post.releaseConnection();
            ((SimpleHttpConnectionManager) httpclient.getHttpConnectionManager()).shutdown();
        }
        return null;
    }
}
