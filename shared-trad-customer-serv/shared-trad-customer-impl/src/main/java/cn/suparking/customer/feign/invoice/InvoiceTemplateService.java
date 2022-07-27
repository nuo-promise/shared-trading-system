package cn.suparking.customer.feign.invoice;

import cn.suparking.customer.feign.invoice.fallback.InvoiceTemplateFallbackFactory;
import cn.suparking.order.api.beans.CarGroupOrderDTO;
import cn.suparking.order.api.beans.ParkingOrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "shared-trad-invoice-serv", path = "/invoice-center", fallbackFactory = InvoiceTemplateFallbackFactory.class)
public interface InvoiceTemplateService {

    /**
     * 存储parkingOrder到发票数据库中.
     * @param parkingOrderDTO {@link ParkingOrderDTO}
     * @return  {@link Integer}
     */
    @PostMapping("/invoice-source-api/parking-order")
    Integer createOrUpdateParkingOrderInvoice(@RequestBody ParkingOrderDTO parkingOrderDTO);


    /**
     * parkingOrder 退款,对应到发票数据库中.
     * @param parkingOrderDTO {@link ParkingOrderDTO}
     * @return  {@link Integer}
     */
    @PostMapping("/invoice-source-api/parking-order/refund")
    Integer refundParkingOrderInvoice(@RequestBody ParkingOrderDTO parkingOrderDTO);

    /**
     * 存储 carGroupOrder 到发票数据库中.
     * @param carGroupOrderDTO {@linkplain CarGroupOrderDTO}
     * @return {@linkplain Integer}
     */
    @PostMapping("/invoice-source-api/car-group-order")
    Integer createOrUpdateCarGroupOrderInvoice(@RequestBody CarGroupOrderDTO carGroupOrderDTO);

    /**
     * carGroupOrder退款,对应到发票数据库.
     * @param carGroupOrderDTO {@linkplain CarGroupOrderDTO}
     * @return {@linkplain Integer}
     */
    @PostMapping("/invoice-source-api/car-group-order/refund")
    Integer refundCarGroupOrderInvoice(@RequestBody CarGroupOrderDTO carGroupOrderDTO);
}
