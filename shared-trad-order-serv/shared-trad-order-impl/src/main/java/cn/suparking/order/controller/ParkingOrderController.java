package cn.suparking.order.controller;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.common.api.utils.SpkCommonAssert;
import cn.suparking.common.api.utils.SpkCommonResultMessage;
import cn.suparking.order.api.beans.OrderDTO;
import cn.suparking.order.api.beans.ParkingOrderDTO;
import cn.suparking.order.api.beans.ParkingOrderQueryDTO;
import cn.suparking.order.api.beans.ParkingQuery;
import cn.suparking.order.dao.entity.ParkingOrderDO;
import cn.suparking.order.dao.vo.LockOrderVO;
import cn.suparking.order.service.ParkingOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.annotation.ShardingSphereTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("parking-order")
public class ParkingOrderController {
    private final ParkingOrderService parkingOrderService;

    public ParkingOrderController(final ParkingOrderService parkingOrderService) {
        this.parkingOrderService = parkingOrderService;
    }

    /**
     * 根据退费订单id查询退费订单信息.
     *
     * @param id 退费订单id
     * @return {@linkplain SpkCommonResult}
     */
    @GetMapping("/{id}")
    public SpkCommonResult detailParkingOrder(@PathVariable("id") final String id) {
        ParkingOrderDO parkingOrderDO = parkingOrderService.findById(id);
        return Optional.ofNullable(parkingOrderDO)
                .map(item -> SpkCommonResult.success(SpkCommonResultMessage.DETAIL_SUCCESS, item))
                .orElseGet(() -> SpkCommonResult.error("订单信息不存在"));
    }

    /**
     * 创建或修改订单信息.
     *
     * @param parkingOrderDTO 订单详情信息
     * @return Integer
     */
    @PostMapping("")
    public SpkCommonResult createParkingOrder(@Valid @RequestBody final ParkingOrderDTO parkingOrderDTO) {
        return Optional.ofNullable(parkingOrderDTO)
                .map(item -> {
                    SpkCommonAssert.notBlank(item.getOrderNo(), "订单号不能为空");
                    Long id = parkingOrderService.createOrUpdate(item);
                    return SpkCommonResult.success(SpkCommonResultMessage.CREATE_SUCCESS, id);
                }).orElseGet(() -> SpkCommonResult.error("订单信息不存在"));
    }

    /**
     * 生成订单数据.
     * @param orderDTO {@link OrderDTO}
     * @return {@link Boolean}
     */
    @PostMapping("/parkingOrder")
    @ShardingSphereTransactionType(TransactionType.BASE)
    public Boolean createAndUpdateParkingOrder(@Valid @RequestBody final OrderDTO orderDTO) {
        SpkCommonAssert.notNull(orderDTO.getParkingOrder(), "Order信息不能为null");
        SpkCommonAssert.notBlank(orderDTO.getPayType(), "支付类型信息不能为空");
        SpkCommonAssert.notBlank(orderDTO.getTermNo(), "支付终端不能为空");
        SpkCommonAssert.notBlank(orderDTO.getPlateForm(), "支付渠道不能为空");
        return parkingOrderService.createAndUpdateParkingOrder(orderDTO);
    }

    /**
     * 根据用户ID 和 开始与结束时间范围查询订单数据.
     * @param parkingQuery {@Link ParkingQuery}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("/findByUserIdsAndBeginTimeOrEndTimeRange")
    public SpkCommonResult findByUserIdsAndBeginTimeOrEndTimeRange(@RequestBody final ParkingQuery parkingQuery) {
        return Optional.ofNullable(parkingQuery)
                .map(item -> {
                    SpkCommonAssert.notNull(item.getUserIds(), "用户信息不能为空");
                    SpkCommonAssert.notNull(item.getBegin(), "开始时间不能为空");
                    SpkCommonAssert.notNull(item.getEnd(), "结束时间不能为空");
                    return parkingOrderService.findByUserIdsAndBeginTimeOrEndTimeRange(parkingQuery);
                }).orElseGet(() -> SpkCommonResult.error("订单信息不存在"));
    }

    /**
     * 根据用户ID 和时间段查询订单信息.
     * @param parkingQuery {@link ParkingQuery}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("/findByUserIdsAndEndTimeRange")
    public SpkCommonResult findByUserIdsAndEndTimeRange(@RequestBody final ParkingQuery parkingQuery) {
        return Optional.ofNullable(parkingQuery)
                .map(item -> {
                    SpkCommonAssert.notNull(item.getUserIds(), "用户信息不能为空");
                    SpkCommonAssert.notNull(item.getBegin(), "开始时间不能为空");
                    SpkCommonAssert.notNull(item.getEnd(), "结束时间不能为空");
                    return parkingOrderService.findByUserIdsAndEndTimeRange(parkingQuery);
                }).orElseGet(() -> SpkCommonResult.error("订单信息不存在"));
    }


    /**
     * 根据用户ID 查询下一次开始时间.
     * @param parkingQuery {@Link ParkingQuery}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("/findNextAggregateBeginTime")
    public SpkCommonResult findNextAggregateBeginTime(@RequestBody final ParkingQuery parkingQuery) {
        return Optional.ofNullable(parkingQuery)
                .map(item -> {

                    SpkCommonAssert.notNull(item.getUserIds(), "用户信息不能为空");
                    return parkingOrderService.findNextAggregateBeginTime(parkingQuery);
                }).orElseGet(() -> SpkCommonResult.error("订单信息不存在"));
    }

    /**
     * 根据用户ID 时间范围查询订单信息.
     * @param parkingOrderQueryDTO {@link ParkingOrderQueryDTO}
     * @return {@link List}
     */
    @PostMapping("/findOrderByUserId")
    public LinkedList<LockOrderVO> findOrderByUserId(@RequestBody final ParkingOrderQueryDTO parkingOrderQueryDTO) {
        return parkingOrderService.findLockOrderByUserId(parkingOrderQueryDTO);
    }
}
