package cn.suparking.customer.api.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 锁状态逻辑.
 * up-升板命令(升板之后不再做车辆状态检测) : 降板,驶离,升板事件
 * 换电立即升板 (有车情况) : 升板事件
 * 二次升板  (已经是升板情况) : 升板事件
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ControlModel implements Serializable {

    private static final long serialVersionUID = 4197342713349993187L;

    // 设备编码
    private String deviceNo;

    // 命令类型 up - 升板 down - 降板 syn - 锁相关操作
    private String cmdType;

    // 6 远程复位 7 强制结束订单 8 有车立即升板 9 二次升板
    private String data;
}
