package cn.suparking.data.mq.messagehandler;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.data.Application;
import cn.suparking.data.exception.SharedTradException;
import cn.suparking.data.mq.messageTemplate.MessageTemplate;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static cn.suparking.data.api.constant.DataConstant.CTP_REQUEST_PARK_STATUS_ACK;
import static cn.suparking.data.api.constant.DataConstant.TOPIC_DATA;

public abstract class MessageHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MessageHandler.class);

    private static final RabbitTemplate RABBIT_TEMPLATE = Application.getBean("MQCloudTemplate", RabbitTemplate.class);

    private final String method;

    /**
     * constructor.
     * @param method service name
     */
    public MessageHandler(final String method) {
        this.method = method;
    }

    /**
     * 消费类统一入口,然后调用专属方法,处理完之后 根据返回值,调用template 发送返回.
     * @param messageTemplate {@link MessageTemplate}
     */
    public static void consume(final MessageTemplate messageTemplate) {
        SpkCommonResult retBody = null;
        Message message = messageTemplate.getMessage();
        MessageProperties messageProperties = message.getMessageProperties();
        Map<String, Object> headers = messageProperties.getHeaders();

        String from = (String) headers.get("from");
        String topic = (String) headers.get("topic");
        String rcvts = (String) headers.get("timestamp");
        String reqBody = new String(message.getBody());
        String method = "MQ_" + from + "_" + headers.get("method");

        if (Application.containsBean(method) && topic.contains(TOPIC_DATA)) {
            LOG.info("#" + from + "-" + method + " <== " + reqBody + " from cloud rabbit at " + rcvts);
            MessageHandler messageHandler = Application.getBean(method, MessageHandler.class);
            try {
                retBody = messageHandler.consumeMessage(messageTemplate);
            } catch (SharedTradException ex) {
                LOG.warn("Shared Trade Data Exception. " + ex);
                retBody = SpkCommonResult.error(method + " consume error " + ex.getMessage());
            } catch (Exception ex) {
                Arrays.stream(ex.getStackTrace()).forEach(item -> LOG.error(item.toString()));
                retBody = SpkCommonResult.error(method + " consume error" + ex.getMessage());
            }

            if (Objects.nonNull(retBody)) {
                String replyTo = message.getMessageProperties().getReplyTo();
                String sndts = DateUtils.timestamp();
                LOG.info("#" + from + "-" + topic + " ==> " + retBody + " to " + replyTo + " at " + sndts);

                if (Objects.nonNull(replyTo)) {
                    MessageProperties replyMessageProperties = new MessageProperties();
                    replyMessageProperties.setHeader("method", messageHandler.method);
                    replyMessageProperties.setCorrelationId(messageProperties.getCorrelationId());
                    Message messageRet = new Message(((JSONObject) JSONObject.toJSON(retBody)).toJSONString().getBytes(), replyMessageProperties);
                    RABBIT_TEMPLATE.send(replyTo, messageRet);
                } else {
                    LOG.error(CTP_REQUEST_PARK_STATUS_ACK + " is discarded as replyTo is null");
                }
            }
        } else {
            LOG.warn("方法: " + method + " 数据类型: " + topic + " ,未查询到系统存在此方法,忽略");
        }
    }

    /**
     * 具体消费消息方法.
     * @param message {@link MessageTemplate}
     * @return response
     * @throws SharedTradException {@link SharedTradException}
     */
    public abstract SpkCommonResult consumeMessage(MessageTemplate message) throws SharedTradException;
}
