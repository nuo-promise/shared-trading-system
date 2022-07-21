package cn.suparking.customer.configuration;

import cn.hutool.core.date.DateUtil;
import cn.suparking.common.api.utils.RandomCharUtils;
import cn.suparking.customer.spring.SharedTradCustomerInit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class ParkConsumer implements MessageListener {

    @Override
    public void onMessage(final Message message) {
        log.info("接收到支付库更新通知消息 ========> 时间: " + DateUtil.now());
        char randomChar = RandomCharUtils.getRandomChar();
        try {
            SharedTradCustomerInit.initPayTool(randomChar, log);
        } catch (Exception ex) {
            log.error("初始化支付库异常: " + Arrays.toString(ex.getStackTrace()));
            Arrays.stream(ex.getStackTrace()).forEach(item -> log.error(item.toString()));
        }
    }
}
