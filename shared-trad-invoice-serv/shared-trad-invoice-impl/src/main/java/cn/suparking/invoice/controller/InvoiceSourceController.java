package cn.suparking.invoice.controller;

import api.beans.InvoiceInfoQueryDTO;
import api.beans.InvoiceSourceDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.invoice.dao.entity.InvoiceSourceDO;
import cn.suparking.invoice.service.InvoiceSourceService;
import cn.suparking.order.api.beans.CarGroupOrderDTO;
import cn.suparking.order.api.beans.CarGroupRefundOrderDTO;
import cn.suparking.order.api.beans.ParkingOrderDTO;
import cn.suparking.order.api.beans.ParkingRefundOrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("invoice-source-api")
public class InvoiceSourceController {

    private final InvoiceSourceService invoiceSourceService;

    public InvoiceSourceController(final InvoiceSourceService invoiceSourceService) {
        this.invoiceSourceService = invoiceSourceService;
    }

    /**
     * 获取开票订单列表.
     *
     * @param invoiceSourceDTO {@linkplain InvoiceInfoQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("invoiceSourceList")
    public SpkCommonResult invoiceSourceList(@RequestBody final InvoiceSourceDTO invoiceSourceDTO) {
        return Optional.ofNullable(invoiceSourceDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(String.valueOf(item.getUserId()), "userId不能为空");
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, invoiceSourceService.invoiceSourceList(invoiceSourceDTO));
                }).orElseGet(() -> SpkCommonResult.error("用户信息不能为空"));
    }

    /**
     * 小程序获取用户开票订单列表.
     *
     * @param invoiceSourceDTO {@linkplain InvoiceInfoQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("getInvoiceSource")
    public SpkCommonResult getInvoiceSource(@RequestBody final InvoiceSourceDTO invoiceSourceDTO) {
        return Optional.ofNullable(invoiceSourceDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(String.valueOf(item.getUserId()), "userId不能为空");
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, invoiceSourceService.getInvoiceSource(invoiceSourceDTO));
                }).orElseGet(() -> SpkCommonResult.error("用户信息不能为空"));
    }

    /**
     * 新增 | 修改 发票订单.
     *
     * @param invoiceSourceDTO {@linkplain InvoiceSourceDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("saveOrUpdate")
    public SpkCommonResult createSharedInvoiceInfo(@RequestBody final InvoiceSourceDTO invoiceSourceDTO) {
        return Optional.ofNullable(invoiceSourceDTO)
                .map(item -> {
                    SpkCommonAssert.notNull(item.getUserId(), "用户id不能为空");
                    Integer createCount = invoiceSourceService.createOrUpdate(invoiceSourceDTO);
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, createCount);
                }).orElseGet(() -> SpkCommonResult.error("发票订单信息不能为空"));
    }


    /**
     * 根据订单号查找开票订单数据.
     *
     * @param invoiceSourceDTO {@linkplain InvoiceSourceDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("findByOrderNo")
    public SpkCommonResult findByOrderNo(@RequestBody final InvoiceSourceDTO invoiceSourceDTO) {
        return Optional.ofNullable(invoiceSourceDTO)
                .map(item -> {
                    SpkCommonAssert.notNull(item.getOrderNo(), "订单号不能为空");
                    InvoiceSourceDO invoiceSourceDO = invoiceSourceService.findByOrderNo(invoiceSourceDTO);
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, invoiceSourceDO);
                }).orElseGet(() -> SpkCommonResult.error("发票订单信息不能为空"));
    }

    /**
     * 存储parkingOrder到发票数据库中.
     *
     * @param parkingOrderDTO {@linkplain ParkingOrderDTO}
     * @return {@linkplain Integer}
     */
    @PostMapping("parking-order")
    public Integer createOrUpdateParkingOrderInvoice(@RequestBody final ParkingOrderDTO parkingOrderDTO) {
        return invoiceSourceService.createOrUpdateParkingOrderInvoice(parkingOrderDTO);
    }

    /**
     * parkingOrder退款,对应到发票数据库.
     *
     * @param parkingRefundOrderDTO {@linkplain ParkingRefundOrderDTO}
     * @return {@linkplain Integer}
     */
    @PostMapping("parking-order/refund")
    public Integer refundParkingOrderInvoice(@RequestBody final ParkingRefundOrderDTO parkingRefundOrderDTO) {
        return invoiceSourceService.refundParkingOrderInvoice(parkingRefundOrderDTO);
    }

    /**
     * 存储 carGroupOrder 到发票数据库中.
     *
     * @param carGroupOrderDTO {@linkplain CarGroupOrderDTO}
     * @return {@linkplain Integer}
     */
    @PostMapping("car-group-order")
    public Integer createOrUpdateCarGroupOrderInvoice(@RequestBody final CarGroupOrderDTO carGroupOrderDTO) {
        return invoiceSourceService.createOrUpdateCarGroupOrderInvoice(carGroupOrderDTO);
    }

    /**
     * carGroupOrder退款,对应到发票数据库.
     *
     * @param carGroupRefundOrderDTO {@linkplain CarGroupRefundOrderDTO}
     * @return {@linkplain Integer}
     */
    @PostMapping("car-group-order/refund")
    public Integer refundCarGroupOrderInvoice(@RequestBody final CarGroupRefundOrderDTO carGroupRefundOrderDTO) {
        return invoiceSourceService.refundCarGroupOrderInvoice(carGroupRefundOrderDTO);
    }
}
