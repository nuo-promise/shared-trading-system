package cn.suparking.data.mq.messageTemplate;

import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.data.configuration.properties.MQConfigProperties;
import cn.suparking.data.mq.messagehandler.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component("DeviceMessageThread")
public class DeviceMessageThread {
    @Resource
    private MQConfigProperties mqConfigProperties;

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
                MessageTemplate message = GroupInfoService.pollMessage(groupId);
                if (Optional.ofNullable(message).isPresent()) {
                    startMilli = DateUtils.getCurrentMillis();
                    log.info("取出数据,开始执行前:{}", startMilli);
                    // 取出 有数据 则进行处理
                    MessageHandler.consume(message);
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
}
