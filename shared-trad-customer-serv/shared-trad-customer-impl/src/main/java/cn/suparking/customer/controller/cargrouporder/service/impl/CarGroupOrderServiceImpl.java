package cn.suparking.customer.controller.cargrouporder.service.impl;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.DateUtils;
import cn.suparking.customer.api.beans.cargroup.CarGroupDTO;
import cn.suparking.customer.api.beans.vip.VipPayDTO;
import cn.suparking.customer.api.constant.ParkConstant;
import cn.suparking.customer.api.constant.order.OrderConstant;
import cn.suparking.customer.controller.cargrouporder.service.CarGroupOrderService;
import cn.suparking.customer.feign.invoice.InvoiceTemplateService;
import cn.suparking.customer.feign.order.OrderTemplateService;
import cn.suparking.order.api.beans.CarGroupOrderDTO;
import cn.suparking.order.dao.entity.CarGroupOrderDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Objects;

@Slf4j
@Service
public class CarGroupOrderServiceImpl implements CarGroupOrderService {

    private final OrderTemplateService orderTemplateService;

    private final InvoiceTemplateService invoiceTemplateService;

    public CarGroupOrderServiceImpl(final OrderTemplateService orderTemplateService,
                                    final InvoiceTemplateService invoiceTemplateService) {
        this.orderTemplateService = orderTemplateService;
        this.invoiceTemplateService = invoiceTemplateService;
    }

    /**
     * 新增/修改合约订单.
     *
     * @param carGroupOrderDTO 订单内容
     * @return SpkCommonResult {@linkplain SpkCommonResult}
     */
    @Override
    public SpkCommonResult createOrUpdate(final CarGroupOrderDTO carGroupOrderDTO) {
        Long result = orderTemplateService.createOrUpdate(carGroupOrderDTO);
        if (Objects.isNull(result) || result < 0) {
            return SpkCommonResult.error("合约订单操作失败");
        }
        return SpkCommonResult.success(result);
    }

    /**
     * 组织合约订单数据.
     *
     * @param vipPayDTO   {@linkplain VipPayDTO}
     * @param carGroup    {@linkplain CarGroupDTO}
     * @param operateType 操作类型 新办 NEW 续费 RENEW
     * @return {@link CarGroupOrderDTO}
     * @author ZDD
     * @date 2022/7/22 18:25:13
     */
    @Override
    public CarGroupOrderDTO makeCarGroupOrder(final VipPayDTO vipPayDTO, final CarGroupDTO carGroup, final String orderState, final String operateType) {
        CarGroupOrderDTO carGroupOrder = new CarGroupOrderDTO();

        Long beginDate = DateUtils.getMillByDateStartStr(vipPayDTO.getBeginDate());
        Long endDate = DateUtils.getMillByDateEndStr(vipPayDTO.getEndDate());
        String orderNo = vipPayDTO.getOrderNo();

        carGroupOrder.setProjectNo(carGroup.getProjectNo());
        carGroupOrder.setUserId(carGroup.getUserId());
        //设置支付订单号
        carGroupOrder.setOrderNo(orderNo);
        //设置合约id
        carGroupOrder.setCarGroupId(String.valueOf(carGroup.getId()));
        //设置车辆类型id
        carGroupOrder.setCarTypeId(String.valueOf(carGroup.getCarTypeId()));
        //设置车辆类型名称
        carGroupOrder.setCarTypeName(carGroup.getCarTypeName());
        //设置租约协议id
        carGroupOrder.setProtocolId(String.valueOf(carGroup.getProtocolId()));
        //设置租约协议名称
        carGroupOrder.setProtocolName(carGroup.getProtocolName());
        //设置订单续期
        carGroupOrder.setBeginTime(beginDate);
        carGroupOrder.setEndTime(endDate);
        //设置总金额
        carGroupOrder.setTotalAmount(vipPayDTO.getDueAmount());
        //设置优惠金额
        carGroupOrder.setDiscountedAmount(0);
        //设置应付金额
        carGroupOrder.setDueAmount(vipPayDTO.getDueAmount());
        //设置支付渠道
        carGroupOrder.setPayChannel(ParkConstant.PAY_TYPE);
        //设置支付方式
        carGroupOrder.setPayType(ParkConstant.PAY_TYPE);
        //开票状态
        carGroupOrder.setInvoiceState(OrderConstant.INVOICE_STATE_UNISSUED);
        //退费状态
        carGroupOrder.setRefundState(OrderConstant.REFUND_STATE_NONE);
        //终端号
        carGroupOrder.setTermNo(OrderConstant.ORDER_TERM_NO);

        if (orderNo.endsWith("W")) {
            carGroupOrder.setPayType(ParkConstant.WXPAY);
        }
        if (orderNo.endsWith("A")) {
            carGroupOrder.setPayType(ParkConstant.ALIPAY);
        }
        //设置联系人手机
        carGroupOrder.setUserMobile(carGroup.getUserMobile());
        //设置车主姓名
        carGroupOrder.setUserName(carGroup.getUserName());
        //设置订单状态
        carGroupOrder.setOrderState(orderState);
        //设置创建人
        carGroupOrder.setCreator(ParkConstant.OPERATOR);
        //设置修改人
        carGroupOrder.setModifier(ParkConstant.OPERATOR);
        //设置创建时间
        carGroupOrder.setDateCreated(new Timestamp(System.currentTimeMillis()));
        //设置更新时间
        carGroupOrder.setDateUpdated(new Timestamp(System.currentTimeMillis()));
        //设置订单类型
        carGroupOrder.setOrderType(operateType);
        return carGroupOrder;
    }

    @Override
    public CarGroupOrderDO findByOrderNo(final String orderNo) {
        CarGroupOrderDTO carGroupOrderDTO = new CarGroupOrderDTO();
        carGroupOrderDTO.setOrderNo(orderNo);
        return orderTemplateService.findByOrderNo(carGroupOrderDTO);
    }
}
