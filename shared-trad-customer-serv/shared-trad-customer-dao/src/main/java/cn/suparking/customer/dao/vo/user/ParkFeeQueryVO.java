package cn.suparking.customer.dao.vo.user;

import cn.suparking.customer.api.beans.parkfee.DiscountInfo;
import cn.suparking.data.dao.entity.DiscountInfoDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkFeeQueryVO {

    // 待使用优惠券列表
    private List<DiscountInfoDO> discountInfoList;

    // 当前使用的优惠券信息
    private DiscountInfo discountInfo;

    // 入场记录状态ID
    private String parkingId;

    // 临时订单号 根据 expireTime 存入缓存
    private String tmpOrderNo;

    // 项目编号
    private String projectNo;

    // 用户ID
    private String userId;

    //总金额 分
    private Integer totalAmount;

    // 应付金额
    private Integer dueAmount;

    // 优惠时长
    private Integer discountedMinutes;

    // 优惠金额
    private Integer discountedAmount;

    // 车主身份
    private String carTypeClass;

    private String carTypeName;

    // 入场时间
    private Long beginTime;

    private Long endTime;

    // 停车时长
    private Integer parkingMinutes;

    // 预付费
    private Integer paidAmount;

    // 车位编号
    private String parkNo;

    private String parkName;

    private String parkId;

    // 设备编号
    private String deviceNo;

    // 订单有效期
    private Long expireTime;
}
