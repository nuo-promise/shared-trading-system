package cn.suparking.customer.feign.order.fallback;

import cn.suparking.customer.api.beans.cargrouporder.CarGroupOrderDTO;
import cn.suparking.customer.feign.order.OrderTemplateService;
import cn.suparking.order.api.beans.OrderDTO;
import cn.suparking.order.api.beans.ParkingOrderDTO;
import cn.suparking.order.api.beans.ParkingOrderQueryDTO;
import cn.suparking.order.dao.vo.LockOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;

@Slf4j
@Component
public class OrderTemplateFallbackFactory implements FallbackFactory<OrderTemplateService> {

    /**
     * 降级将Throwable 作为入参传递.
     * @param cause {@link Throwable}
     * @return {@link OrderTemplateService}
     */
    @Override
    public OrderTemplateService create(final Throwable cause) {
        Arrays.stream(cause.getStackTrace()).forEach(item -> log.error(item.toString()));

        return new OrderTemplateService() {

            @Override
            public Integer createCarGroupOrder(final CarGroupOrderDTO carGroupOrderDTO) {
                log.error("OrderTemplateService: createCarGroupOrder error: " + cause.getMessage());
                return null;
            }

            @Override
            public Integer createParkingOrder(final ParkingOrderDTO parkingOrderDTO) {
                log.error("OrderTemplateService: createParkingOrder error: " + cause.getMessage());
                return null;
            }

            @Override
            public Boolean createAndUpdateParkingOrder(final OrderDTO orderDTO) {
                log.error("OrderTemplateService: createAndUpdateParkingOrder error: " + cause.getMessage());
                return false;
            }

            @Override
            public LinkedList<LockOrderVO> findOrderByUserId(final ParkingOrderQueryDTO parkingOrderQueryDTO) {
                log.error("OrderTemplateService: findOrderByUserId error: " + cause.getMessage());
                return null;
            }
        };
    }
}
