package cn.suparking.invoice.rabbitmq.messagehandler;

import api.beans.PublishData;
import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.invoice.concurrent.SparkingThreadFactory;
import cn.suparking.invoice.configure.properties.RabbitmqProperties;
import cn.suparking.invoice.configure.properties.SharedProperties;
import cn.suparking.invoice.tools.BeansManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class OrderMessageThread {

    private static final Logger LOG = LoggerFactory.getLogger(OrderMessageThread.class);

    // 定时器中断线程Map.
    private static final Map<String, ScheduledFuture<?>> FUTUREMAP = new ConcurrentHashMap<>();

    // 线程执行器.
    private static ScheduledThreadPoolExecutor executor;

    private final RabbitmqProperties rabbitmqProperties = BeansManager.getBean("RabbitmqProperties", RabbitmqProperties.class);

    private final SharedProperties sharedProperties = BeansManager.getBean("SharedProperties", SharedProperties.class);

    private final RabbitTemplate rabbitTemplate = BeansManager.getBean("MQCloudTemplate", RabbitTemplate.class);

    private String groupId;

    public OrderMessageThread() {
        if (Objects.isNull(executor)) {
            executor = new ScheduledThreadPoolExecutor(sharedProperties.getCorePoolSize(), SparkingThreadFactory.create("invoice-order-message", true));
        }
    }

    /**
     * 开启线程处理接收到的消息.
     * @param groupId {@link String}
     */
    public void orderMessageHandler(final String groupId) {

        this.groupId = groupId;

        ScheduledFuture<?> orderFuture = FUTUREMAP.get(this.groupId);
        if (Objects.nonNull(orderFuture)) {
            LOG.info("组对应的执行器已存在，组编号： " + groupId);
            return;
        }

        orderFuture = executor.scheduleWithFixedDelay(this::messageHandler, 0, sharedProperties.getQueryInterval(), TimeUnit.MILLISECONDS);
        FUTUREMAP.put(this.groupId, orderFuture);
        LOG.info("组编号: " + this.groupId + " 处理接收到订单信息,开启成功...");
    }

    private void messageHandler() {
        Long startMilli;
        Long endMilli;
        GroupInfoService.setGroupThreadFlag(groupId, true);
        try {
            Message message = GroupInfoService.pollMessage(groupId);
            if (Optional.ofNullable(message).isPresent()) {
                startMilli = DateUtils.getCurrentMillis();
                LOG.info("取出数据,开始执行前:{}", startMilli);
                // 取出 有数据 则进行处理
                this.orderMQMessageHandler(message);
                endMilli = DateUtils.getCurrentMillis();
                LOG.info("处理完成,当前时间:{},耗时:{},同时重置组的超时时间", endMilli, endMilli - startMilli);
                GroupInfoService.resetMilli(groupId);
            } else {
                // 如果此时数据 空了, 那么判断 超过指定时间 则退出线程
                if (GroupInfoService.quitThreadJudge(groupId, rabbitmqProperties.getThreadTimeout())) {
                    LOG.info("判断队列为空超过 {} 毫秒,即将退出线程", rabbitmqProperties.getThreadTimeout());
                    GroupInfoService.setGroupThreadFlag(groupId, false);
                    FUTUREMAP.get(groupId).cancel(true);
                    FUTUREMAP.remove(groupId);
                }
            }
        } catch (Exception ex) {
            Arrays.stream(ex.getStackTrace()).forEach(item -> LOG.error(item.toString()));
        }
    }

    private void orderMQMessageHandler(final Message message) {
        try {
            String topic = (String) message.getMessageProperties().getHeaders().get("topic");
            String body = new String(message.getBody());
            PublishData publishData = JSON.parseObject(body, PublishData.class);
            if (!Objects.nonNull(publishData.getFrom()) && (publishData.getFrom().equals("PARKING") || publishData.getFrom().equals("GROUP"))) {
                String from = publishData.getFrom();
                JSONObject result = new JSONObject();
                if ("PARKING".equals(from)) {
                    // 根据类型将data数据转换.然后落库
                } else {
                    // 根据类型将data数据转换.然后落库
                }

                // 处理完之后 发送消息到MQ
                String replyTo = message.getMessageProperties().getReplyTo();
                if (Objects.nonNull(replyTo)) {
                    MessageProperties replyMessageProperties = new MessageProperties();
                    replyMessageProperties.setHeader("method", "INVOICE_ORDER_ACK_RET");
                    replyMessageProperties.setCorrelationId(message.getMessageProperties().getCorrelationId());
                    Message messageRet = new Message(result.toJSONString().getBytes(), replyMessageProperties);
                    rabbitTemplate.send(message.getMessageProperties().getReplyTo(), messageRet);
                }

            } else {
                LOG.error(publishData.getFrom() + " 暂不支持此类型订单落库操作, topic: " + topic);
            }
        } catch (Exception ex) {
            Arrays.stream(ex.getStackTrace()).forEach(item -> LOG.error(item.toString()));
        }
    }
}
