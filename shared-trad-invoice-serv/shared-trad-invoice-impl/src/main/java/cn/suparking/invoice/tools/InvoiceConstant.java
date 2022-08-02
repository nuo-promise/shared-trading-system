package cn.suparking.invoice.tools;

public class InvoiceConstant {

    /**
     * 开票.
     */
    public static final String INTERFACE_INVOICE_MAKE = "/commonInvoice/make";

    /**
     * 开票结果查询.
     */
    public static final String INTERFACE_INVOICE_FIND_INVOICE_RESULT = "/commonInvoice/findInvoiceResult";

    public static final String SUCCESS = "00000";

    //************************ 发票订单类型 *******************************/
    // 临停订单.
    public static final String SOURCE_PARKING = "PARKING";

    // 合约订单.
    public static final String SOURCE_ORDER = "ORDER";

    // 电子钱包订单.
    public static final String SOURCE_WALLET = "WALLET_ORDER";


    //************************ 开票状态 *******************************/
    // 未开票
    public static final String INVOICE_STATE_UNISSUED = "UNISSUED";

    // 纸质开票
    public static final String INVOICE_STATE_PAPER = "PAPER";

    // 电子开票
    public static final String INVOICE_STATE_ELECTRONIC = "ELECTRONIC";


    //************************ 订单退费状态 *******************************/
    // 未退费
    public static final String REFUND_NONE = "NONE";

    // 部分退费
    public static final String REFUND_PARTIAL = "PRATIAL";

    // 完全退费
    public static final String REFUND_REFUNDED = "REFUNDED";


    //************************ 订单状态 *******************************
    // 成功
    public static final String CAR_GROUP_ORDER_SUCCESS = "SUCCESS";

    // 失败
    public static final String CAR_GROUP_ORDER_FAILED = "FAILED";

    // 等待支付
    public static final String CAR_GROUP_ORDER_PENDING = "PENDING";

    // 关闭
    public static final String CAR_GROUP_ORDER_CLOSED = "CLOSED";

    // 异常
    public static final String CAR_GROUP_ORDER_ERROR = "ERROR";


    //************************ 开票结果查询状态 *******************************
    // 开票完成
    public static final String INVOICE_STATE_SUCCESS = "2";

    // 开票中
    public static final String INVOICE_STATE_PENDING = "20";

    // 开票成功签章中
    public static final String INVOICE_STATE_SIGN_PENDING = "21";

    // 开票失败
    public static final String INVOICE_STATE_FAILED = "22";

    // 开票成功签章失败
    public static final String INVOICE_STATE_SIGN_FAILED = "24";
}
