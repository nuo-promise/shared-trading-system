package cn.suparking.invoice.service;

import api.beans.InvoiceInfoQueryDTO;
import api.beans.InvoiceSourceDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.invoice.dao.entity.InvoiceSourceDO;
import cn.suparking.order.api.beans.CarGroupOrderDTO;
import cn.suparking.order.api.beans.CarGroupRefundOrderDTO;
import cn.suparking.order.api.beans.ParkingOrderDTO;
import cn.suparking.order.api.beans.ParkingRefundOrderDTO;

import java.util.List;

public interface InvoiceSourceService {

    /**
     * 获取开票订单列表.
     *
     * @param invoiceSourceDTO {@linkplain InvoiceInfoQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    List<InvoiceSourceDO> invoiceSourceList(InvoiceSourceDTO invoiceSourceDTO);

    /**
     * 获取开票订单列表.
     *
     * @param invoiceSourceDTO {@linkplain InvoiceInfoQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    List<InvoiceSourceDO> invoiceSourceListLikeOrderNo(InvoiceSourceDTO invoiceSourceDTO);

    /**
     * 新增 | 修改 发票订单.
     *
     * @param invoiceSourceDTO {@linkplain InvoiceSourceDTO}
     * @return {@linkplain SpkCommonResult}
     */
    Integer createOrUpdate(InvoiceSourceDTO invoiceSourceDTO);

    /**
     * 根据ID删除.
     *
     * @param id id
     * @return {@Link Integer}
     */
    Integer deleteById(Long id);

    /**
     * 更新或者创建临停订单对应的发票数据.
     * @param parkingOrderDTO {@linkplain ParkingOrderDTO}
     * @return Integer
     */
    Integer createOrUpdateParkingOrderInvoice(ParkingOrderDTO parkingOrderDTO);

    /**
     * 临停订单退费对应发票数据.
     * @param parkingRefundOrderDTO {@linkplain ParkingRefundOrderDTO}
     * @return Integer
     */
    Integer refundParkingOrderInvoice(ParkingRefundOrderDTO parkingRefundOrderDTO);

    /**
     * 更新或者创建合约订单对应的发票数据.
     * @param carGroupOrderDTO {@linkplain CarGroupOrderDTO}
     * @return Integer
     */
    Integer createOrUpdateCarGroupOrderInvoice(CarGroupOrderDTO carGroupOrderDTO);

    /**
     * 临停订单退费对应发票数据.
     * @param carGroupRefundOrderDTO {@linkplain CarGroupRefundOrderDTO}
     * @return Integer
     */
    Integer refundCarGroupOrderInvoice(CarGroupRefundOrderDTO carGroupRefundOrderDTO);

    /**
     * 根据订单号查找开票订单数据.
     *
     * @param invoiceSourceDTO {@linkplain InvoiceSourceDTO}
     * @return {@linkplain SpkCommonResult}
     */
    InvoiceSourceDO findByOrderNo(InvoiceSourceDTO invoiceSourceDTO);
}
