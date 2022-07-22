package cn.suparking.customer.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.suparking.payutils.controller.ShuBoPaymentUtils;
import com.suparking.payutils.model.APIOrderNo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static cn.suparking.customer.api.constant.ParkConstant.PAY_TERM_NO;

public class OrderUtils {

    private static final Logger LOG = LoggerFactory.getLogger(OrderUtils.class);

    /**
     * 删除订单redis缓存.
     * @param orderNo String
     * @return Boolean
     */
    public static Boolean deleteOrder(final String orderNo) {
        return ReactiveRedisUtils.deleteValue(orderNo).block(Duration.ofMillis(3000));
    }

    /**
     * 检查订单是否存在.
     * @param orderNo String
     * @return {@link Boolean}
     */
    public static String checkOrder(final String orderNo) {
        return (String) ReactiveRedisUtils.getData(orderNo).block(Duration.ofMillis(3000));
    }

    /**
     * 将订单信息存入redis.
     * @param orderNo 订单号.
     * @return 执行结果.
     */
    public static Boolean saveOrder(final String orderNo) {
        return ReactiveRedisUtils.putValue(orderNo, orderNo, 3 * 60).block(Duration.ofMillis(3000));
    }

    /**
     * 支付库生成订单.
     * @param projectNo {@link String}
     * @param tmpOrderNo {@link String}
     * @return {@link String}
     */
    public static String getOrderNo(final String projectNo, final String tmpOrderNo) {
        APIOrderNo apiOrderNo = new APIOrderNo();
        apiOrderNo.setProjectNo(projectNo);
        apiOrderNo.setTermInfo(PAY_TERM_NO);
        apiOrderNo.setBusinessType("0".charAt(0));
        apiOrderNo.setPayChannel("0".charAt(0));
        apiOrderNo.setPayType("0".charAt(0));
        String orderNo = tmpOrderNo;
        try {
            String result = ShuBoPaymentUtils.getOrderNo(apiOrderNo);
            JSONObject jsonObj = JSON.parseObject(result);
            if (jsonObj.getInteger("status") == 200) {
                JSONObject resultObj = JSON.parseObject(jsonObj.getString("result"));
                if (resultObj.getString("result_code").equals("0")) {
                    orderNo = resultObj.getString("orderNo");
                }
            }
        } catch (Exception e) {
            LOG.error("支付库生成订单号异常: " + e.getMessage());
        }
        return orderNo;
    }
}
