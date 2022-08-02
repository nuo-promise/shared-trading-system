package cn.suparking.customer.feign.invoice.fallback;

import api.beans.InvoiceInfoDTO;
import api.beans.InvoiceModelDTO;
import api.beans.InvoiceModelQueryDTO;
import api.beans.InvoiceSourceDTO;
import api.beans.ProjectConfig;
import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.customer.feign.invoice.InvoiceTemplateService;
import cn.suparking.invoice.dao.entity.InvoiceModelDO;
import cn.suparking.invoice.dao.entity.InvoiceSourceDO;
import cn.suparking.invoice.dao.vo.InvoiceModelVO;
import cn.suparking.invoice.dao.vo.InvoiceSourceVO;
import cn.suparking.order.api.beans.CarGroupOrderDTO;
import cn.suparking.order.api.beans.ParkingOrderDTO;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class InvoiceTemplateFallbackFactory implements FallbackFactory<InvoiceTemplateService> {

    /**
     * 降级将Throwable 作为入参传递.
     * @param cause {@link Throwable}
     * @return  {@link InvoiceTemplateService}
     */
    @Override
    public InvoiceTemplateService create(final Throwable cause) {
        Arrays.stream(cause.getStackTrace()).forEach(item -> log.error(item.toString()));

        return new InvoiceTemplateService() {
            @Override
            public Integer createOrUpdateParkingOrderInvoice(final ParkingOrderDTO parkingOrderDTO) {
                log.error("InvoiceTemplateService: createOrUpdateParkingOrderInvoice error: " + cause.getMessage());
                return null;
            }

            @Override
            public Integer refundParkingOrderInvoice(final ParkingOrderDTO parkingOrderDTO) {
                log.error("InvoiceTemplateService: refundParkingOrderInvoice error: " + cause.getMessage());
                return null;
            }

            @Override
            public Integer createOrUpdateCarGroupOrderInvoice(final CarGroupOrderDTO carGroupOrderDTO) {
                log.error("InvoiceTemplateService: createOrUpdateCarGroupOrderInvoice error: " + cause.getMessage());
                return null;
            }

            @Override
            public Integer refundCarGroupOrderInvoice(final CarGroupOrderDTO carGroupOrderDTO) {
                log.error("InvoiceTemplateService: refundCarGroupOrderInvoice error: " + cause.getMessage());
                return null;
            }

            @Override
            public SpkCommonResult makeInvoiceModel(final InvoiceModelQueryDTO invoiceModelQueryDTO) {
                log.error("InvoiceTemplateService: makeInvoiceModel error: " + cause.getMessage());
                return null;
            }

            @Override
            public List<InvoiceSourceVO> getInvoiceSource(final InvoiceSourceDTO invoiceSourceDTO) {
                log.error("InvoiceTemplateService: getInvoiceSource error: " + cause.getMessage());
                return null;
            }

            @Override
            public JSONObject getInvoiceContent(final InvoiceSourceDTO invoiceSourceDTO) {
                log.error("InvoiceTemplateService: getInvoiceContent error: " + cause.getMessage());
                return null;
            }

            @Override
            public List<InvoiceModelVO> invoiceModelList(final InvoiceModelQueryDTO invoiceModelQueryDTO) {
                log.error("InvoiceTemplateService: invoiceModelList error: " + cause.getMessage());
                return null;
            }

            @Override
            public List<InvoiceSourceVO> getInvoiceSourceByNo(final InvoiceSourceDTO invoiceSourceDTO) {
                log.error("InvoiceTemplateService: getInvoiceSourceByNo error: " + cause.getMessage());
                return null;
            }

            @Override
            public Integer deleteInvoiceInfo(final InvoiceInfoDTO invoiceInfoDTO) {
                log.error("InvoiceTemplateService: deleteInvoiceInfo error: " + cause.getMessage());
                return null;
            }
        };
    }
}
