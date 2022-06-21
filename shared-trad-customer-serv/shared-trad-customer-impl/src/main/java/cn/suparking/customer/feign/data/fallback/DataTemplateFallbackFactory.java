package cn.suparking.customer.feign.data.fallback;

import cn.suparking.customer.feign.data.DataTemplateService;
import cn.suparking.customer.feign.user.UserTemplateService;
import cn.suparking.data.api.beans.ParkingLockModel;
import cn.suparking.data.api.beans.ProjectConfig;
import cn.suparking.data.api.query.ParkEventQuery;
import cn.suparking.data.api.query.ParkQuery;
import cn.suparking.data.dao.entity.ParkingDO;
import cn.suparking.data.dao.entity.ParkingEventDO;
import cn.suparking.data.dao.entity.ParkingTriggerDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * UserTemplateService hystrix 降级工厂.
 */
@Slf4j
@Component
public class DataTemplateFallbackFactory implements FallbackFactory<DataTemplateService> {

    /**
     * 降级将Throwable 作为入参传递.
     * @param cause {@link Throwable}
     * @return {@link UserTemplateService}
     */
    @Override
    public DataTemplateService create(final Throwable cause) {
        Arrays.stream(cause.getStackTrace()).forEach(item -> log.error(item.toString()));
        return new DataTemplateService() {
            @Override
            public ParkingLockModel findParkingLock(final String deviceNo) {
                log.error("DataTemplateService: findParkingLock error: " + cause.getMessage());
                return null;
            }

            @Override
            public ParkingDO findParking(final ParkQuery parkQuery) {
                log.error("DataTemplateService: findParking error: " + cause.getMessage());
                return null;
            }

            @Override
            public ParkingTriggerDO findParkingTrigger(final Long projectId, final Long triggerId) {
                log.error("DataTemplateService: findParkingTrigger error: " + cause.getMessage());
                return null;
            }

            @Override
            public List<ParkingEventDO> findParkingEvents(final ParkEventQuery parkEventQuery) {
                log.error("DataTemplateService: findParkingEvents error: " + cause.getMessage());
                return null;
            }

            @Override
            public ProjectConfig getProjectConfig(final String projectNo) {
                log.error("DataTemplateService: getProjectConfig error: " + cause.getMessage());
                return null;
            }
        };
    }
}
