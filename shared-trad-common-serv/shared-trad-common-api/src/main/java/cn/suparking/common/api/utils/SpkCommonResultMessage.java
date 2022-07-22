package cn.suparking.common.api.utils;

/**
 * result message.
 */
public final class SpkCommonResultMessage {

    // ======================= order ===========================

    public static final String DISCOUNT_ACTIVE = "优惠券正在使用";

    public static final String ORDER_ACTIVE = "订单正在进行中";

    public static final String ORDER_VALID = "订单非法";

    public static final String ORDER_EXPIRE = "订单过期";

    public static final String SIGN_NOT_VALID = "签名非法";

    public static final String DEVICE_NOT_SETTING = "设备无对应车位";

    public static final String ENTER_PARKING_NOT_FOUND = "无入场记录";

    public static final String CHARGE_VALID = "计费服务错误";

    public static final String CHARGE_CHANGE_DATA_VALID = "无计费信息";

    // parking user valid.
    public static final String PARKING_DATE_USER_VALID = "停车用户数据非法";

    // parking trigger valid
    public static final String PARKING_DATA_TRIGGER_VALID = "停车触发数据非法";

    public static final String PARKING_CONFIG_VALID = "场库配置非法";

    // parking events valid
    public static final String PARKING_DATA_EVENT_VALID = "停车事件非法";

    public static final String TOKEN_HAS_NO_PERMISSION = "token无权限";

    public static final String CAR_GROUP_DATA_VALID = "合约办理失败";

    public static final String UNIQUE_INDEX_CONFLICT_ERROR = "unique index conflict, please enter again";

    public static final String NOT_FOUND_EXCEPTION = "未发现异常";

    public static final String PARAMETER_ERROR = "参数错误";

    public static final String CREATE_SUCCESS = "成功";

    public static final String USER_CREATE_USER_ERROR = "用户信息空,请确认";

    public static final String CAR_PARK_CREATE_USER_ERROR = "empty car park info, please confirm";

    public static final String USER_WALLET_CREATE_USER_ERROR = "empty user wallet info, please confirm";

    public static final String CAR_LICENSE_CREATE_USER_ERROR = "empty car license info, please confirm";

    public static final String MERCHANT_CREATE_USER_ERROR = "empty merchant info, please confirm";

    public static final String MERCHANT_WALLET_CREATE_USER_ERROR = "empty merchant wallet info, please confirm";

    public static final String DETAIL_SUCCESS = "detail success";

    public static final String RESULT_SUCCESS = "result success";

    public static final String USER_QUERY_ERROR = "user info is empty";

    public static final String CAR_PARK_QUERY_ERROR = "car park info is empty";

    public static final String USER_WALLET_QUERY_ERROR = "user wallet info is empty";

    public static final String CAR_LICENSE_QUERY_ERROR = "car license info is empty";

    public static final String MERCHANT_QUERY_ERROR = "merchant info is empty";

    public static final String MERCHANT_WALLET_QUERY_ERROR = "merchant wallet info is empty";
}
