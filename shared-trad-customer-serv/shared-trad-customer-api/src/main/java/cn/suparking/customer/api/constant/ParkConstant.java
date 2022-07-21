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
     * 根据场库编号、协议id获取场库信息和合约协议信息.
     */
    public static final String INTERFACE_MYVIPCARINFO = "/vipGroup/myVipCarInfo";

    /**
     * 获取所有可线上办理的协议列表.
     */
    public static final String INTERFACE_NEWPROTOCOL = "/vipGroup/newProtocol";

    /**
     * 获取指定场库可线上办理的协议列表.
     */
    public static final String INTERFACE_NEWPROTOCOLBYPROJECTNO = "/vipGroup/newProtocolByProjectNo";

    /**
     * 获取所有可线上办理的场库.
     */
    public static final String INTERFACE_PROJECTLIST = "/vipGroup/projectListByNos";

    /**
     * 根据项目编号获取场库信息.
     */
    public static final String INTERFACE_PARKBYPROJECT = "/map/projectInfoByProjectNo";

    /**
     * 根据项目编号,场库编号获取设备信息.
     */
    public static final String INTERFACE_GETDEVICENO = "/map/getDeviceNo";

    // 核销优惠券方法名
    public static final String INTERFACE_DISCOUNT_USED = "/used";

    // CTP 设备控制
    public static final String INTERFACE_CTP_CONTROL_DEVICE = "/ctp/ctpControlCmd";

    // 升板
    public static final String CTP_CONTROL_UP_TYPE = "up";

    // 降板
    public static final String CTP_CONTROL_DOWN_TYPE = "down";
}
