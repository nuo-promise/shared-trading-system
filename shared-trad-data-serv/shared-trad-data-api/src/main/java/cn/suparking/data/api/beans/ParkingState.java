package cn.suparking.data.api.beans;

public enum ParkingState {
    //已入场
    //以下均为完成开闸状态
    ENTERED,

    //已入场（强制开闸）1通道开启强制开闸模式2白名单起作用
    ENTERED_FORCE,

    //已出场
    LEFT,

    //已出场（强制开闸）1通道开启强制开闸模式2白名单起作用3车辆类型免匹配
    LEFT_FORCE,

    //已出场（异常开闸）
    LEFT_ABNORMAL,

    //等待入场
    //以下均为等待开闸状态
    ENTER_WAIT,

    //等待支付
    PAY_WAIT,

    //等待出场（未匹配）
    LEAVE_UNMATCHED
}
