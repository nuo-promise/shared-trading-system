package cn.suparking.customer.controller.cargrouporder.service.impl;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.customer.api.beans.cargrouporder.CarGroupOrderDTO;
import cn.suparking.customer.controller.cargrouporder.service.CarGroupOrderService;
import cn.suparking.customer.feign.order.OrderTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CarGroupOrderServiceImpl implements CarGroupOrderService {

    private final OrderTemplateService orderTemplateService;

    public CarGroupOrderServiceImpl(final OrderTemplateService orderTemplateService) {
        this.orderTemplateService = orderTemplateService;
    }

    /**
     * 新增/修改合约订单.
     *
     * @param carGroupOrderDTO 订单内容
     * @return SpkCommonResult {@linkplain SpkCommonResult}
     */
    @Override
    public SpkCommonResult createOrUpdate(final CarGroupOrderDTO carGroupOrderDTO) {
        Integer count = orderTemplateService.createCarGroupOrder(carGroupOrderDTO);
        if (count > 0) {
            return SpkCommonResult.success();
        }
        return SpkCommonResult.error("操作失败");
    }
}
