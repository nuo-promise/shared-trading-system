package cn.suparking.data.service.impl;

import cn.suparking.common.api.beans.SpkCommonResult;
import cn.suparking.data.api.beans.ParkConfigDTO;
import cn.suparking.data.service.ParkConfigService;
import cn.suparking.data.tools.ProjectConfigUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ParkConfigServiceImpl implements ParkConfigService {

    @Override
    public SpkCommonResult parkingConfig(final ParkConfigDTO parkConfigDTO) {
        if (parkConfigDTO.getType().equals("ADD")) {
            ProjectConfigUtils.push(parkConfigDTO);
        } else {
            ProjectConfigUtils.remove(parkConfigDTO.getProjectNo(), parkConfigDTO.getResource());
        }
        log.info("项目缓存更新之后 : " + ProjectConfigUtils.show());
        return SpkCommonResult.success("处理成功");
    }
}
