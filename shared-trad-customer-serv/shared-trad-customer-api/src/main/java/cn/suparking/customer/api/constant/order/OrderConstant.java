package cn.suparking.customer.api.constant.order;

public class OrderConstant {
    /**
     * 终端号.
     */
    public static final String ORDER_TERM_NO = "502";

    /**
     * 未开票.
     */
    public static final String INVOICE_STATE_UNISSUED = "UNISSUED";

    /**
     * 已开纸质票.
     */
    public static final String INVOICE_STATE_PAPER = "PAPER";

    /**
     * 已开电子票.
     */
    public static final String INVOICE_STATE_ELECTRONIC = "ELECTRONIC";

    /**
     * 未退费.
     */
    public static final String REFUND_STATE_NONE = "NONE";

    /**
     * 已部分退费.
     */
    public static final String REFUND_STATE_PARTIAL = "PARTIAL";

    /**
     * 已退费.
     */
    public static final String REFUND_STATE_REFUNDED = "REFUNDED";
}
