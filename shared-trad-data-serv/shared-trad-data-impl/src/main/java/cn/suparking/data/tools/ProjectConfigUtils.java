package cn.suparking.data.tools;

import cn.suparking.common.api.utils.HttpUtils;
import cn.suparking.data.Application;
import cn.suparking.data.api.beans.ParkConfigDTO;
import cn.suparking.data.configuration.properties.SparkProperties;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static cn.suparking.data.api.constant.DataConstant.REQUEST_PROJECT_CONFIG;

public class ProjectConfigUtils {

    private static final Object LOCK = new Object();

    private static final Map<String, Map<String, Object>> PROJECT_CONFIG_MAP = new ConcurrentHashMap<>();

    private final SparkProperties sparkProperties = Application.getBean("SparkProperties", SparkProperties.class);

    /**
     * save park config.
     * @param parkConfigDTO {@link ParkConfigDTO}
     */
    public static void push(final ParkConfigDTO parkConfigDTO) {
        synchronized (LOCK) {
            Map<String, Object> content = PROJECT_CONFIG_MAP.get(parkConfigDTO.getProjectNo());
            if (Objects.nonNull(content)) {
                content.put(parkConfigDTO.getResource(), parkConfigDTO.getData());
            } else {
                content = new ConcurrentHashMap<>(1);
                content.put(parkConfigDTO.getResource(), parkConfigDTO.getData());
            }
            PROJECT_CONFIG_MAP.put(parkConfigDTO.getProjectNo(), content);
        }
    }

    /**
     * get data.
     * @param projectNo projectNo.
     * @param resource resource
     * @return Object
     */
    public static Object poll(final String projectNo, final String resource) {
        if (Objects.nonNull(PROJECT_CONFIG_MAP.get(projectNo))) {
            return PROJECT_CONFIG_MAP.get(projectNo).get(resource);
        }
        return null;
    }

    /**
     * delete project resource.
     * @param projectNo project
     * @param resource resource
     */
    public static void remove(final String projectNo, final String resource) {
        if (Objects.nonNull(PROJECT_CONFIG_MAP.get(projectNo))) {
            synchronized (LOCK) {
                PROJECT_CONFIG_MAP.get(projectNo).remove(resource);
            }
        }
    }

    /**
     * 全量拉取项目配置.
     * @param url request url.
     */
    public static void init(final String url) {
        invoke(url);
    }

    /**
     * 遍历.
     * @return String
     */
    public static String show() {
        return JSON.toJSONString(PROJECT_CONFIG_MAP);
    }

    private static void invoke(final String url) {
        JSONObject result = HttpUtils.sendGet(url + REQUEST_PROJECT_CONFIG, null);
        if (Objects.nonNull(result) && result.containsKey("code") && result.getString("code").equals("00000")) {
            JSONArray jsonArray = result.getJSONArray("data");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject tmp = jsonArray.getJSONObject(i);
                ParkConfigDTO parkConfigDTO = ParkConfigDTO.builder()
                        .projectNo(tmp.getString("projectNo"))
                        .resource(tmp.getString("resource"))
                        .data(tmp.getString("data"))
                        .build();
                push(parkConfigDTO);
            }
        }
    }
}
