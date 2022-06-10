package cn.suparking.data.mq.messagehandler;

import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.data.api.beans.ParkStatusModel;
import cn.suparking.data.api.beans.PublishData;
import cn.suparking.data.configuration.properties.MQConfigProperties;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static cn.suparking.data.api.constant.DataConstant.CTP_REQUEST_PARK_STATUS_ACK;
import static cn.suparking.data.api.constant.DataConstant.CTP_TYPE;

@Slf4j
@Component
public class DeviceMessageThread {

    private final RabbitTemplate rabbitTemplate;

    @Resource
    private MQConfigProperties mqConfigProperties;

    public DeviceMessageThread(@Qualifier("MQCloudTemplate") final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Receive Device Message Handler.
     * @param groupId group id.
     */
    @Async("shardTradingExecutor")
    public void deviceMessageHandler(final String groupId) {
        boolean flag = true;
        log.info("组ID: " + groupId + "处理线程开启成功");
        Long startMilli;
        Long endMilli;
        GroupInfoService.setGroupThreadFlag(groupId, true);
        while (flag) {
            try {
                ParkingLockMessageModel message = GroupInfoService.pollMessage(groupId);
                if (Optional.ofNullable(message).isPresent()) {
                    startMilli = DateUtils.getCurrentMillis();
                    log.info("取出数据,开始执行前:{}", startMilli);
                    // 取出 有数据 则进行处理
                    this.deviceMQMessageHandler(message);
                    endMilli = DateUtils.getCurrentMillis();
                    log.info("处理完成,当前时间:{},耗时:{},同时重置组的超时时间", endMilli, endMilli - startMilli);
                    GroupInfoService.resetMilli(groupId);
                } else {

                    // 如果此时数据 空了, 那么判断 超过指定时间 则退出线程
                    if (GroupInfoService.quitThreadJudge(groupId, mqConfigProperties.getThreadTimeout())) {
                        log.info("判断队列为空超过 {} 毫秒,即将退出线程", mqConfigProperties.getThreadTimeout());
                        GroupInfoService.setGroupThreadFlag(groupId, false);
                        flag = false;
                        break;
                    }
                }
                Thread.sleep(100);
            } catch (Exception ex) {
                Arrays.stream(ex.getStackTrace()).forEach(item -> log.error(item.toString()));
            }
        }
        log.info("线程组 " + groupId + " 退出成功");
    }

    /**
     * 处理CTP地锁状态上报数据.
     * @param parkingLockMessage {@link ParkingLockMessageModel}
     */
    private void deviceMQMessageHandler(final ParkingLockMessageModel parkingLockMessage) {
        try {
            Message message = parkingLockMessage.getMessage();
            String retBody = "";
            MessageProperties messageProperties = message.getMessageProperties();
            String from = (String) message.getMessageProperties().getHeaders().get("from");
            String topic = (String) message.getMessageProperties().getHeaders().get("topic");
            String body = new String(message.getBody());
            PublishData publishData = JSON.parseObject(body, PublishData.class);
            if (CTP_TYPE.equals(from) && topic.contains("device.status")) {
                ParkStatusModel parkStatusModel = JSON.parseObject(publishData.getData(), ParkStatusModel.class);
                retBody = invoke(parkStatusModel);
            }
            if (StringUtils.isNotBlank(retBody)) {
                String replyTo = message.getMessageProperties().getReplyTo();
                String sndts = DateUtils.timestamp();
                log.info("#" + from + "-" + topic + " ==> " + retBody + " to " + replyTo + " at " + sndts);

                if (Objects.nonNull(replyTo)) {
                    MessageProperties replyMessageProperties = new MessageProperties();
                    replyMessageProperties.setHeader("method", CTP_REQUEST_PARK_STATUS_ACK);
                    replyMessageProperties.setCorrelationId(messageProperties.getCorrelationId());
                    Message messageRet = new Message(retBody.getBytes(), replyMessageProperties);
                    rabbitTemplate.send(replyTo, messageRet);
                } else {
                    log.error(CTP_REQUEST_PARK_STATUS_ACK + " is discarded as replyTo is null");
                }
            }
        } catch (Exception ex) {
            Arrays.stream(ex.getStackTrace()).forEach(item -> log.error(item.toString()));
        }
    }

    private String invoke(final ParkStatusModel parkStatusModel) {
        // 此处进行组织 Parking 数据 落库
        return "";
    }
}
