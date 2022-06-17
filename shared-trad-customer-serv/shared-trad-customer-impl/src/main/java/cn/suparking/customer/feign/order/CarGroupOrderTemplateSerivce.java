package cn.suparking.customer.feign.order;

import cn.suparking.customer.api.beans.cargrouporder.CarGroupOrderDTO;
import cn.suparking.user.api.vo.RegisterVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "shared-trad-order-serv",path = "/car-group-order")
public interface CarGroupOrderTemplateSerivce {

    /**
     * 新增/更新合约订单.
     *
     * @param carGroupOrderDTO 订单内容
     * @return {@linkplain RegisterVO}
     */
    @PostMapping("/createCarGroupOrder")
    Integer createCarGroupOrder(@RequestBody CarGroupOrderDTO carGroupOrderDTO);
}
