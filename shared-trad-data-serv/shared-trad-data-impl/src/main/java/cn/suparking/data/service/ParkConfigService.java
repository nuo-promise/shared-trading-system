package cn.suparking.data.service;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.data.api.beans.ParkConfigDTO;

public interface ParkConfigService {

    /**
     * 配置信息变更通知,更新缓存.
     * @param parkConfigDTO {@link ParkConfigDTO}
     * @return {@link SpkCommonResult}
     */
    SpkCommonResult parkingConfig(ParkConfigDTO parkConfigDTO);
}
