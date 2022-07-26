package cn.suparking.invoice.rabbitmq;

import api.beans.PublishData;
import cn.suparking.invoice.configure.properties.RabbitmqProperties;
import cn.suparking.invoice.rabbitmq.messagehandler.GroupInfoObj;
import cn.suparking.invoice.rabbitmq.messagehandler.GroupInfoService;
import cn.suparking.invoice.rabbitmq.messagehandler.MessageObj;
import cn.suparking.invoice.rabbitmq.messagehandler.OrderMessageThread;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component("OrderReceive")
public class OrderReceive implements MessageListener {

    @Resource
    private RabbitmqProperties rabbitmqProperties;

    @Override
    public void onMessage(final Message message) {
        try {
            log.info("Invoice MQ 接收到事件:{}", message.toString());
            String topic = (String) message.getMessageProperties().getHeaders().get("topic");
            String body = new String(message.getBody());
            PublishData publishData = JSON.parseObject(body, PublishData.class);
            String lockCode = "";
            if (Objects.nonNull(publishData)) {
                if (publishData.getFrom().equals("PARKING")) {
                   // 将data转换成parking 数据
                } else if (publishData.getFrom().equals("GROUP")) {
                   // 将 data 转换成 group 订单数据
                } else {
                   log.warn("Invoice MQ 接收到来自未知数据源: " + publishData.getFrom());
                }
            } else {
                log.warn("Invoice Receive MQ Data Error: " + body);
            }
            // 根据项目编号分组
            String projectNo = publishData.getProjectNo();
            if (StringUtils.isNotBlank(projectNo)) {
                MessageObj messageObj = GroupInfoService.findGroupInfoObj(projectNo, rabbitmqProperties.getQueueLength());
                // 如果 返回不为null 那么就直接将消息存入队列
                if (Optional.ofNullable(messageObj).isPresent()) {
                    // 将数据丢进 队列
                    GroupInfoService.setMessageInfo(message, messageObj.getGroupId(), projectNo);
                    // 入队之后 判断 当前组的 线程是否开启,如果未开启 则开启
                    if (!GroupInfoService.getGroupThreadStatus(messageObj.getGroupId())) {
                        new OrderMessageThread().orderMessageHandler(messageObj.getGroupId());
                    }
                } else {
                    // 未找到组信息
                    String groupId = buildGroupInfo(message, projectNo);
                    // 开启 线程处理 事件 只需要将组ID 传递进去即可
                    new OrderMessageThread().orderMessageHandler(groupId);
                }
            } else {
                log.warn("来自: " + publishData.getFrom() + " 地锁,编号: " + lockCode + " : 经过核实,该编号不是平台设备,忽略执行以下逻辑.");
            }
        } catch (Exception ex) {
            Arrays.stream(ex.getStackTrace()).forEach(item -> log.error(item.toString()));
        }
    }

    static String buildGroupInfo(final Message message, final String projectNo) {
        GroupInfoObj groupInfoObj = GroupInfoObj.builder().build();
        AtomicBoolean atomicBoolean = new AtomicBoolean();
        atomicBoolean.set(false);
        groupInfoObj.setThreadFlag(atomicBoolean);
        AtomicLong atomicLong = new AtomicLong();
        atomicLong.set(0L);
        groupInfoObj.setStartDealMillisecond(atomicLong);
        Vector<String> vector = new Vector<>(1);
        vector.add(projectNo);
        groupInfoObj.setProjectNos(vector);
        ConcurrentLinkedQueue<Message> messages = new ConcurrentLinkedQueue<>();
        messages.offer(message);
        groupInfoObj.setMessages(messages);
        return GroupInfoService.insertGroupInfoObj(groupInfoObj);
    }
}
