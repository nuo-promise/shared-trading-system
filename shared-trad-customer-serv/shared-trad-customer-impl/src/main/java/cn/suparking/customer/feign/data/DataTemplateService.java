package cn.suparking.customer.feign.data;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.customer.api.beans.cargroupstock.CarGroupStockOperateRecordDTO;
import cn.suparking.customer.api.beans.cargroupstock.CarGroupStockOperateRecordQueryDTO;
import cn.suparking.customer.feign.data.fallback.DataTemplateFallbackFactory;
import cn.suparking.data.api.beans.ParkingLockModel;
import cn.suparking.data.api.beans.ProjectConfig;
import cn.suparking.data.api.parkfee.Parking;
import cn.suparking.data.api.query.ParkEventQuery;
import cn.suparking.data.api.query.ParkQuery;
import cn.suparking.data.dao.entity.ParkingDO;
import cn.suparking.data.dao.entity.ParkingEventDO;
import cn.suparking.data.dao.entity.ParkingTriggerDO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "shared-trad-data-serv", path = "/data-center", fallbackFactory = DataTemplateFallbackFactory.class)
public interface DataTemplateService {

    /**
     * 根据设备编号,获取车位信息.
     *
     * @param deviceNo device no
     * @return {@link ParkingLockModel}
     */
    @GetMapping("/data-api/findParkingLock")
    ParkingLockModel findParkingLock(@RequestParam("deviceNo") String deviceNo);

    /**
     * 根据车位信息,查询最近一次入场数据.
     *
     * @param parkQuery {@link ParkQuery}
     * @return {@link ParkingDO}
     */
    @PostMapping("/data-api/findParking")
    ParkingDO findParking(@RequestBody ParkQuery parkQuery);

    /**
     * 根据parking trigger.
     *
     * @param projectId project id.
     * @param triggerId trigger id
     * @return {@link ParkingTriggerDO}
     */
    @GetMapping("/data-api/findParkingTrigger")
    ParkingTriggerDO findParkingTrigger(@RequestParam("projectId") Long projectId, @RequestParam("triggerId") Long triggerId);

    /**
     * 根据 projectid 和 事件ids 查询事件.
     *
     * @param parkEventQuery {@link ParkEventQuery}
     * @return {@link List}
     */
    @PostMapping("/data-api/findParkingEvents")
    List<ParkingEventDO> findParkingEvents(@RequestBody ParkEventQuery parkEventQuery);

    /**
     * 根据项目编号获取项目配置基础信息.
     *
     * @param projectNo String
     * @return {@link ProjectConfig}
     */
    @GetMapping("/data-api/getProjectConfig")
    ProjectConfig getProjectConfig(@RequestParam("projectNo") String projectNo);

    /**
     * 合约库存列表.
     *
     * @param carGroupStockOperateRecordQueryDTO {@link CarGroupStockOperateRecordQueryDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("/car-group-stock-log/list")
    SpkCommonResult carGroupStockLogList(@RequestBody CarGroupStockOperateRecordQueryDTO carGroupStockOperateRecordQueryDTO);

    /**
     * 新增合约库存.
     *
     * @param carGroupStockOperateRecordDTO {@link CarGroupStockOperateRecordDTO}
     * @return {@linkplain SpkCommonResult}
     */
    @PostMapping("/car-group-stock-log/insert")
    SpkCommonResult carGroupStockLogInsert(@RequestBody CarGroupStockOperateRecordDTO carGroupStockOperateRecordDTO);

    /**
     * 更新停车记录.
     *
     * @param parking {@link Parking}
     * @return {@link Boolean}
     */
    @PostMapping("/data-api/parking")
    Boolean createAndUpdateParking(@RequestBody Parking parking);
}
