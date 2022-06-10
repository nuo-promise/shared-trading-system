package cn.suparking.data.mq.messagehandler;

import cn.suparking.common.api.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
public class GroupInfoService {

    private static final Map<String, GroupInfoObj> GROUP_INFO_MAP = new ConcurrentHashMap<>(1);

    /**
     * 返回当前组的线程状态.
     * @param groupId 组ID
     * @return {@link Boolean}
     */
    public static Boolean getGroupThreadStatus(final String groupId) {
        return GROUP_INFO_MAP.get(groupId).getThreadFlag().get();
    }

    /**
     * 判断是否超时 退出线程.
     * @param groupId 组ID
     * @param milli 毫秒
     * @return {@link Boolean}
     */
    public static Boolean quitThreadJudge(final String groupId, final Long milli) {
        if (System.currentTimeMillis() - GROUP_INFO_MAP.get(groupId).getStartDealMillisecond().get() >= milli) {
            return true;
        }
        return false;
    }

    /**
     * 重置线程时间.
     * @param groupId 组ID
     */
    public static void resetMilli(final String groupId) {
        GROUP_INFO_MAP.get(groupId).getStartDealMillisecond().set(System.currentTimeMillis());
    }

    /**
     * 出队.
     * @param groupId 组ID
     * @return {@link Message}
     */
    public static ParkingLockMessageModel pollMessage(final String groupId) {
        return GROUP_INFO_MAP.get(groupId).getMessages().poll();
    }

    /**
     * 插入数据.
     * @param groupInfoObj 组ID
     */
    public static String insertGroupInfoObj(final GroupInfoObj groupInfoObj) {
        String groupId = UUIDUtils.getUuid();
        GROUP_INFO_MAP.put(groupId, groupInfoObj);
        return groupId;
    }

    /**
     * 获取组信息.
     * @param projectNo project no
     * @param messageQueueLength messageQueueLength
     * @return {@Link MessageObj}
     */
    public static MessageObj findGroupInfoObj(final String projectNo, final Integer messageQueueLength) {
        MessageObj messageObj = null;
        for (Map.Entry<String, GroupInfoObj> entry : GROUP_INFO_MAP.entrySet()) {
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
     * 设置线程状态.
     * @param groupId 组ID
     * @param flag status
     */
    public static void setGroupThreadFlag(final String groupId, final Boolean flag) {
        GROUP_INFO_MAP.get(groupId).getThreadFlag().set(flag);
    }

    /**
     * 数据入队.
     * @param message {@link Message}
     * @param groupId 组ID
     * @param projectNo 项目编号
     */
    public static void setMessageInfo(final ParkingLockMessageModel message, final String groupId, final String projectNo) {

        // 入队之前判断下此组中是否存在此项目编号,如果不存在 加入,如果存在 则 忽略
        if (!GROUP_INFO_MAP.get(groupId).getProjectNos().contains(projectNo)) {
            GROUP_INFO_MAP.get(groupId).getProjectNos().add(projectNo);
        }

        if (GROUP_INFO_MAP.get(groupId).getMessages().offer(message)) {
            log.info("项目组:{},MQ 入队成功,入队消息: " + GROUP_INFO_MAP.get(groupId).getProjectNos().toString(), message);
        } else {
            log.warn("项目组:{},MQ 入队失败,入队消息: " + GROUP_INFO_MAP.get(groupId).getProjectNos().toString(), message);
        }
    }
}
