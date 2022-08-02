package cn.suparking.customer.feign.invoice;

import api.beans.InvoiceInfoDTO;
import api.beans.InvoiceModelQueryDTO;
import api.beans.InvoiceSourceDTO;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.customer.feign.invoice.fallback.InvoiceTemplateFallbackFactory;
import cn.suparking.invoice.dao.entity.InvoiceModelDO;
import cn.suparking.invoice.dao.vo.InvoiceModelVO;
import cn.suparking.invoice.dao.vo.InvoiceSourceVO;
import cn.suparking.order.api.beans.CarGroupOrderDTO;
import cn.suparking.order.api.beans.ParkingOrderDTO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "shared-trad-invoice-serv", path = "/invoice-center", fallbackFactory = InvoiceTemplateFallbackFactory.class)
public interface InvoiceTemplateService {

    /**
     * 存储parkingOrder到发票数据库中.
     *
     * @param parkingOrderDTO {@link ParkingOrderDTO}
     * @return {@link Integer}
     */
    @PostMapping("/invoice-source-api/parking-order")
    Integer createOrUpdateParkingOrderInvoice(@RequestBody ParkingOrderDTO parkingOrderDTO);


    /**
     * parkingOrder 退款,对应到发票数据库中.
     *
     * @param parkingOrderDTO {@link ParkingOrderDTO}
     * @return {@link Integer}
     */
    @PostMapping("/invoice-source-api/parking-order/refund")
    Integer refundParkingOrderInvoice(@RequestBody ParkingOrderDTO parkingOrderDTO);

    /**
     * 存储 carGroupOrder 到发票数据库中.
     *
     * @param carGroupOrderDTO {@linkplain CarGroupOrderDTO}
     * @return {@linkplain Integer}
     */
    @PostMapping("/invoice-source-api/car-group-order")
    Integer createOrUpdateCarGroupOrderInvoice(@RequestBody CarGroupOrderDTO carGroupOrderDTO);

    /**
     * carGroupOrder退款,对应到发票数据库.
     *
     * @param carGroupOrderDTO {@linkplain CarGroupOrderDTO}
     * @return {@linkplain Integer}
     */
    @PostMapping("/invoice-source-api/car-group-order/refund")
    Integer refundCarGroupOrderInvoice(@RequestBody CarGroupOrderDTO carGroupOrderDTO);

    /**
     * 开票数据存储.
     *
     * @param invoiceModelQueryDTO {@linkplain InvoiceModelQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("/invoice-model-api/makeInvoiceModel")
    SpkCommonResult makeInvoiceModel(@RequestBody InvoiceModelQueryDTO invoiceModelQueryDTO);

    /**
     * 获取可开票列表.
     *
     * @param invoiceSourceDTO {@link InvoiceSourceDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("/invoice-source-api/getInvoiceSource")
    List<InvoiceSourceVO> getInvoiceSource(@RequestBody InvoiceSourceDTO invoiceSourceDTO);

    /**
     * 获取可开票列表.
     *
     * @param invoiceSourceDTO {@link InvoiceSourceDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("/invoice-source-api/getInvoiceContent")
    JSONObject getInvoiceContent(InvoiceSourceDTO invoiceSourceDTO);

    /**
     * 获取用户开票历史列表.
     *
     * @param invoiceModelQueryDTO {@linkplain InvoiceModelQueryDTO}
     * @return {@linkplain InvoiceModelDO}
     */
    @PostMapping("/invoice-model-api/invoiceModelList")
    List<InvoiceModelVO> invoiceModelList(InvoiceModelQueryDTO invoiceModelQueryDTO);

    /**
     * 根据开票历史记录 查询对应的开票订单.
     *
     * @param invoiceSourceDTO {@link InvoiceSourceDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("/invoice-source-api/getInvoiceSourceByNo")
    List<InvoiceSourceVO> getInvoiceSourceByNo(InvoiceSourceDTO invoiceSourceDTO);

    /**
     * 删除常用地址.
     *
     * @param invoiceInfoDTO {@linkplain InvoiceInfoDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("/invoice-info-api/deleteInvoiceInfo")
    Integer deleteInvoiceInfo(InvoiceInfoDTO invoiceInfoDTO);
}
