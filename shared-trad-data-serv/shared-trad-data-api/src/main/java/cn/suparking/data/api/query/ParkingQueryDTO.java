package cn.suparking.data.api.query;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ParkingQueryDTO implements Serializable {

    private static final long serialVersionUID = -2606006530153349296L;

    // id
    private String id;

    // 手动离场
    private List<Long> projectIds;

    // 分页-页数
    private Integer page;

    // 分页-条数
    private Integer size;

    // 关键字查询--手机
    private String keyword;

    //状态--是否跨夜--true为跨夜--false为在场--空为不做校验
    private Boolean across;

    //车辆状态(在列表查询中指融合状态-在场车enter / 进场记录parking / 离场记录leave)
    private String parkingState;

    //车辆类型名称
    private String carTypeName;

    //开始时间
    private Long beginTime;

    //结束时间
    private Long endTime;

    // 出场开始时间
    private Long leaveBeginTime;

    // 出场结束时间
    private Long leaveEndTime;

    //区域id
    private String subAreaId;

    //用户id
    private Long userId;

    //停车状态
    private List<String> parkingStateList;
}
