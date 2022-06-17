package cn.suparking.data.mq.messageTemplate;

import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.data.Application;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static cn.suparking.data.api.constant.DataConstant.CTP_REQUEST_PARK_STATUS_ACK;

@Component
public class RabbitUtils {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitUtils.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitUtils(@Lazy final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 发送 MQ ACK.
     * @param message {@link Message}
     * @param from MQ from
     * @param topic MQ topic
     * @param retBody MQ message
     */
    public void sendRabbitAck(final Message message, final String from, final String topic, final String retBody) {
        String replyTo = message.getMessageProperties().getReplyTo();
        String sndts = DateUtils.timestamp();
        LOG.info("#" + from + "-" + topic + " ==> " + retBody + " to " + replyTo + " at " + sndts);

        if (Objects.nonNull(replyTo)) {
            MessageProperties replyMessageProperties = new MessageProperties();
            replyMessageProperties.setHeader("method", CTP_REQUEST_PARK_STATUS_ACK);
            replyMessageProperties.setCorrelationId(message.getMessageProperties().getCorrelationId());
            Message messageRet = new Message(JSON.toJSONString(retBody).getBytes(), replyMessageProperties);
            rabbitTemplate.send(replyTo, messageRet);
        } else {
            LOG.error(CTP_REQUEST_PARK_STATUS_ACK + " is discarded as replyTo is null");
        }
    }
}
