package cn.suparking.customer.feign.order;

import cn.suparking.customer.feign.order.fallback.OrderTemplateFallbackFactory;
import cn.suparking.order.api.beans.CarGroupOrderDTO;
import cn.suparking.order.api.beans.OrderDTO;
import cn.suparking.order.api.beans.ParkingOrderDTO;
import cn.suparking.order.api.beans.ParkingOrderQueryDTO;
import cn.suparking.order.dao.entity.CarGroupOrderDO;
import cn.suparking.order.dao.vo.LockOrderVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.LinkedList;

@FeignClient(value = "shared-trad-order-serv", path = "/order-center", fallbackFactory = OrderTemplateFallbackFactory.class)
public interface OrderTemplateService {

    /**
     * 新增/更新合约订单.
     *
     * @param carGroupOrderDTO 订单内容
     * @return {@linkplain Integer}
     */
    @PostMapping("/car-group-order/createCarGroupOrder")
    Integer createCarGroupOrder(@RequestBody CarGroupOrderDTO carGroupOrderDTO);

    /**
     * 根据orderNo 获取合约订单.
     * @param carGroupOrderDTO {@link CarGroupOrderDTO}
     * @return {@link LinkedList}
     */
    @PostMapping("/car-group-order/findByOrderNo")
    CarGroupOrderDO findByOrderNo(@RequestBody CarGroupOrderDTO carGroupOrderDTO);

    /**
     * 新增或者更新停车订单.
     * @param parkingOrderDTO {@link ParkingOrderDTO}
     * @return {@link Integer}
     */
    @PostMapping("/parking-order/")
    Integer createParkingOrder(@RequestBody ParkingOrderDTO parkingOrderDTO);


    /**
     * 生成订单.
     * @param orderDTO {@link OrderDTO}
     * @return {@link Boolean}
     */
    @PostMapping("/parking-order/parkingOrder")
    Long createAndUpdateParkingOrder(@RequestBody OrderDTO orderDTO);

    /**
     * 根据用户ID 获取订单.
     * @param parkingOrderQueryDTO {@link ParkingOrderQueryDTO}
     * @return {@link LinkedList}
     */
    @PostMapping("/parking-order/findOrderByUserId")
    LinkedList<LockOrderVO> findOrderByUserId(@RequestBody ParkingOrderQueryDTO parkingOrderQueryDTO);


    /**
     * 根据用户ID 获取合约订单信息.
     * @param parkingOrderQueryDTO {@link ParkingOrderQueryDTO}
     * @return {@link LinkedList}
     */
    @PostMapping("/car-group-order/findOrderByOrderNo")
    LinkedList<LockOrderVO> findGroupOrderByUserId(@RequestBody ParkingOrderQueryDTO parkingOrderQueryDTO);
}
