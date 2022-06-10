package cn.suparking.data.mq.messagehandler;

import lombok.Builder;
import lombok.Data;
import org.springframework.amqp.core.Message;

import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@Data
@Builder
public class GroupInfoObj {

    // 处理组信息的线程标志 true 正在处理,false 处理完成
    private AtomicBoolean threadFlag;

    // 线程开启时间 毫秒,每次用这个时间与当前时间做对比,如果当前没有任务处理,那么可以退出 关闭,节省系统资源,如果当前有任务,并且当前
    // 时间快到了,那么重置时间
    private AtomicLong startDealMillisecond;

   // 保存已在组中的项目编号
    private Vector<String> projectNos;

    // 存放需要处理的消息队列
    private ConcurrentLinkedQueue<ParkingLockMessageModel> messages;
}
