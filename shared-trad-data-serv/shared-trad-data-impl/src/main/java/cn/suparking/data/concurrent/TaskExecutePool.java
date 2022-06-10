package cn.suparking.data.concurrent;

import cn.suparking.data.configuration.properties.ThreadPoolProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class TaskExecutePool {

    @Resource
    private ThreadPoolProperties threadPoolProperties;

    private ThreadPoolTaskExecutor executor;

    /**
     * init Thread pool.
     * @return {@link Executor}
     */
    @Bean
    @Scope("prototype")
    public Executor shardTradingExecutor() {
        executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPoolProperties.getCoreSize());
        executor.setMaxPoolSize(threadPoolProperties.getMaxSize());
        executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
        executor.setKeepAliveSeconds(threadPoolProperties.getKeepAlive());
        executor.setThreadNamePrefix("<ShardTradingData>");

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    /**
     * getThreadPoolTaskExecutor.
     * @return {@link ThreadPoolTaskExecutor}
     */
    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        return executor;
    }
}
