package cn.suparking.data.api.beans;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static cn.suparking.data.api.constant.DataConstant.CTP_TYPE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkStatusModel implements Serializable {

    private static final long serialVersionUID = -16026974984618756L;

    // 设备编码 - 地锁编号
    private String lockCode;

    // 车位编号
    private String parkingCode;

    //地锁类型
    private String lockType = CTP_TYPE;

    // 0 在线 1 复位 2 逃费 3 状态上报 4 按钮按下
    private Integer status;

    // 停车状态 false: 无车  true: 有车
    private Boolean parkStatus;


    // 挡板 UP 上升 DOWN 下降
    private String armsStatus;

    //true 报警 false 未报警
    private Boolean warn;

    //状态变化时间
    private Long eventTime;

    // 数据类型
    private Integer dataType;

    // 电池电压
    private String voltage;

    // 状态码1 8位16进制字符串
    private String statusOne;

    // 状态码2 12位16进制字符串
    private String statusTwo;

    // 数据时间 yyyy-MM-dd HH:mm:ss
    private String time;
}
