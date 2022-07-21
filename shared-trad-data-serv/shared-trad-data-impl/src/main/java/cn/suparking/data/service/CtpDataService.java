package cn.suparking.data.service;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.data.api.beans.ParkingLockModel;
import cn.suparking.data.api.beans.ProjectConfig;
import cn.suparking.data.api.parkfee.Parking;
import cn.suparking.data.api.query.ParkQuery;
import cn.suparking.data.dao.entity.ParkingDO;
import com.alibaba.fastjson.JSONObject;

public interface CtpDataService {

    /**
     * ctp park status save.
     * @param obj {@Link JSONObject}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult parkStatus(JSONObject obj);

    /**
     * 地锁复位时候先查询业务是否允许降板.
     * @param params {@link JSONObject}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult searchBoardStatus(JSONObject params);

    /**
     * 根据设备编号查询车位信息.
     * @param deviceNo device no
     * @return {@link ParkingLockModel}
     */
    ParkingLockModel findParkingLock(String deviceNo);

    /**
     * 根据场库信息,车位ID查询最近入场记录.
     * @param parkQuery {@link ParkQuery}
     * @return {@link ParkingDO}
     */
    ParkingDO findParking(ParkQuery parkQuery);

    /**
     * 根据项目编号查询Config信息.
     * @param projectNo String
     * @return {@link ProjectConfig}
     */
    ProjectConfig getProjectConfig(String projectNo);

    /**
     * 更新parking.
     * @param parking {@link Parking}
     * @return {@link Boolean}
     */
    Boolean createAndUpdateParking(Parking parking);
}
