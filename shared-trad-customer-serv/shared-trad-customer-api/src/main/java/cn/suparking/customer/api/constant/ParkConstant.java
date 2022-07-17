package cn.suparking.customer.api.constant;

public class ParkConstant {

    public static final String WETCHATMINI = "JSAPI_MINI";

    public static final String PAY_TYPE = "WXPAY";

    public static final String PAY_TERM_NO = "502";

    public static final String ORDER_TYPE = "mini";

    // 优惠券使用超时时间
    public static final Integer DISCOUNT_DELAY_TIME = 180;

    // RPC 设置 默认通道
    public static final String PAYINFO_RESPONSE_QUEUE = "shared.trad.query.pay.response";

    public static final String SUCCESS = "00000";

    /**
     * 获取距离范围内的数据.
     */
    public static final String INTERFACE_NEARBYPARK = "/map/getCoordinate";

    /**
     * 获取所有场库信息.
     */
    public static final String INTERFACE_ALLPARK = "/map/getAllCoordinate";

    /**
     * 获取常去场库.
     */
    public static final String INTERFACE_REGULARPARK = "/map/regularLocations";

    /**
     * 根据设备编号获取场库信息.
     */
    public static final String INTERFACE_PARKBYDEVICE = "/map/projectInfoByDeviceNo";

    /**
     * 根据项目编号获取场库信息.
     */
    public static final String INTERFACE_PARKBYPROJECT = "/map/projectInfoByProjectNo";

    /**
     * 根据项目编号,场库编号获取设备信息.
     */
    public static final String INTERFACE_GETDEVICENO = "/map/getDeviceNo";
}
