package cn.suparking.customer.spring;

import cn.suparking.common.api.utils.RandomCharUtils;
import cn.suparking.customer.exception.SharedTradException;
import com.alibaba.fastjson.JSONObject;
import com.suparking.payutils.controller.ShuBoPaymentUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static cn.suparking.common.api.exception.SpkCommonCode.SUCCESS;

@Slf4j
@Component
public class SharedTradCustomerInit implements ApplicationRunner {

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        log.info("共享消费服务启动, 开始初始化支付库");
        char randomChar = RandomCharUtils.getRandomChar();
        try {
            initPayTool(randomChar, log);
        } catch (SharedTradException ex) {
            log.error("初始化支付库异常: " + Arrays.toString(ex.getStackTrace()));
            Arrays.stream(ex.getStackTrace()).forEach(item -> log.error(item.toString()));
        }
    }

    /**
     * 初始化支付库.
     * @param randomChar 加密随机字符串
     * @param log 日志记录器
     * @throws Exception 异常
     */
    public static void initPayTool(final char randomChar, final Logger log) throws Exception {
        String initRet = ShuBoPaymentUtils.initPayment("", randomChar);
        JSONObject initObj = JSONObject.parseObject(initRet);
        if (initObj.getIntValue("status") == SUCCESS) {
            JSONObject resultObj = initObj.getJSONObject("result");
            String resultCode = resultObj.getString("result_code");
            if ("0".equals(resultCode)) {
                log.info("支付库初始化成功");
            } else {
                log.warn("支付库初始化失败返回: " + initRet);
            }
        } else {
            log.warn("支付库初始化失败返回: " + initRet);
        }
    }
}
