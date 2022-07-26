package cn.suparking.invoice.rabbitmq.messagehandler;

import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.common.api.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class GroupInfoService {

    private static Map<String, GroupInfoObj> groupInfoMap = new ConcurrentHashMap<>(1);

    /**
     * 返回当前组的线程状态.
     * @param groupId  groupId
     * @return Boolean
     */
    public static Boolean getGroupThreadStatus(final String groupId) {
        return groupInfoMap.get(groupId).getThreadFlag().get();
    }

    /**
     * 判断是否超时 退出线程.
     * @param groupId groupId
     * @param milli milli second
     * @return Boolean
     */
    public static Boolean quitThreadJudge(final String groupId, final Long milli) {
        if (DateUtils.getCurrentMillis() - groupInfoMap.get(groupId).getStartDealMillisecond().get() >= milli) {
            return true;
        }
        return false;
    }

    /**
     * 重置线程时间.
     * @param groupId groupId
     */
    public static void resetMilli(final String groupId) {
        groupInfoMap.get(groupId).getStartDealMillisecond().set(DateUtils.getCurrentMillis());
    }

    /**
     * 出队.
     * @param groupId groupId
     * @return Message
     */
    public static Message pollMessage(final String groupId) {
        return groupInfoMap.get(groupId).getMessages().poll();
    }

    /**
     * 插入数据.
     * @param groupInfoObj groupInfoObj
     * @return String
     */
    public static String insertGroupInfoObj(final GroupInfoObj groupInfoObj) {
        String groupId = UUIDUtils.getUuid();
        groupInfoMap.put(groupId, groupInfoObj);
        return groupId;
    }

    /**
     * 根据 项目编号 查询消息组里面是否存在,如果存在 返回消息组的 key 如果不存在 返回null.
     * @param projectNo {@link String}
     * @param messageQueueLength {@link Integer}
     * @return {@link MessageObj}
     */
    public static MessageObj findGroupInfoObj(final String projectNo, final Integer messageQueueLength) {
        MessageObj messageObj = null;
        for (Map.Entry<String, GroupInfoObj> entry : groupInfoMap.entrySet()) {
            GroupInfoObj groupInfoObj = entry.getValue();
            // 先判断如果队列不足设定的个数 也将组ID 返回出来
            if (groupInfoObj.getProjectNos().size() < messageQueueLength) {
                if (Optional.ofNullable(messageObj).isPresent()) {
                    messageObj.setGroupId(entry.getKey());
                } else {
                    messageObj = MessageObj.builder().groupId(entry.getKey()).build();
                }
            }
            if (groupInfoObj.getProjectNos().contains(projectNo)) {
                if (Optional.ofNullable(messageObj).isPresent()) {
                    messageObj.setGroupId(entry.getKey());
                } else {
                    messageObj = MessageObj.builder().groupId(entry.getKey()).build();
                }
                break;
            }
        }
        return messageObj;
    }

    /**
     * 设置组对应的线程状态.
     * @param flag {@link Boolean}
     * @param groupId {@link String}
     */
    public static void setGroupThreadFlag(final String groupId, final Boolean flag) {
        groupInfoMap.get(groupId).getThreadFlag().set(flag);
    }

    /**
     * 数据入队.
     * @param message {@link Message}
     * @param groupId {@link String}
     * @param projectNo {@link String}
     */
    public static void setMessageInfo(final Message message, final String groupId, final String projectNo) {
        // 入队之前判断下此组中是否存在此项目编号,如果不存在 加入,如果存在 则 忽略
        if (!groupInfoMap.get(groupId).getProjectNos().contains(projectNo)) {
            groupInfoMap.get(groupId).getProjectNos().add(projectNo);
        }
        if (groupInfoMap.get(groupId).getMessages().offer(message)) {
            log.info("项目组:{},MQ 入队成功,入队消息:{}", groupInfoMap.get(groupId).getProjectNos().toString(), message);
        } else {
            log.warn("项目组:{},MQ 入队失败,入队消息:{}", groupInfoMap.get(groupId).getProjectNos().toString(), message);
        }
    }
}
