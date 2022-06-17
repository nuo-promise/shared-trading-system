package cn.suparking.data.controller;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.data.api.beans.ParkConfigDTO;
import cn.suparking.data.api.beans.ParkingLockModel;
import cn.suparking.data.api.query.ParkEventQuery;
import cn.suparking.data.api.query.ParkQuery;
import cn.suparking.data.dao.entity.ParkingDO;
import cn.suparking.data.dao.entity.ParkingEventDO;
import cn.suparking.data.dao.entity.ParkingTriggerDO;
import cn.suparking.data.service.CtpDataService;
import cn.suparking.data.service.ParkConfigService;
import cn.suparking.data.service.ParkingEventService;
import cn.suparking.data.service.ParkingTriggerService;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("data-api")
public class ParkingController {

    private final CtpDataService ctpDataService;

    private final ParkingTriggerService parkingTriggerService;

    private final ParkingEventService parkingEventService;

    private final ParkConfigService parkConfigService;

    public ParkingController(final ParkConfigService parkConfigService, final CtpDataService ctpDataService,
                             final ParkingTriggerService parkingTriggerService, final ParkingEventService parkingEventService) {
        this.parkConfigService = parkConfigService;
        this.ctpDataService = ctpDataService;
        this.parkingTriggerService = parkingTriggerService;
        this.parkingEventService = parkingEventService;
    }

    /**
     * 地锁复位时候先查询业务是否允许降板.
     * @param params {@link JSONObject}
     * @return {@link SpkCommonResult}
     */
    public SpkCommonResult searchBoardStatus(@RequestBody final JSONObject params) {
        return ctpDataService.searchBoardStatus(params);
    }

    /**
     * receive ctp park status.
     * @param params device data
     * @return {@link SpkCommonResult}
     */
    @PostMapping("/parkStatus")
    public SpkCommonResult parkStatus(@RequestBody final JSONObject params) {
        return ctpDataService.parkStatus(params);
    }

    /**
     * 项目配置信息变更通知.
     * @param parkSettingDTO {@link ParkConfigDTO}
     * @return {@link SpkCommonResult}
     */
    @PostMapping("/parkConfig")
    public SpkCommonResult parkingConfig(@Valid @RequestBody final ParkConfigDTO parkSettingDTO) {
        return parkConfigService.parkingConfig(parkSettingDTO);
    }

    /**
     * 根据deviceNo 查询 车位锁信息.
     * @param deviceNo device no.
     * @return {@link ParkingLockModel}
     */
    @GetMapping("/findParkingLock")
    public ParkingLockModel findParkingLock(@RequestParam("deviceNo")final String deviceNo) {
        return ctpDataService.findParkingLock(deviceNo);
    }

    /**
     * 查询最近一次入场记录.
     * @param parkQuery {@link ParkQuery}
     * @return {@Link ParkingDO}
     */
    @PostMapping("/findParking")
    public ParkingDO findParking(@RequestBody final ParkQuery parkQuery) {
        return ctpDataService.findParking(parkQuery);
    }

    /**
     * 根据trigger id 查询 parking trigger event.
     * @param triggerId parking trigger id.
     * @param projectId project id.
     * @return {@link ParkingTriggerDO}
     */
    @GetMapping("/findParkingTrigger")
    public ParkingTriggerDO findParkingTrigger(@RequestParam("projectId")final Long projectId, @RequestParam("triggerId")final Long triggerId) {
        return parkingTriggerService.findByProjectIdAndId(projectId, triggerId);
    }

    /**
     * 根据项目编号,事件ID 查询事件信息.
     * @param parkEventQuery {@link ParkEventQuery}
     * @return {@link List}
     */
    @PostMapping("/findParkingEvents")
    public List<ParkingEventDO> findParkingEvents(@RequestBody final ParkEventQuery parkEventQuery) {
        return parkingEventService.findParkingEvents(parkEventQuery);
    }
}
