package cn.suparking.data.initializer;

import cn.suparking.data.configuration.properties.SparkProperties;
import cn.suparking.data.tools.ProjectConfigUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

@Slf4j
@Component
public class SharedTradingDataInitializer implements ApplicationRunner {

    @Resource
    private SparkProperties sparkProperties;

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        try {
            ProjectConfigUtils.init(sparkProperties.getUrl());
            log.info("Project Config Init Complete : " + ProjectConfigUtils.show());
        } catch (Exception ex) {
            Arrays.stream(ex.getStackTrace()).forEach(item -> log.error(item.toString()));
        }
    }
}
