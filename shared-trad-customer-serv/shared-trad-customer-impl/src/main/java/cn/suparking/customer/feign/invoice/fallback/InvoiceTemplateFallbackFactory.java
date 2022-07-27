package cn.suparking.customer.feign.invoice.fallback;

import cn.suparking.customer.feign.invoice.InvoiceTemplateService;
import cn.suparking.order.api.beans.CarGroupOrderDTO;
import cn.suparking.order.api.beans.ParkingOrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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
        };
    }
}
