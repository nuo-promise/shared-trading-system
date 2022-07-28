package cn.suparking.invoice.service;

import api.beans.ProjectConfig;
import cn.suparking.common.api.utils.HttpUtils;
import cn.suparking.invoice.configuration.InvoiceProperties;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service("ProjectConfigService")
public class ProjectConfigService {

    private final Map<String, ProjectConfig> projectConfigMap = new ConcurrentHashMap<String, ProjectConfig>(10);

    @Resource
    private InvoiceProperties invoiceProperties;

    /**
     * 根据项目编号,获取开票的配置信息.
     * @param projectNo 项目编号
     * @return 项目配置信息
     */
    public ProjectConfig findOneByProjectNo(final String projectNo) {
        ProjectConfig projectConfig = projectConfigMap.get(projectNo + "-invoice-config");
        if (Objects.isNull(projectConfig)) {
            JSONObject request = new JSONObject();
            request.put("projectNo", projectNo);
            log.info("ProjectConfigService.findOneByProjectNo request: {}", request);
            JSONObject response = HttpUtils.sendPost(invoiceProperties.getDataUrl(), request.toJSONString());
            log.info("ProjectConfigService.findOneByProjectNo response: {}", response);
            if (Objects.nonNull(response) && response.containsKey("code") && response.getString("code").equals("00000")) {
                JSONArray configArray = response.getJSONArray("projectConfig");
                List<ProjectConfig> configLists = configArray.toJavaList(ProjectConfig.class);
                if (configLists.size() > 0) {
                    projectConfig = configLists.get(0);
                    projectConfigMap.put(projectNo + "-invoice-config", projectConfig);
                }
            }
        }
        return projectConfig;
    }
}
